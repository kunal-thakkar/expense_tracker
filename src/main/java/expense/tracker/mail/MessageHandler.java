package expense.tracker.mail;

import javax.mail.Message;

public interface MessageHandler {
	public void handle(Message msg);
}
