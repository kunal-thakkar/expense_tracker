package expense.tracker;

import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.rapidoid.setup.App;

import expense.tracker.database.DB;
import expense.tracker.mail.MailClient;

public class Main {
	static final int port = 8080;
	public static DB db;
	
	@SuppressWarnings("unchecked")
	public void init(String[] args) throws Exception {
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(new FileReader(new File("config.json")));
		
		JSONObject dbJsonObj = (JSONObject) obj.get("database");
		String url = String.format("jdbc:%s:", dbJsonObj.getOrDefault("server", "sqlite"));
		if(dbJsonObj.containsKey("host")) {
			url += String.format("//%s/%s", dbJsonObj.get("host"), dbJsonObj.get("database"));
		}
		else {
			url += dbJsonObj.get("database");
		}
		if(dbJsonObj.containsKey("username") && dbJsonObj.containsKey("password")) {
			url += String.format("?user=%s&password=%s", dbJsonObj.get("username"), dbJsonObj.get("password"));
		}
		db = new DB(url);
		db.init();
		
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
		
		for(int i = 0; i < args.length; i++) {
			if(args[i].equals("fetch")) {
				i++;
				LocalDate date = LocalDate.parse(args[i]);
				client.fetchHistory(folders, java.sql.Date.valueOf(date), new MessageHandlerImpl());
				System.out.println("Scanning completed");
			}
			if(args[i].equals("run")) {
				App.bootstrap(args).auth();
				System.out.println("Web-Application started");
			}
			if(args[i].equals("monitor")) {
				client.monitor(folders, new MessageHandlerImpl());
				System.out.println("Monitoring mail-box");
			}
		}
	}
	
	public static void main(String args[]) throws Exception {
		new Main().init(args);
	}
}
