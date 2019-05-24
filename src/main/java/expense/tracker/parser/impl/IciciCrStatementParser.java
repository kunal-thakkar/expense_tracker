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

public class IciciCrStatementParser implements StatementParser {
	
	private Pattern subPattern;
	private Pattern fileNamePattern;
	
	public IciciCrStatementParser() {
		subPattern = Pattern.compile("^(.*)?Your Credit Card Statement for the period.*.( to ).*");
		fileNamePattern = Pattern.compile("4477 XXXX XXXX 0002-[0-9]{0,10}.pdf");
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
		return "kuna1604";
	}

	@Override
	public String getType() {
		return "ICICI";
	}

	public List<Transaction> parseStatement(File file, String password) throws InvalidPasswordException, IOException{
		List<Transaction> result = new ArrayList<Transaction>();
        PDFTextStripper reader = new PDFTextStripper();
        PDDocument doc = PDDocument.load(file, password);
        String pageText = reader.getText(doc);
        String[] lines = pageText.split(Utils.newLineSeparator), linePart, datePart;
        String desc, line, tempLine = "", transactionType, paymentMode = "ICICI Cr. Card";
        int len;
        boolean inTable = false, isComplete = true;
        for(int i = 0; i < lines.length; i++){
        	line = lines[i].trim();
        	if(line.startsWith("TRANSACTION DETAILS") || 
			line.startsWith("Card Number : ") ||
			line.startsWith("Great offers on your card")
        	){
        		inTable = false;
        		continue;
			}
        	else if(line.equals("Ref. Number")){
        		inTable = true;
        		continue;
			}
        	if(inTable){
            	if(lines[i].matches("^?\\d{2,9}/\\d{2}/\\d{4}.*$")){
            		if(!line.matches(".*\\d{1,5}\\.\\d{1,10}.*")){
            			isComplete = false;
            			tempLine += line + " ";
					}
            		else {
            			isComplete = true;
					}
				}
            	else {
            		if(!isComplete){
            			tempLine += " " + line;
                		if(line.matches(".*\\d{1,5}\\.\\d{1,10}.*")){
                			isComplete = true;
                			line = tempLine;
                			tempLine = "";
						}
					}
				}
            	if(isComplete){
            		linePart = line.split(" ");
            		datePart = linePart[0].split("/");
            		len = datePart[0].length();
            		if(len > 2) datePart[0] = datePart[0].substring(len - 2);
            		len = linePart.length;
            		if(linePart[len-1].indexOf("CR") != -1){
						transactionType = "cr";
                		len--;
					}
            		else {
						transactionType = "dr";
            			linePart[len-1] = linePart[len-1].substring(0, linePart[len-1].lastIndexOf(".") + 3);
					}
            		desc = linePart[1];
            		for(int c = 2; c < len-1; c++){
            			desc += " " + linePart[c];
					}
					result.add(new Transaction(
							Utils.getDateTime(datePart[2], datePart[1], datePart[0]), 
							"", paymentMode, transactionType, desc.replaceAll(",", " "), linePart[len-1])
					);
				}
			}
		}
        doc.close();
        return result;
	}

}
