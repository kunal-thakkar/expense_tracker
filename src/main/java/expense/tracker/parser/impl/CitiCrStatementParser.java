package expense.tracker.parser.impl;

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

public class CitiCrStatementParser implements StatementParser {
	
	private Pattern subPattern;
	private Pattern fileNamePattern;
	
	public CitiCrStatementParser() {
		subPattern = Pattern.compile("Statement Online - CITI CASH BACK TITANIUM CARD");
		fileNamePattern = Pattern.compile("CITI CASH BACK TITANIUM CARD.pdf");
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
		return "KUNA16APR";
	}

	@Override
	public String getType() {
		return "CITI";
	}
	
	public List<Transaction> parseStatement(PDDocument pdf) throws InvalidPasswordException, IOException{
		List<Transaction> result = new ArrayList<Transaction>();
        PDFTextStripper reader = new PDFTextStripper();
        String pageText = reader.getText(pdf);
        String[] lines = pageText.split(Utils.newLineSeparator);
        String[] linePart, datePart;
        String desc, transactionType, paymentMode = "CITI Cr. Card";
        int year = 0, len;
        for(int i = 0; i < lines.length; i++){
        	if(lines[i].matches("^\\d{2}/\\d{2} .*$")){
        		linePart = lines[i].split(" ");
        		datePart = linePart[0].split("/");
        		desc = "";
        		len = linePart.length;
        		desc = linePart[1];
        		for(int c = 2; c < len -1; c++){
        			if(linePart[c].length() != 0)
        			desc += " " + linePart[c];
				}
				if(linePart[len-1].endsWith("CR")){
					transactionType = "cr";
					linePart[len-1] = linePart[len-1].substring(0, linePart[len-1].length()-2); 
				}
				else {
					transactionType = "dr";
				}
				result.add(new Transaction(
					Utils.getDateTime("20" + year, datePart[1], datePart[0]), 
					"", paymentMode, transactionType, desc, linePart[len-1])
				);
			}
        	else if(lines[i].matches("^\\d{2}/\\d{2}/\\d{2}.*$")){
        		year = Integer.parseInt(lines[i].split("/")[2].trim());
			}
		}
        pdf.close();
        return result;
	}

}
