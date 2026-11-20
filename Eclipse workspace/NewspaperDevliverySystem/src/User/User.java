package User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import Database.DataProvider;

public class User implements DataProvider{
	private static final Map<Integer, User> users = new LinkedHashMap<>();
    
	private int id;
    private String name;
    private String password;
    private String contact;
    private String access_level;


    // No-argument constructor for JUnit tests
    public User() {
    }

    // Constructor with user details
    public User(int id, String name, String password,
                String contact, String access_level) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.contact = contact;
        this.access_level = access_level;
    }
    
    

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getContact() {
        return contact;
    }

    public String getAccess_level() {
        return access_level;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setAccess_level(String access_level) {
        this.access_level = access_level;
    }

    // Verify user ID
    public boolean verifyId(int id) {
        return id > 0;
    }

    // Verify user name
    public boolean verifyName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    // Verify password
    public boolean verifyPassword(String password) {
        return password != null
            && !password.trim().isEmpty()
            && password.length() >= 8;
    }

    // Verify contact number: exactly 10 digits
    public boolean verifyContact(String contact) {
        return contact != null && contact.matches("\\d{10}");
    }

    // Verify access level
    public boolean verifyAccessLevel(String access_level) {
        if (access_level == null) {
            return false;
        }

        return access_level.equals("newspaper delivery person")
            || access_level.equals("news agent")
            || access_level.equals("admin");
    }

    // Verify all user details
    public boolean verifyUserAccount() {
        return verifyId(id)
            && verifyName(name)
            && verifyPassword(password)
            && verifyContact(contact)
            && verifyAccessLevel(access_level);
    }

    // CREATE: Add a new user
    public boolean createUserAccount(int id, String name,
            String password, String contact, String access_level) {

        User newUser = new User(
            id, name, password, contact, access_level
        );

        if (!newUser.verifyUserAccount()) {
            return false;
        }

        // Prevent duplicate IDs
        if (readUserAccount(id) != null) {
            return false;
        }

        return true;
    }

    // READ: Find a user by ID
    public User readUserAccount(int id) {
    	return users.get(id);
    }

    // UPDATE: Modify an existing user
    public boolean updateUserAccount(int id, String name,
            String password, String contact, String access_level) {

        User user = readUserAccount(id);

        if (user == null) {
            return false;
        }

        User updatedUser = new User(
            id, name, password, contact, access_level
        );

        if (!updatedUser.verifyUserAccount()) {
            return false;
        }

        user.setName(name);
        user.setPassword(password);
        user.setContact(contact);
        user.setAccess_level(access_level);

        return true;
    }
    
    public boolean deleteUserAccount() {
    	
    }

    public void listUserAccounts() {
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        for (User user : users.values()) {
            System.out.println("ID: " + user.getId()
                    + ", Name: " + user.getName()
                    + ", Contact: " + user.getContact()
                    + ", Access Level: " + user.getAccess_level());
        }
    }

    public LinkedHashMap<String,Object> getData(){
		LinkedHashMap<String,Object> data = new LinkedHashMap<>();
		data.put("name",this.getName());
		data.put("contact",this.getContact());
		data.put("access_level",this.getAccess_level());
		return data;
	}
}
