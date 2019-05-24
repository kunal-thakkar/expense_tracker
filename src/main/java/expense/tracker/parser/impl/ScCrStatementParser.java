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

public class ScCrStatementParser implements StatementParser {
	
	private Pattern subPattern;
	private Pattern fileNamePattern;
	
	public ScCrStatementParser() {
		subPattern = Pattern.compile("^(.*)?Your eStatement for .*.( is now available. Ref:<).*.(>)");
		fileNamePattern = Pattern.compile("eStatement6048IN_[0-9]{4}-[0-9]{2}-[0-9]{2}_[0-9]{2}:[0-9]{2}.pdf");
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
		return "KUNA160486";
	}

	@Override
	public String getType() {
		return "Standard Chartered";
	}

	public List<Transaction> parseStatement(File file, String password) throws InvalidPasswordException, IOException{
		List<Transaction> result = new ArrayList<Transaction>();
        PDFTextStripper reader = new PDFTextStripper();
        PDDocument doc = PDDocument.load(file, password);
        String pageText = reader.getText(doc);
        String[] lines = pageText.split(Utils.newLineSeparator), linePart;
        String desc, line, transactionType, paymentMode = "SC Cr. Card";
        int len;
        for(int i = 0; i < lines.length; i++){
        	line = lines[i];
        	if(line.length() > 30 && line.matches(".*\\d{1,15}\\.\\d{2}(CR)?\\d{6}.*")){
				line = line.substring(28).trim();
        		if(line.endsWith("CR")){
        			transactionType = "cr";
        			line = line.substring(0, line.length() - 2);
				}
        		else {
        			transactionType = "dr";
				}
        		linePart = line.split(" ");
        		desc = "";
        		len = linePart.length;
        		desc = linePart[1];
        		for(int c = 2; c < len; c++){
        			if(linePart[c].length() != 0)
        			desc += " " + linePart[c];
				}
        		linePart[0] = linePart[0].replaceAll(",", "");
        		len = linePart[0].length();
        		result.add(new Transaction(
    				Utils.getDateTime(linePart[0].substring(len-2, len), 
						linePart[0].substring(len-4, len-2), 
						linePart[0].substring(len-6, len-4)
					), "", "paymentMode", transactionType, desc.replaceAll(",", " "), linePart[0].substring(0, len-6))
        		);
			}
        	else if(line.matches("^\\d{6}.*.\\d{1,15}\\.\\d{2}(CR|$)")){
        		line = line.replaceAll("\\s+", " ");
        		if(line.endsWith("CR")){
        			transactionType = "cr";
        			line = line.substring(0, line.length() - 2);
				}
        		else {
        			transactionType = "dr";
				}
        		linePart = line.split(" ");
        		desc = "";
        		len = linePart.length;
        		desc = linePart[1];
        		for(int c = 2; c < len - 1; c++){
        			if(linePart[c].length() != 0)
        			desc += " " + linePart[c];
				}
        		linePart[0] = linePart[0].replaceAll(",", "");
        		result.add(new Transaction(
    				Utils.getDateTime("20"+linePart[0].substring(4, 6), 
						linePart[0].substring(2, 4), 
						linePart[0].substring(0, 2)
					), "", paymentMode, transactionType, desc.replaceAll(",", " "), linePart[len-1])
        		);
			}
		}
        doc.close();
        return result;
	}

}
