package User;

import java.util.LinkedHashMap;
import org.apache.commons.lang3.StringUtils;

import Database.DataProvider;

public class User implements DataProvider{

	// the only two access levels; admin can use everything,
	// a delivery person can only print the day's dockets and mark deliveries
	public static final String ADMIN = "admin";
	public static final String DELIVERY_PERSON = "deliveryPerson";

	private final int userId; // 0 means this record has not been inserted yet
	private String name;
	private String password; // plain text, as agreed for this project
	private String contact;
	private String accessLevel;
	
	// ID may be absent before insertion
	public User(LinkedHashMap<String, Object> row) {
		 this(optionalId(row),
		         (String) row.get("name"),
		         (String) row.get("password"),
		         (String) row.get("contact"),
		         (String) row.get("access_level"));
	}
	
	public User(int userId, String name, String password, String contact, String accessLevel) {
		if(verifyId(userId)) {
			this.userId = userId;
		}
		else {
			throw new IllegalArgumentException("Invalid ID");
		}
		setName(name);
		setPassword(password);
		setContact(contact);
		setAccessLevel(accessLevel);
	}
	
	
	public int getUserId() {return userId;}
	
	public String getName() {return name;}
	
	public String getPassword() {return password;}
	
	public String getContact() {return contact;}
	
	public String getAccessLevel() {return accessLevel;}
	
	public boolean isAdmin() {return accessLevel.equals(ADMIN);}
	
	public boolean checkPassword(String password) {
		return this.password.equals(password);
	}

	
	public void setName(String name) {
		if(verifyName(name)) {
			this.name = name.trim();
		}
		else {
			throw new IllegalArgumentException("Invalid Name");
		}
	}
	
	public void setPassword(String password) {
		if(verifyPassword(password)) {
			this.password = password;
		}
		else {
			throw new IllegalArgumentException("Invalid Password (at least 4 characters)");
		}
	}

	public void setContact(String contact) {
		if(verifyContact(contact)) {
			this.contact = contact;
		}
		else {
			throw new IllegalArgumentException("Invalid Contact");
		}
	}
	
	// stored as exactly "admin" or "deliveryPerson", whatever case was typed
	public void setAccessLevel(String accessLevel) {
		if(accessLevel != null && accessLevel.trim().equalsIgnoreCase(ADMIN)) {
			this.accessLevel = ADMIN;
		}
		else if(accessLevel != null && accessLevel.trim().equalsIgnoreCase(DELIVERY_PERSON)) {
			this.accessLevel = DELIVERY_PERSON;
		}
		else {
			throw new IllegalArgumentException("Invalid Access Level (" + ADMIN + " or " + DELIVERY_PERSON + ")");
		}
	}
	
	
	// the password is left out so it is never printed to the console
	@Override
	public String toString() {
		return "User ID: " + userId + " | Name: " + name + " | Contact: " + contact + " | Access Level: " + accessLevel;
	}
	
	
	private boolean verifyId(int id) {	
		return id >= 0;
	}
	
	private boolean verifyName(String name){
		if(name == null) {
			return false;
		}
        return !name.trim().equals("");
    }
	
	private boolean verifyPassword(String password){
		if(password == null) {
			return false;
		}
        return password.length() >= 4;
    }

	private boolean verifyContact(String contact){
		if(contact == null) {
			return false;
		}
        if(contact.length() >= 7 && contact.length() <= 11){
            return StringUtils.isNumeric(contact);
        }
        return false;
    }

	public void updateUser(String name, String password, String contact, String accessLevel){
        setName(name);
        setPassword(password);
		setContact(contact);
		setAccessLevel(accessLevel);
    }
	
	
	public LinkedHashMap<String,Object> getData(){
		LinkedHashMap<String,Object> data = new LinkedHashMap<>();
		data.put("name",this.getName());
		data.put("password",this.getPassword());
		data.put("contact",this.getContact());
		data.put("access_level",this.getAccessLevel());
		return data;
	}
	
	private static int optionalId(LinkedHashMap<String, Object> row) {
		return row.containsKey("userId") ? toInt(row.get("userId")) : 0;
	}
	
	private static int toInt(Object value) {
	    if (value instanceof Number) {
	      return ((Number) value).intValue();
	    }
	    try {
	      return Integer.parseInt(String.valueOf(value).trim());
	    } catch (NumberFormatException ex) {
	      throw new IllegalArgumentException("Invalid number: " + value);
	    }
	}
}
