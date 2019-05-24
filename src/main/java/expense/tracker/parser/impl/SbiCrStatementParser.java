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

public class SbiCrStatementParser implements StatementParser {
	
	private Pattern subPattern;
	private Pattern fileNamePattern;
	
	public SbiCrStatementParser() {
		subPattern = Pattern.compile("^(.*)?Your SBI Card Monthly Statement -.*");
		fileNamePattern = Pattern.compile("5264685420147702_[0-9]{8}.pdf");
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
		return "5264685424183125";
	}

	@Override
	public String getType() {
		return "SBI";
	}

	public List<Transaction> parseStatement(File file, String password) throws InvalidPasswordException, IOException{
		List<Transaction> result = new ArrayList<Transaction>();
        PDFTextStripper reader = new PDFTextStripper();
        PDDocument doc = PDDocument.load(file, password);
        String pageText = reader.getText(doc);
        String[] lines = pageText.split(Utils.newLineSeparator), linePart;
        String desc, line, tempLine = "", transactionType, paymentMode = "SBI Cr. Card";
        int len;
        boolean is_split_line = false, lineFound = false;
        for(int i = 0; i < lines.length; i++){
        	line = lines[i].trim(); 
        	if(is_split_line){
        		tempLine += " " + line;
        		if(tempLine.endsWith(" D") || tempLine.endsWith(" C")){
        			is_split_line = false;
        			line = tempLine;
        			lineFound = true;
				}
			}
        	else if(line.matches("\\d{2}\\s(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s\\d{2}.*$")){
        		if(line.length() == 11){
        			continue;
				}
        		else if(line.length() == 9){
        			is_split_line = true;
        			tempLine = line;
        			lineFound = false;
        			continue;
				}
        		else{
        			lineFound = true;
				}
			}
        	else {
        		lineFound = false;
			}
            if(lineFound) {
				transactionType = line.endsWith(" C")?"cr":"dr";
				line = line.substring(0, line.length() - 2);
        		linePart = line.split(" ");
        		desc = "";
        		len = linePart.length;
        		desc = linePart[3];
        		for(int c = 4; c < len -1; c++){
        			if(linePart[c].length() != 0)
        			desc += " " + linePart[c];
				}
				result.add(new Transaction(
					Utils.getDateTime("20"+linePart[2], String.valueOf(Utils.months.indexOf(linePart[1])+1), linePart[0]), 
					"", paymentMode, transactionType, desc.replaceAll(",", " "), linePart[len-1])
				);
			}
		}
        doc.close();
        return result;
	}

}
