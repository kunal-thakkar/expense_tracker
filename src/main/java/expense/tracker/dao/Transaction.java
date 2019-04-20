package expense.tracker.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "transactions")
public class Transaction {

	@DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
	private Integer transactionId;
	
	@DatabaseField
	private long dateTime;
	
	@DatabaseField
	private String category;
	
	@DatabaseField
	private String paymentMode;
	
	@DatabaseField
	private String transactionType;
	
	@DatabaseField
	private String description;
	
	@DatabaseField
	private double amount;
	
	public Transaction() {
		
	}
	
	public Transaction(long dateTime, String category, String paymentMode, String transactionType, String description, double amount) {
		this.dateTime = dateTime;
		this.category = category;
		this.paymentMode = paymentMode;
		this.transactionType = transactionType;
		this.description = description;
		this.amount = amount;
	}

	public Transaction(long dateTime, String category, String paymentMode, String transactionType, String description, String amount) {
		this.dateTime = dateTime;
		this.category = category;
		this.paymentMode = paymentMode;
		this.transactionType = transactionType;
		this.description = description;
		this.amount = Double.valueOf(amount.replaceAll(",", ""));
	}

	public Integer getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}
	
	public long getDateTime() {
		return dateTime;
	}

	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
