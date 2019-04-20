package expense.tracker.mail;

import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.util.MailSSLSocketFactory;

public class MailClient {

	private Properties properties;
	private Authenticator authenticator;
	private IMAPStore store = null;

	public MailClient(String username, String password, String host, int port, String protocol)
			throws GeneralSecurityException, MessagingException {
		MailSSLSocketFactory socketFactory = new MailSSLSocketFactory();
		socketFactory.setTrustAllHosts(true);

		properties = new Properties();
		// properties.put("mail.debug", "true");
		properties.put("mail.store.protocol", protocol);
		properties.put("mail.imaps.host", host);
		properties.put("mail.imaps.port", port);
		properties.put("mail.imaps.timeout", "10000");
		properties.put("mail.imaps.ssl.socketFactory", socketFactory);

		authenticator = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		};
		Session session = Session.getInstance(properties, authenticator);

		store = (IMAPStore) session.getStore(protocol);
		store.connect();

		store.addConnectionListener(new ConnectionListener() {
			@Override
			public void opened(ConnectionEvent e) {

			}

			@Override
			public void disconnected(ConnectionEvent e) {
				try {
					store.connect();
				} catch (MessagingException e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void closed(ConnectionEvent e) {
				System.out.println("Mail client closed!");
			}
		});

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					if (store.isConnected())
						store.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void fetchHistory(String[] folders, Date upTo, MessageHandler handler) throws MessagingException {
		if (!store.isConnected())
			store.connect();
		for (String folderName : folders) {
			Folder folder = store.getFolder(folderName);
			if (!folder.isOpen())
				folder.open(Folder.READ_ONLY);
			Message[] messages = folder.getMessages();
			for (int i = messages.length - 1; i >= 0; i--) {
				Message msg = messages[i];
				if (msg.getReceivedDate().before(upTo)) {
					break;
				}
				handler.handle(msg);
			}
			if (folder != null && folder.isOpen()) {
				folder.close(false);
			}
		}
	}

	public void monitor(String[] folders, MessageHandler handler) throws MessagingException {
		for (String folderName : folders) {
			IMAPFolder folder = (IMAPFolder) store.getFolder(folderName);
			if (!folder.isOpen())
				folder.open(Folder.READ_ONLY);
			folder.addMessageCountListener(new MessageCountListener() {
				@Override
				public void messagesRemoved(MessageCountEvent e) {

				}

				@Override
				public void messagesAdded(MessageCountEvent e) {
					for (Message msg : e.getMessages()) {
						handler.handle(msg);
					}
				}
			});
			folder.idle();
		}
	}
}
