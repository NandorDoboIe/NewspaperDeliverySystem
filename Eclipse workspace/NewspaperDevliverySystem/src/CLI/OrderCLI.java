package CLI;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import Database.DAO;
import Order.Order;

public class OrderCLI {

    // ---- Entity configuration: change these when porting to another object ----
    private static final String TABLE = "orders";
    private static final String ID_KEY = "orderId";
    private static final String ENTITY = "Order";
    private static final String ENTITY_PLURAL = "Orders";
    private static final List<String> FIELDS = List.of("customerId", "publicationId", "deliveryPattern");
    // ----------------------------------------------------------------------------

    private static final int LOW_STOCK_THRESHOLD = 5;

    private final Scanner scanner;

    public OrderCLI(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showMenu(ArrayList<Order> orders) {
        while (true) {
            System.out.println("## " + ENTITY_PLURAL.toUpperCase() + " ##");
            System.out.println("1. Create " + ENTITY);
            System.out.println("2. View " + ENTITY_PLURAL);
            System.out.println("3. Update " + ENTITY);
            System.out.println("4. Delete " + ENTITY);
            System.out.println("5. Restock " + ENTITY);
            System.out.println("0. Back to Main Menu");
            

            switch (readChoice()) {
            case 1:
                createPublication(orders);
                break;
            case 2:
                viewPublications(orders);
                break;
            case 3:
                updatePublication(orders);
                break;
            case 4:
                deletePublication(orders);
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice. Enter a menu number.");
            }
        }
    }

    private void createPublication(ArrayList<Order> orders) {
        LinkedHashMap<String, Object> input = takeUserInput(FIELDS);
        try {
            Order newPublication = new Order(input); // validates
            int newId = new DAO().addData(TABLE, newPublication);
            if (newId <= 0) {
                System.out.println("Insert failed");
                return;
            }
            input.put(ID_KEY, newId);
            Order saved = new Order(input);
            orders.add(saved);
            System.out.println(ENTITY + " created and uploaded to the database successfully");
            System.out.println(saved);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void viewPublications(ArrayList<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println(ENTITY_PLURAL + " is empty");
            return;
        }
        orders.forEach(System.out::println);
    }

    private void updatePublication(ArrayList<Order> orders) {
        Order existing = selectPublication(orders, "update");
        if (existing == null) return;

        LinkedHashMap<String, Object> input = takeUserInput(FIELDS);
        input.put(ID_KEY, existing.getPublicationID());
        try {
            Order updated = new Order(input); // validates
            if (new DAO().editRow(TABLE, updated, existing.getPublicationID())) {
                orders.set(orders.indexOf(existing), updated);
                System.out.println("Updated: " + updated);
            } else {
                System.out.println("Update failed");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void deletePublication(ArrayList<Order> orders) {
        Order existing = selectPublication(orders, "delete");
        if (existing == null) return;

        if (new DAO().deleteRow(TABLE, existing.getPublicationID())) {
            orders.remove(existing);
            System.out.println("Deleted.");
        } else {
            System.out.println("Delete failed");
        }
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

    private LinkedHashMap<String, Object> takeUserInput(List<String> fields) {
        LinkedHashMap<String, Object> userInput = new LinkedHashMap<>();
        userInput.put(ID_KEY, 0);
        for (String field : fields) {
            System.out.print("Please enter " + field + ": ");
            userInput.put(field, scanner.nextLine().trim());
        }
        return userInput;
    }

    /** Lists items, asks for an id, and returns the match, or null (after printing why). */
    private Order selectPublication(ArrayList<Order> orders, String action) {
        viewPublications(orders);
        System.out.print("Id of the " + ENTITY.toLowerCase() + " to " + action + ": ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid id.");
            return null;
        }
        for (Order p : orders) {
            if (p.getPublicationID() == id) {
                return p;
            }
        }
        System.out.println("No " + ENTITY.toLowerCase() + " with id " + id);
        return null;
    }
}
