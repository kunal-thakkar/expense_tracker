package expense.tracker.parser.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

import expense.tracker.dao.Transaction;
import expense.tracker.parser.StatementParser;
import expense.tracker.util.Utils;

public class HdfcCrStatementParser implements StatementParser {
	
	private Pattern subPattern;
	private Pattern fileNamePattern;
	
	public HdfcCrStatementParser() {
		subPattern = Pattern.compile("^(.*)?Your HDFC Bank (Regalia|MoneyBack|Platinum Plus) MasterCard Credit Card Statement.*.-[0-9]{4}");
		fileNamePattern = Pattern.compile("[0-9X]{16}_[0-9]{2}-[0-9]{2}-[0-9]{4}.PDF");
	}

	@Override
	public boolean isSubjectMatches(String sub) {
		return subPattern.matcher(sub).matches();
	}

	@Override
	public boolean isFileNameMatches(String fileName) {
		return fileNamePattern.matcher(fileName).matches();
	}

	@Override
	public String getPassword(String subject) {
		if(subject.contains("Regalia")) {
			return "KUNA4932";
		}
		else if(subject.contains("MoneyBack")) {
			return "KUNA9336";
		}
		else {
			return "KUNA7139";
		}
	}

	@Override
	public String getType() {
		return "HDFC";
	}

	public List<Transaction> parseStatement(File file, String password) throws InvalidPasswordException, IOException{
		List<Transaction> result = new ArrayList<Transaction>();
        PDFTextStripper reader = new PDFTextStripper();
        PDDocument doc = PDDocument.load(file, password);
        String pageText = reader.getText(doc);
        String[] lines = pageText.split(Utils.newLineSeparator);
        String[] linePart, datePart;
        String desc, line, transactionType, paymentMode = "HDFC Cr. Card";
        int len;
        boolean is_first_match = true;
        for(int i = 0; i < lines.length; i++){
        	line = lines[i].trim(); 
        	if(line.matches("\\d{2}/\\d{2}/\\d{4}.*$")){
        		if(is_first_match){
        			is_first_match = false;
        			continue;
				}
        		if(line.endsWith(" Cr")) {
					transactionType = "cr";
					line = line.substring(0, line.length() - 3);
				}
				else {
					transactionType = "dr";
				}
        		linePart = line.split(" ");
        		datePart = linePart[0].split("/");
        		desc = "";
        		len = linePart.length;
        		desc = linePart[1];
        		for(int c = 2; c < len -1; c++){
        			if(linePart[c].length() != 0)
        			desc += " " + linePart[c];
				}
				result.add(new Transaction(
					Utils.getDateTime(datePart[2], datePart[1], datePart[0]), 
					"", paymentMode, transactionType, desc.replaceAll(",", " "), linePart[len-1])
				);
			}
		}
        doc.close();
        return result;
	}

}
