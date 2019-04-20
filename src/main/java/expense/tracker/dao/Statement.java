package expense.tracker.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Statement {

	@DatabaseField
	private long dateTime;
	
	@DatabaseField
	private String fileName;
	
	@DatabaseField
	private String password;
	
	@DatabaseField
	private String filePath;
	
	@DatabaseField
	private String statementType;
	
	public Statement() {
		
	}

	public Statement(long dateTime, String fileName, String password, String filePath, String statementType) {
		this.dateTime = dateTime;
		this.fileName = fileName;
		this.password = password;
		this.filePath = filePath;
		this.statementType = statementType;
	}

	public long getDateTime() {
		return dateTime;
	}

	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getStatementType() {
		return statementType;
	}

	public void setStatementType(String statementType) {
		this.statementType = statementType;
	}
	
	
}
