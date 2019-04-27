package expense.tracker.database;

import java.util.List;
import java.util.Map;

import org.apache.commons.pool2.impl.GenericObjectPool;

import expense.tracker.database.SQLite.CustomQueryBuilder;

public class DB {

	private GenericObjectPool<SQLite> sqlitePool;
	
	public DB(String dbPath) throws ClassNotFoundException {
		// load the sqlite-JDBC driver using the current class loader
	    Class.forName("org.sqlite.JDBC");
		sqlitePool = new GenericObjectPool<SQLite>(new SQLiteFactory(dbPath));
	}
	
	public <T> T getUniqueRecord(Object id, Class<T> t) throws Exception {
		SQLite sqlite = sqlitePool.borrowObject();
		T temp = sqlite.getUniqueRecord(id, t);
		sqlitePool.returnObject(sqlite);
		return temp;
	}
	
	public <T> List<T> getRecords(Map<String, Object> fieldValues, Class<T> t) throws Exception{
		SQLite sqlite = sqlitePool.borrowObject();
		List<T> temp = sqlite.getRecords(fieldValues, t);
		sqlitePool.returnObject(sqlite);
		return temp;
	}
	
	public void init() throws Exception {
		SQLite sqlite = sqlitePool.borrowObject();
		sqlite.init();
		sqlitePool.returnObject(sqlite);
	}

	public <T> List<T> getRecord(T o, Class<T> t) throws Exception {
		SQLite sqlite = sqlitePool.borrowObject();
		List<T> r = sqlite.getRecord(o, t);
		sqlitePool.returnObject(sqlite);
		return r;
	}
	
	public <T> List<T> getRecords(CustomQueryBuilder builder, Class<T> t) throws Exception{
		SQLite sqlite = sqlitePool.borrowObject();
		List<T> r = sqlite.getRecords(builder, t);
		sqlitePool.returnObject(sqlite);
		return r;
	}

	public <T> void saveRecords(List<T> records, Class<T> t) throws Exception {
		SQLite sqlite = sqlitePool.borrowObject();
		sqlite.saveRecords(records, t);
		sqlitePool.returnObject(sqlite);
	}

	public <T> void saveRecord(T record, Class<T> t) throws Exception {
		SQLite sqlite = sqlitePool.borrowObject();
		sqlite.saveRecord(record, t);
		sqlitePool.returnObject(sqlite);
	}

	public <T> int deleteRecord(T record, Class<T> t) throws Exception {
		SQLite sqlite = sqlitePool.borrowObject();
		int i = sqlite.deleteRecord(record, t);
		sqlitePool.returnObject(sqlite);
		return i;
	}
}
