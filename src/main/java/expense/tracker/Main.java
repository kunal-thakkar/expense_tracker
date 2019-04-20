package expense.tracker;

import java.io.File;
import java.io.FileReader;
import java.util.Calendar;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.rapidoid.setup.App;

import expense.tracker.database.DB;
import expense.tracker.mail.MailClient;

public class Main {
	static final int port = 8080;
	public static DB db;
	
	public void init(String[] args) throws Exception {
		db = new DB("expense-tracker.db");
		db.init();
		
		App.bootstrap(args).auth();
		
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(new FileReader(new File("config.json")));
		
		JSONObject mailAccount = (JSONObject) obj.get("mail_account");
		String[] folders = null;
		if(mailAccount.containsKey("folders")){
			JSONArray fs = (JSONArray) mailAccount.get("folders");
			folders = new String[fs.size()];
			for(int i = 0; i < fs.size(); i++){
				folders[i] = (String) fs.get(i);
			}
		}
		
		MailClient client = new MailClient(
				mailAccount.get("username").toString(), 
				mailAccount.get("password").toString(), 
				mailAccount.get("host").toString(), 
				Integer.parseInt(mailAccount.get("port").toString()), 
				mailAccount.get("protocol").toString()
		);
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(2019, 2, 1);
		//client.fetchHistory(folders, calendar.getTime(), new MessageHandlerImpl());
		client.monitor(folders, new MessageHandlerImpl());
		System.out.println("Scanning completed");
	}
	
	public static void main(String args[]) throws Exception {
		new Main().init(args);
	}
}
