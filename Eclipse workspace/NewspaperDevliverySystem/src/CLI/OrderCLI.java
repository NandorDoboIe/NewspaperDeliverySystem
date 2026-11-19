package CLI;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import Customer.Customer;
import Publication.Publication;
import Database.DAO;
import DeliveryDocket.DeliveryDocket;
import Order.Order;

public class OrderCLI {

    // ---- Entity configuration: change these when porting to another object ----
    private static final String TABLE = "orders";
    private static final String ID_KEY = "orderId";
    private static final String ENTITY = "Order";
    private static final String ENTITY_PLURAL = "Orders";
    private static final List<String> FIELDS = List.of("customerId", "publicationId", "pattern");
    // ----------------------------------------------------------------------------

    private static final int LOW_STOCK_THRESHOLD = 5;

    private final Scanner scanner;

    public OrderCLI(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showMenu(ArrayList<Order> orders, ArrayList<Customer> customers, ArrayList<Publication> publications,
            ArrayList<DeliveryDocket> deliveryDockets) {
        while (true) {
            System.out.println("## " + ENTITY_PLURAL.toUpperCase() + " ##");
            System.out.println("1. Create " + ENTITY);
            System.out.println("2. View " + ENTITY_PLURAL);
            System.out.println("3. Update " + ENTITY);
            System.out.println("4. Delete " + ENTITY);
            System.out.println("0. Back to Main Menu");
            

            switch (readChoice()) {
            case 1:
                createOrder(orders, customers, publications);
                break;
            case 2:
                viewOrders(orders);
                break;
            case 3:
                updateOrder(orders, customers, publications);
                break;
            case 4:
                deleteOrder(orders, deliveryDockets);
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice. Enter a menu number.");
            }
        }
    }

    private void createOrder(ArrayList<Order> orders, ArrayList<Customer> customers, ArrayList<Publication> publications) {
        LinkedHashMap<String, Object> input = takeUserInput(FIELDS, customers, publications);
        try {
            Order newOrder = new Order(input); // validates
            validateOrderRecords(newOrder, customers, publications);
            int newId = new DAO().addData(TABLE, newOrder);
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

    private void viewOrders(ArrayList<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println(ENTITY_PLURAL + " is empty");
            return;
        }
        orders.forEach(System.out::println);
    }

    private void updateOrder(ArrayList<Order> orders, ArrayList<Customer> customers, ArrayList<Publication> publications) {
        Order existing = selectOrder(orders, "update");
        if (existing == null) return;

        LinkedHashMap<String, Object> input = takeUserInput(FIELDS, customers, publications);
        input.put(ID_KEY, existing.getOrderID());
        try {
            Order updated = new Order(input); // validates
            validateOrderRecords(updated, customers, publications);
            if (new DAO().editRow(TABLE, updated, existing.getOrderID())) {
                orders.set(orders.indexOf(existing), updated);
                System.out.println("Updated: " + updated);
            } else {
                System.out.println("Update failed");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void deleteOrder(ArrayList<Order> orders, ArrayList<DeliveryDocket> deliveryDockets) {
        Order existing = selectOrder(orders, "delete");
        if (existing == null) return;

        // the database would reject the delete, so explain why first
        int id = existing.getOrderID();
        long docketCount = deliveryDockets.stream().filter(d -> d.getOrderId() == id).count();
        if (docketCount > 0) {
            System.out.println("Cannot delete " + ENTITY.toLowerCase() + " " + id + ": it has " + docketCount
                    + " delivery docket(s). Delete those first.");
            return;
        }

        if (!confirm("Are you sure you want to delete " + ENTITY.toLowerCase() + " " + id + "? (y/n): ")) {
            System.out.println("Delete cancelled.");
            return;
        }

        if (new DAO().deleteRow(TABLE, existing.getOrderID())) {
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

    private boolean confirm(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) return false;
        String answer = scanner.nextLine().trim().toLowerCase();
        return answer.equals("y") || answer.equals("yes");
    }

    private void validateOrderRecords(Order order, List<Customer> customers, List<Publication> publications) {
        if (customers.stream().noneMatch(c -> c.getCustomerId() == order.getCustomerID())) {
            throw new IllegalArgumentException("Customer ID " + order.getCustomerID() + " was not found.");
        }
        if (publications.stream().noneMatch(p -> p.getPublicationID() == order.getPublicationID())) {
            throw new IllegalArgumentException("Publication ID " + order.getPublicationID() + " was not found.");
        }
    }

    private LinkedHashMap<String, Object> takeUserInput(List<String> fields, ArrayList<Customer> customers,
            ArrayList<Publication> publications) {
        LinkedHashMap<String, Object> userInput = new LinkedHashMap<>();
        userInput.put(ID_KEY, 0);
        for (String field : fields) {
            if (field.equals("customerId")) {
                customers.forEach(System.out::println);
            } else if (field.equals("publicationId")) {
                publications.forEach(System.out::println);
            }
            System.out.print("Please enter " + field + ": ");
            userInput.put(field, scanner.nextLine().trim());
        }
        return userInput;
    }

    /** Lists items, asks for an id, and returns the match, or null (after printing why). */
    private Order selectOrder(ArrayList<Order> orders, String action) {
        viewOrders(orders);
        System.out.print("Id of the " + ENTITY.toLowerCase() + " to " + action + ": ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid id.");
            return null;
        }
        for (Order o : orders) {
            if (o.getOrderID() == id) {
                return o;
            }
        }
        System.out.println("No " + ENTITY.toLowerCase() + " with id " + id);
        return null;
    }
}
