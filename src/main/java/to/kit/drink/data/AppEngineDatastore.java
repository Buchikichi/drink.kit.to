package to.kit.drink.data;

import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

class AppEngineDatastore implements DataAccessor {
	public void save(TableRecord rec) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity foo = new Entity(rec.getTable());

		for (Map.Entry<String, Object> entry : rec.entrySet()) {
			foo.setProperty(entry.getKey(), entry.getValue());
		}
		datastore.put(foo);
	}

	@Override
	public Map<String, String> read(TableRecord rec) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, String>> list(String kind) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
