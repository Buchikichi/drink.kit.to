package to.kit.drink.data;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.services.datastore.DatastoreV1.BeginTransactionRequest;
import com.google.api.services.datastore.DatastoreV1.BeginTransactionResponse;
import com.google.api.services.datastore.DatastoreV1.CommitRequest;
import com.google.api.services.datastore.DatastoreV1.Entity;
import com.google.api.services.datastore.DatastoreV1.EntityResult;
import com.google.api.services.datastore.DatastoreV1.GqlQuery;
import com.google.api.services.datastore.DatastoreV1.Key;
import com.google.api.services.datastore.DatastoreV1.PartitionId;
import com.google.api.services.datastore.DatastoreV1.Property;
import com.google.api.services.datastore.DatastoreV1.RunQueryRequest;
import com.google.api.services.datastore.DatastoreV1.RunQueryResponse;
import com.google.api.services.datastore.DatastoreV1.Value;
import com.google.api.services.datastore.client.Datastore;
import com.google.api.services.datastore.client.DatastoreException;
import com.google.api.services.datastore.client.DatastoreHelper;
import com.google.protobuf.ByteString;

class ApiServicesDatastore implements DataAccessor {
	public void create(TableRecord rec) throws GeneralSecurityException, DatastoreException, IOException {
		Datastore datastore = DatastoreHelper.getDatastoreFromEnv();
		BeginTransactionRequest.Builder treq = BeginTransactionRequest.newBuilder();
		BeginTransactionResponse tres = datastore.beginTransaction(treq.build());
		ByteString tx = tres.getTransaction();
		CommitRequest.Builder creq = CommitRequest.newBuilder();
		creq.setTransaction(tx);

		PartitionId partitionId = PartitionId.newBuilder().build();
		Key.Builder key = Key.newBuilder()
				.setPartitionId(partitionId)
				.addPathElement(Key.PathElement.newBuilder().setKind(rec.getTable()).setId(1)/*.setName(uuid)*/);
		Entity.Builder entityBuilder = Entity.newBuilder().setKey(key);
/*
		entityBuilder.addProperty(
				Property.newBuilder().setName("src").setValue(Value.newBuilder().setStringValue("neko.jpg")));
		entityBuilder
				.addProperty(Property.newBuilder().setName("magni").setValue(Value.newBuilder().setIntegerValue(331)));
		//*/
		for (Map.Entry<String, Object> entry : rec.entrySet()) {
			Object val = entry.getValue();
			Value.Builder builderForValue = Value.newBuilder();

			if (val instanceof Integer) {
				builderForValue.setIntegerValue(((Integer) val).longValue());
			} else if (val instanceof Double) {
				builderForValue.setDoubleValue(((Double) val).doubleValue());
			} else {
				builderForValue.setStringValue(String.valueOf(val));
			}
			entityBuilder.addProperty(Property.newBuilder().setName(entry.getKey()).setValue(builderForValue));
		}
		Entity entity = entityBuilder.build();

		creq.getMutationBuilder().addInsert(entity);
		datastore.commit(creq.build());
	}

	private Map<String, String> toMap(EntityResult entityResult) {
		Map<String, String> map = new HashMap<>();
		Entity entity = entityResult.getEntity();

		map.put("id", entity.getKey().getPathElement(0).getName());
		for (Property prop : entity.getPropertyList()) {
			String name = prop.getName();
			Value value = prop.getValue();

			map.put(name, value.getStringValue());
		}
		return map;
	}

	@Override
	public Map<String, String> read(TableRecord rec) throws Exception {
		Map<String, String> map = null;
		String kind = rec.getTable();
		String whereClause = "__key__ = Key(" + kind + ", '" + rec.getKey() + "') ";
		String queryString = "SELECT * FROM " + kind + " WHERE " + whereClause;
		Datastore datastore = DatastoreHelper.getDatastoreFromEnv();
		GqlQuery.Builder query = GqlQuery.newBuilder().setAllowLiteral(true).setQueryString(queryString);
		RunQueryRequest request = RunQueryRequest.newBuilder().setGqlQuery(query).build();
		RunQueryResponse response = datastore.runQuery(request);

		for (EntityResult entityResult : response.getBatch().getEntityResultList()) {
			map = toMap(entityResult);
			break;
		}
		return map;
	}

	@Override
	public List<Map<String, String>> list(String kind) throws GeneralSecurityException, IOException, DatastoreException {
		List<Map<String, String>> list = new ArrayList<>();
		Datastore datastore = DatastoreHelper.getDatastoreFromEnv();
		GqlQuery.Builder query = GqlQuery.newBuilder().setQueryString("SELECT * FROM " + kind);
		RunQueryRequest request = RunQueryRequest.newBuilder().setGqlQuery(query).build();
		RunQueryResponse response = datastore.runQuery(request);

		for (EntityResult entityResult : response.getBatch().getEntityResultList()) {
			Map<String, String> map = toMap(entityResult);

			list.add(map);
		}
		return list;
	}
}
