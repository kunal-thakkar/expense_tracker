package expense.tracker.parser;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import expense.tracker.dao.Transaction;

public interface StatementParser {
	
	public boolean isSubjectMatches(String sub);
	public boolean isFileNameMatches(String fileName);
	public String getPassword(String subject);
	public List<Transaction> parseStatement(PDDocument pdf) throws InvalidPasswordException, IOException;
	public String getType();

}
