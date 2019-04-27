package expense.tracker.controller;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.rapidoid.annotation.Controller;
import org.rapidoid.annotation.DELETE;
import org.rapidoid.annotation.POST;
import org.rapidoid.http.Req;
import org.rapidoid.http.Resp;
import org.rapidoid.u.U;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import expense.tracker.Main;
import expense.tracker.dao.Transaction;
import expense.tracker.database.SQLite;

@Controller
public class TransactionCtrl {

	@POST("/transaction")
	public Map<String, Object> transaction(Transaction t){
		try {
			Main.db.saveRecord(t, Transaction.class);
			return U.map("Status", "OK");
		} catch (Exception e) {
			e.printStackTrace();
			return U.map("Error", e.getMessage());
		}
	}
	
	@POST("/getTransactions")
	public List<Transaction> getTransactions(Req req, Resp resp) throws Exception {
		if(req.header("authorization", "").equals("")) {
			resp.code(404);
			return null;
		}
		List<Transaction> list = Main.db.getRecords(new SQLite.CustomQueryBuilder() {
			@Override
			public <T> void build(QueryBuilder<T, String> builder) throws SQLException {
				Calendar calFrom = Calendar.getInstance();
				calFrom.add(Calendar.YEAR, -1);
				Calendar calTo = Calendar.getInstance();
				Where<T, String> where = builder.where();
				where.between("dateTime", req.posted("from", calFrom.getTimeInMillis()), req.posted("to", calTo.getTimeInMillis()));
			}
		}, Transaction.class);
		return list;
	}
	
	@POST("/filterTransactions")
	public List<Transaction> filterTransactions(Req req, Resp resp) throws Exception {
		if(req.header("authorization", "").equals("")) {
			resp.code(404);
			return null;
		}
		List<Transaction> list = Main.db.getRecords(new SQLite.CustomQueryBuilder() {
			final Map<String, Object> posted = req.posted();
			@Override
			public <T> void build(QueryBuilder<T, String> builder) throws SQLException {
				Where<T, String> where = builder.where();
				int count = 0;
				if(posted.containsKey("from")) {
					where.ge("dateTime", posted.get("from"));
					count++;
				}
				if(posted.containsKey("to")) {
					where.le("dateTime", posted.get("to"));
					count++;
				}
				if(posted.containsKey("category") && !posted.get("category").equals("")) {
					where.eq("category", posted.get("category"));
					count++;
				}
				if(posted.containsKey("paymentMode") && !posted.get("paymentMode").equals("")) {
					where.eq("paymentMode", posted.get("paymentMode"));
					count++;
				}
				if(posted.containsKey("amountMin"))	{
					where.ge("amount", posted.get("amountMin"));
					count++;
				}
				if(posted.containsKey("amountMax")) {
					where.le("amount", posted.get("amountMax"));
					count++;
				}
				if(posted.containsKey("transactionType")) {
					where.eq("transactionType", posted.get("transactionType"));
					count++;
				}
				if(posted.containsKey("description") && !posted.get("description").equals("")) {
					where.like("description", "%"+posted.get("description")+"%");
					count++;
				}
				where.and(count);
			}
		}, Transaction.class);
		return list;
	}
	
	@DELETE("/transaction/{id}")
	public Map<String, Object> deleteTransaction(int id) throws Exception {
		Transaction t = new Transaction();
		t.setTransactionId(id);
		if(Main.db.deleteRecord(t, Transaction.class) == 1) {
			return U.map("Status", "OK");
		}
		else {
			return U.map("Error", "Record Not Found!");
		}
	}
}
