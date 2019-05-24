package expense.tracker.parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import expense.tracker.dao.Transaction;

public interface StatementParser {
	
	public boolean isSubjectMatches(String sub);
	public boolean isFileNameMatches(String fileName);
	public String getPassword(String subject);
	public List<Transaction> parseStatement(File file, String password) throws InvalidPasswordException, IOException;
	public String getType();

}
