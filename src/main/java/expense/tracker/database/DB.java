package expense.tracker.database;

import java.util.List;
import java.util.Map;

import org.apache.commons.pool2.impl.GenericObjectPool;

import expense.tracker.database.OrmLite.CustomQueryBuilder;

public class DB {

	private GenericObjectPool<OrmLite> ormLitePool;
	
	public DB(String url) throws ClassNotFoundException {
		ormLitePool = new GenericObjectPool<OrmLite>(new OrmLiteFactory(url));
	}
	
	public <T> T getUniqueRecord(Object id, Class<T> t) throws Exception {
		OrmLite sqlite = ormLitePool.borrowObject();
		T temp = sqlite.getUniqueRecord(id, t);
		ormLitePool.returnObject(sqlite);
		return temp;
	}
	
	public <T> List<T> getRecords(Map<String, Object> fieldValues, Class<T> t) throws Exception{
		OrmLite sqlite = ormLitePool.borrowObject();
		List<T> temp = sqlite.getRecords(fieldValues, t);
		ormLitePool.returnObject(sqlite);
		return temp;
	}
	
	public void init() throws Exception {
		OrmLite sqlite = ormLitePool.borrowObject();
		sqlite.init();
		ormLitePool.returnObject(sqlite);
	}

	public <T> List<T> getRecord(T o, Class<T> t) throws Exception {
		OrmLite sqlite = ormLitePool.borrowObject();
		List<T> r = sqlite.getRecord(o, t);
		ormLitePool.returnObject(sqlite);
		return r;
	}
	
	public <T> List<T> getRecords(CustomQueryBuilder builder, Class<T> t) throws Exception{
		OrmLite sqlite = ormLitePool.borrowObject();
		List<T> r = sqlite.getRecords(builder, t);
		ormLitePool.returnObject(sqlite);
		return r;
	}

	public <T> void saveRecords(List<T> records, Class<T> t) throws Exception {
		OrmLite sqlite = ormLitePool.borrowObject();
		sqlite.saveRecords(records, t);
		ormLitePool.returnObject(sqlite);
	}

	public <T> void saveRecord(T record, Class<T> t) throws Exception {
		OrmLite sqlite = ormLitePool.borrowObject();
		sqlite.saveRecord(record, t);
		ormLitePool.returnObject(sqlite);
	}

	public <T> int deleteRecord(T record, Class<T> t) throws Exception {
		OrmLite sqlite = ormLitePool.borrowObject();
		int i = sqlite.deleteRecord(record, t);
		ormLitePool.returnObject(sqlite);
		return i;
	}
}
