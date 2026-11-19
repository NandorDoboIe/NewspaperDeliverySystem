package CLI;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import Database.DAO;
import User.User;

public class UserCLI {

    // ---- Entity configuration: change these when porting to another object ----
    private static final String TABLE = "users";
    private static final String ID_KEY = "userId";
    private static final String ENTITY = "User";
    private static final String ENTITY_PLURAL = "Users";
    private static final List<String> FIELDS = List.of("name", "password", "contact", "access_level");
    // ----------------------------------------------------------------------------

    private static final int MAX_LOGIN_ATTEMPTS = 3;

    private final Scanner scanner;

    public UserCLI(Scanner scanner) {
        this.scanner = scanner;
    }

    /** Asks for name and password; returns the matching user, or null after too many failed attempts. */
    public User login(ArrayList<User> users) {
        if (users.isEmpty()) {
            System.out.println("No users exist. Run databaseSetup.sql to create the default admin.");
            return null;
        }
        System.out.println("\n## LOGIN ##");
        for (int attempt = 1; attempt <= MAX_LOGIN_ATTEMPTS; attempt++) {
            System.out.print("Name: ");
            if (!scanner.hasNextLine()) return null;
            String name = scanner.nextLine().trim();
            System.out.print("Password: ");
            if (!scanner.hasNextLine()) return null;
            String password = scanner.nextLine();

            User user = findUserByName(users, name);
            if (user != null && user.checkPassword(password)) {
                System.out.println("Welcome, " + user.getName() + " (" + user.getAccessLevel() + ")");
                return user;
            }
            System.out.println("Wrong name or password. " + (MAX_LOGIN_ATTEMPTS - attempt) + " attempt(s) left.");
        }
        return null;
    }

    public void showMenu(ArrayList<User> users, User currentUser) {
        while (true) {
            System.out.println("## " + ENTITY_PLURAL.toUpperCase() + " ##");
            System.out.println("1. Create " + ENTITY);
            System.out.println("2. View " + ENTITY_PLURAL);
            System.out.println("3. Update " + ENTITY);
            System.out.println("4. Delete " + ENTITY);
            System.out.println("0. Back to Main Menu");

            switch (readChoice()) {
            case 1:
                createUser(users);
                break;
            case 2:
                viewUsers(users);
                break;
            case 3:
                updateUser(users, currentUser);
                break;
            case 4:
                deleteUser(users, currentUser);
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice. Enter a menu number.");
            }
        }
    }

    private void createUser(ArrayList<User> users) {
        LinkedHashMap<String, Object> input = takeUserInput(FIELDS);
        try {
            User newUser = new User(input); // validates
            validateUniqueName(newUser, users);
            int newId = new DAO().addData(TABLE, newUser);
            if (newId <= 0) {
                System.out.println("Insert failed");
                return;
            }
            input.put(ID_KEY, newId);
            User saved = new User(input);
            users.add(saved);
            System.out.println(ENTITY + " created and uploaded to the database successfully");
            System.out.println(saved);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void viewUsers(ArrayList<User> users) {
        if (users.isEmpty()) {
            System.out.println(ENTITY_PLURAL + " is empty");
            return;
        }
        users.forEach(System.out::println);
    }

    private void updateUser(ArrayList<User> users, User currentUser) {
        User existing = selectUser(users, "update");
        if (existing == null) return;

        LinkedHashMap<String, Object> input = takeUserInput(FIELDS);
        input.put(ID_KEY, existing.getUserId());
        try {
            User updated = new User(input); // validates
            validateUniqueName(updated, users);
            // stops the logged in admin from locking themselves out of the admin menu
            if (existing.getUserId() == currentUser.getUserId() && !updated.isAdmin()) {
                System.out.println("You cannot remove admin access from your own account.");
                return;
            }
            if (new DAO().editRow(TABLE, updated, existing.getUserId())) {
                users.set(users.indexOf(existing), updated);
                System.out.println("Updated: " + updated);
            } else {
                System.out.println("Update failed");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void deleteUser(ArrayList<User> users, User currentUser) {
        User existing = selectUser(users, "delete");
        if (existing == null) return;

        if (existing.getUserId() == currentUser.getUserId()) {
            System.out.println("You cannot delete the account you are logged in with.");
            return;
        }

        if (!confirm("Are you sure you want to delete " + ENTITY.toLowerCase() + " " + existing.getUserId()
                + "? (y/n): ")) {
            System.out.println("Delete cancelled.");
            return;
        }

        if (new DAO().deleteRow(TABLE, existing.getUserId())) {
            users.remove(existing);
            System.out.println("Deleted.");
        } else {
            System.out.println("Delete failed");
        }
    }

    // login is by name, so two users cannot share one
    private void validateUniqueName(User user, List<User> users) {
        User sameName = findUserByName(users, user.getName());
        if (sameName != null && sameName.getUserId() != user.getUserId()) {
            throw new IllegalArgumentException("A user called " + user.getName() + " already exists.");
        }
    }

    private static User findUserByName(List<User> users, String name) {
        for (User user : users) {
            if (user.getName().equalsIgnoreCase(name)) return user;
        }
        return null;
    }

    private int readChoice() {
        System.out.print("Enter choice: ");
        if (!scanner.hasNextLine()) {
            return 0;
        }
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private boolean confirm(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) return false;
        String answer = scanner.nextLine().trim().toLowerCase();
        return answer.equals("y") || answer.equals("yes");
    }

    private LinkedHashMap<String, Object> takeUserInput(List<String> fields) {
        LinkedHashMap<String, Object> userInput = new LinkedHashMap<>();
        userInput.put(ID_KEY, 0);
        for (String field : fields) {
            if (field.equals("access_level")) {
                System.out.print("Please enter " + field + " (" + User.ADMIN + "/" + User.DELIVERY_PERSON + "): ");
            } else {
                System.out.print("Please enter " + field + ": ");
            }
            userInput.put(field, scanner.nextLine().trim());
        }
        return userInput;
    }

    /** Lists items, asks for an id, and returns the match, or null (after printing why). */
    private User selectUser(ArrayList<User> users, String action) {
        viewUsers(users);
        System.out.print("Id of the " + ENTITY.toLowerCase() + " to " + action + ": ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid id.");
            return null;
        }
        for (User u : users) {
            if (u.getUserId() == id) {
                return u;
            }
        }
        System.out.println("No " + ENTITY.toLowerCase() + " with id " + id);
        return null;
    }
}
