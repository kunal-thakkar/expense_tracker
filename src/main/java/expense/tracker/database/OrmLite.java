package expense.tracker.database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import expense.tracker.dao.Statement;
import expense.tracker.dao.Transaction;
import expense.tracker.dao.User;

public class OrmLite {
	
	private ConnectionSource connectionSource;
	
	public OrmLite(String url) throws SQLException {
		connectionSource =  new JdbcConnectionSource(url);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					connectionSource.close();
				} catch (IOException e) {
				}
			}
		});
	}
	
	public void init() throws SQLException {
		TableUtils.createTableIfNotExists(connectionSource, Statement.class);
		TableUtils.createTableIfNotExists(connectionSource, User.class);
		TableUtils.createTableIfNotExists(connectionSource, Transaction.class);
	}

	public <T> List<T> getRecords(Map<String, Object> fieldValues, Class<T> t) throws SQLException{
		Dao<T, String> dao = DaoManager.createDao(connectionSource, t);
		List<T> result = dao.queryForFieldValuesArgs(fieldValues);
		dao.clearObjectCache();
		return result;
	}
	
	public interface CustomQueryBuilder {
		public <T> void build(QueryBuilder<T, String> builder) throws SQLException;
	}
	
	public <T> List<T> getRecords(CustomQueryBuilder builder, Class<T> t) throws SQLException{
		Dao<T, String> dao = DaoManager.createDao(connectionSource, t);
		QueryBuilder<T, String> queryBuilder = dao.queryBuilder();
		builder.build(queryBuilder);
		System.out.println(queryBuilder.prepareStatementString());
		List<T> result = dao.query(queryBuilder.prepare());
		return result;
	}
	
	public <T> T getUniqueRecord(Object id, Class<T> t) throws SQLException {
		Dao<T, Object> dao = DaoManager.createDao(connectionSource, t);
		T result = dao.queryForId(id);
		return result;
	}

	public <T> List<T> getRecord(T o, Class<T> t) throws SQLException {
		Dao<T, Object> dao = DaoManager.createDao(connectionSource, t);
		List<T> result = dao.queryForMatching(o);
		return result;
	}

	public <T> void saveRecords(List<T> records, Class<T> t) throws SQLException {
		Dao<T, Object> dao = DaoManager.createDao(connectionSource, t);
		dao.create(records);
	}

	public <T> void saveRecord(T record, Class<T> t) throws SQLException {
		Dao<T, Object> dao = DaoManager.createDao(connectionSource, t);
		dao.createOrUpdate(record);
	}

	public <T> int deleteRecord(T record, Class<T> t) throws SQLException {
		Dao<T, Object> dao = DaoManager.createDao(connectionSource, t);
		return dao.delete(record);
	}
	
}
