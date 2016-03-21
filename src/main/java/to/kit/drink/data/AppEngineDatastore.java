package to.kit.drink.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

class AppEngineDatastore implements DataAccessor {
	private DatastoreService dao = DatastoreServiceFactory.getDatastoreService();

	@Override
	public void save(TableRecord rec) {
		Entity entity = new Entity(rec.getTable(), rec.getKey());

		//entity.setProperty(Entity.KEY_RESERVED_PROPERTY, rec.getKey());
		for (Map.Entry<String, Object> entry : rec.entrySet()) {
			entity.setProperty(entry.getKey(), entry.getValue());
		}
		this.dao.put(entity);
	}

	@Override
	public Map<String, Object> read(TableRecord rec) throws Exception {
		String id = rec.getKey();
		Key key = KeyFactory.createKey(rec.getTable(), id);
		Entity entity = this.dao.get(key);
		Map<String, Object> map = new HashMap<>();

		map.putAll(entity.getProperties());
		map.put("id", id);
		return map;
	}

	@Override
	public List<Map<String, Object>> list(String kind) throws Exception {
		List<Map<String, Object>> list = new ArrayList<>();
		Query query = new Query(kind);
		PreparedQuery pq = this.dao.prepare(query);

		for (Entity entity : pq.asIterable()) {
			Map<String, Object> map = new HashMap<>();

			map.putAll(entity.getProperties());
			map.put("id", entity.getKey().getName());
			list.add(map);
		}
		return list;
	}
}
