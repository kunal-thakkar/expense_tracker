package expense.tracker.database;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class SQLiteFactory  extends BasePooledObjectFactory<SQLite>{

	String dbPath;
	
	public SQLiteFactory(String dbPath) {
		this.dbPath = dbPath;
	}
	
	@Override
	public SQLite create() throws Exception {
		return new SQLite(dbPath);
	}

	@Override
	public PooledObject<SQLite> wrap(SQLite obj) {
		return new DefaultPooledObject<SQLite>(obj);
	}

}
