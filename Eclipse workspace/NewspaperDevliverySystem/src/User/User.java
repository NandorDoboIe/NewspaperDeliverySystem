package User;

import java.util.ArrayList;

public class User {

    private int id;
    private String name;
    private String password;
    private String contact;
    private String access_level;

    // Store all user accounts
    private static ArrayList<User> users = new ArrayList<>();

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

        users.add(newUser);
        return true;
    }

    // READ: Find a user by ID
    public User readUserAccount(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }

        return null;
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

    // DELETE: Remove a user by ID
    public boolean deleteUserAccount(int id) {
        User user = readUserAccount(id);

        if (user == null) {
            return false;
        }

        users.remove(user);
        return true;
    }

    // Display all user accounts
    public void listUserAccounts() {
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        for (User user : users) {
            System.out.println(
                "ID: " + user.id
                + ", Name: " + user.name
                + ", Contact: " + user.contact
                + ", Access Level: " + user.access_level
            );
        }
    }

    // Clear stored users between unit tests
    static void resetUsers() {
        users.clear();
    }
}
