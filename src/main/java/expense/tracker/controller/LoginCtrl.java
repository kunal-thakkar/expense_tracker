package expense.tracker.controller;

import java.util.Map;
import java.util.UUID;

import org.rapidoid.annotation.Controller;
import org.rapidoid.annotation.POST;
import org.rapidoid.http.Req;
import org.rapidoid.u.U;

import expense.tracker.Main;
import expense.tracker.dao.User;

@Controller
public class LoginCtrl {

	@POST("/login")
    public Map<String, Object> login(Req req) throws Exception {
		User u = Main.db.getUniqueRecord(req.posted("username", ""), User.class);
		if(null == u) {
			return U.map("Error", "Invalid Username!!");
		}
		if(!req.posted("password", "").equals(u.getPassword())) {
			return U.map("Error", "Incorrect Password!!");
		}
		u.setToken(UUID.randomUUID().toString());
		Main.db.saveRecord(u, User.class);		
		return U.map("FirstName", u.getFirstname(),
				"FullName", String.format("%s %s", u.getFirstname(), u.getLastname()),
				"Role", u.getRole(),
				"token", u.getToken());
    }
	
}
