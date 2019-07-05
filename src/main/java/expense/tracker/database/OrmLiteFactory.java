package expense.tracker.database;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class OrmLiteFactory  extends BasePooledObjectFactory<OrmLite>{

	String dbPath;
	
	public OrmLiteFactory(String dbPath) {
		this.dbPath = dbPath;
	}
	
	@Override
	public OrmLite create() throws Exception {
		return new OrmLite(dbPath);
	}

	@Override
	public PooledObject<OrmLite> wrap(OrmLite obj) {
		return new DefaultPooledObject<OrmLite>(obj);
	}

}
