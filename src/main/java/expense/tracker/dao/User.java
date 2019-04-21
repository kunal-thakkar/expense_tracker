package expense.tracker.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "users")
public class User {

	@DatabaseField(id = true, canBeNull = false, unique = true, index = true)
	private String username;
    
    @DatabaseField(canBeNull = false, index = true)
    private String password;

	@DatabaseField
    private String firstname;
	
	@DatabaseField
	private String lastname;
	
	@DatabaseField
	private String role;
    
	@DatabaseField(unique = true, index = true)
	private String token;
	
    User() {
    	// all persisted classes must define a no-arg constructor with at least package visibility
    }

	public User(String username, String password, String firstname, String lastname, String role) {
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
