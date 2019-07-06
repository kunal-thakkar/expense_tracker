package expense.tracker;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.reflections.Reflections;

import expense.tracker.dao.Statement;
import expense.tracker.dao.Transaction;
import expense.tracker.mail.MessageHandler;
import expense.tracker.parser.StatementParser;

public class MessageHandlerImpl implements MessageHandler {

	private List<StatementParser> statementParsers;
	
	public MessageHandlerImpl() {
		statementParsers = new ArrayList<StatementParser>();
		Reflections reflections = new Reflections("expense.tracker");
		Set<Class<? extends StatementParser>> classes = reflections.getSubTypesOf(StatementParser.class);
		classes.forEach(new Consumer<Class<? extends StatementParser>>() {
			@Override
			public void accept(Class<? extends StatementParser> t) {
				try {
					statementParsers.add(t.newInstance());
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void writePart(String sub, StatementParser stmtParser, Part p, Date date) throws InvalidPasswordException, MessagingException, IOException, Exception {
		// if (p instanceof Message) writeEnvelope((Message) p);
		// check if the content has attachment
		if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) p.getContent();
			int count = mp.getCount();
			for (int i = 0; i < count; i++)
				writePart(sub, stmtParser, mp.getBodyPart(i), date);
		}
		// check if the content is a nested message
		else if (p.isMimeType("message/rfc822")) {
			writePart(sub, stmtParser, (Part) p.getContent(), ((Message) p).getReceivedDate());
		} else {
			Object o = p.getContent();
			if (o instanceof InputStream && p.getFileName() != null) {
				String fileName = p.getFileName();
				if (stmtParser.isFileNameMatches(fileName)) {
					Statement stmt = new Statement(date.getTime(), fileName, stmtParser.getPassword(sub), null, stmtParser.getType());
					List<Statement> extRecords = Main.db.getRecord(stmt, Statement.class);
					if(extRecords.size() == 0) {
						Path path = Paths.get("statements", stmtParser.getType(), 
								String.format("%d_%s", date.getTime(), fileName));
						Files.createDirectories(path.getParent());
						Files.copy((InputStream) o, path, StandardCopyOption.REPLACE_EXISTING);
						List<Transaction> transactions = stmtParser.parseStatement(
							path.toFile(), stmtParser.getPassword(sub)
						);
						Main.db.saveRecords(transactions, Transaction.class);
						stmt.setFilePath(path.toString());
						Main.db.saveRecord(stmt, Statement.class);
					}
				}
			}
		}
	}

	@Override
	public void handle(Message m) {
		for(StatementParser stmtParser : statementParsers) {
			try {
				if (stmtParser.isSubjectMatches(m.getSubject())) {
					writePart(m.getSubject(), stmtParser, m, m.getReceivedDate());
					break;
				}
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (InvalidPasswordException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
