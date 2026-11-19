package CLI;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import Customer.Customer;
import Database.DAO;
import DeliveryArea.DeliveryArea;
import DeliveryDocket.DeliveryDocket;
import Order.Order;
import Publication.Publication;

public class DeliveryDocketCLI {

    // ---- Entity configuration: change these when porting to another object ----
    private static final String TABLE = "deliveryDockets";
    private static final String ID_KEY = "deliveryDocketId";
    private static final String ENTITY = "Delivery Docket";
    private static final String ENTITY_PLURAL = "Delivery Dockets";
    private static final List<String> FIELDS = List.of("orderId", "customerId", "date", "success");
    // ----------------------------------------------------------------------------

    private final Scanner scanner;

    public DeliveryDocketCLI(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showMenu(ArrayList<DeliveryDocket> deliveryDockets, ArrayList<Order> orders,
            ArrayList<Customer> customers, ArrayList<Publication> publications,
            ArrayList<DeliveryArea> deliveryAreas) {
        while (true) {
            System.out.println("## " + ENTITY_PLURAL.toUpperCase() + " ##");
            System.out.println("1. Create " + ENTITY);
            System.out.println("2. View " + ENTITY_PLURAL);
            System.out.println("3. Update " + ENTITY);
            System.out.println("4. Delete " + ENTITY);
            System.out.println("5. Generate " + ENTITY_PLURAL + " For A Date");
            System.out.println("6. Print " + ENTITY_PLURAL + " For A Date");
            System.out.println("7. Mark " + ENTITY + " Delivered / Failed");
            System.out.println("0. Back to Main Menu");

            switch (readChoice()) {
            case 1:
                createDeliveryDocket(deliveryDockets, orders, customers);
                break;
            case 2:
                viewDocketsForDay(deliveryDockets);
                break;
            case 3:
                updateDeliveryDocket(deliveryDockets, orders, customers);
                break;
            case 4:
                deleteDeliveryDocket(deliveryDockets);
                break;
            case 5:
                generateDeliveryDockets(deliveryDockets, orders);
                break;
            case 6:
                printDeliveryDockets(deliveryDockets, orders, customers, publications, deliveryAreas);
                break;
            case 7:
                markDeliveryDocket(deliveryDockets);
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice. Enter a menu number.");
            }
        }
    }

    /** Menu for a delivery person: only printing a day's dockets and marking deliveries. */
    public void showDeliveryPersonMenu(ArrayList<DeliveryDocket> deliveryDockets, ArrayList<Order> orders,
            ArrayList<Customer> customers, ArrayList<Publication> publications,
            ArrayList<DeliveryArea> deliveryAreas) {
        while (true) {
            System.out.println("\n## DELIVERY PERSON ##");
            System.out.println("1. Print " + ENTITY_PLURAL + " For A Date");
            System.out.println("2. Mark " + ENTITY + " Delivered / Failed");
            System.out.println("0. Exit");

            switch (readChoice()) {
            case 1:
                printDeliveryDockets(deliveryDockets, orders, customers, publications, deliveryAreas);
                break;
            case 2:
                markDeliveryDocket(deliveryDockets);
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice. Enter a menu number.");
            }
        }
    }

    private void createDeliveryDocket(ArrayList<DeliveryDocket> deliveryDockets, ArrayList<Order> orders,
            ArrayList<Customer> customers) {
        LinkedHashMap<String, Object> input = takeUserInput(FIELDS, orders, customers);
        try {
            DeliveryDocket newDeliveryDocket = new DeliveryDocket(input); // validates
            validateDocketOrder(newDeliveryDocket, orders);
            int newId = new DAO().addData(TABLE, newDeliveryDocket);
            if (newId <= 0) {
                System.out.println("Insert failed");
                return;
            }
            input.put(ID_KEY, newId);
            DeliveryDocket saved = new DeliveryDocket(input);
            deliveryDockets.add(saved);
            System.out.println(ENTITY + " created and uploaded to the database successfully");
            System.out.println(saved);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void viewDeliveryDockets(ArrayList<DeliveryDocket> deliveryDockets) {
        if (deliveryDockets.isEmpty()) {
            System.out.println(ENTITY_PLURAL + " is empty");
            return;
        }
        deliveryDockets.forEach(System.out::println);
    }

    private void updateDeliveryDocket(ArrayList<DeliveryDocket> deliveryDockets, ArrayList<Order> orders,
            ArrayList<Customer> customers) {
        DeliveryDocket existing = selectDocketForDay(deliveryDockets, "update");
        if (existing == null) return;

        LinkedHashMap<String, Object> input = takeUserInput(FIELDS, orders, customers);
        input.put(ID_KEY, existing.getDeliveryDocketId());
        try {
            DeliveryDocket updated = new DeliveryDocket(input); // validates
            validateDocketOrder(updated, orders);
            if (new DAO().editRow(TABLE, updated, existing.getDeliveryDocketId())) {
                deliveryDockets.set(deliveryDockets.indexOf(existing), updated);
                System.out.println("Updated: " + updated);
            } else {
                System.out.println("Update failed");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void deleteDeliveryDocket(ArrayList<DeliveryDocket> deliveryDockets) {
        DeliveryDocket existing = selectDocketForDay(deliveryDockets, "delete");
        if (existing == null) return;

        if (!confirm("Are you sure you want to delete " + ENTITY.toLowerCase() + " "
                + existing.getDeliveryDocketId() + "? (y/n): ")) {
            System.out.println("Delete cancelled.");
            return;
        }

        if (new DAO().deleteRow(TABLE, existing.getDeliveryDocketId())) {
            deliveryDockets.remove(existing);
            System.out.println("Deleted.");
        } else {
            System.out.println("Delete failed");
        }
    }

    /** Creates a docket for every order whose pattern includes the weekday of the chosen date. */
    private void generateDeliveryDockets(ArrayList<DeliveryDocket> deliveryDockets, ArrayList<Order> orders) {
        LocalDate date = readDate("Date to generate dockets for (yyyy-MM-dd, Enter for today): ");
        if (date == null) return;

        int dayIndex = date.getDayOfWeek().getValue() - 1; // Monday = 0 ... Sunday = 6
        int created = 0;
        int skipped = 0;
        for (Order order : orders) {
            if (order.getPatternString().charAt(dayIndex) != '1') continue;

            if (findDocketForOrder(deliveryDockets, order.getOrderID(), date) != null) {
                skipped++;
                continue;
            }

            LinkedHashMap<String, Object> input = new LinkedHashMap<>();
            input.put("orderId", order.getOrderID());
            input.put("customerId", order.getCustomerID());
            input.put("date", date.toString());
            input.put("success", false);
            try {
                DeliveryDocket newDeliveryDocket = new DeliveryDocket(input); // validates
                int newId = new DAO().addData(TABLE, newDeliveryDocket);
                if (newId <= 0) {
                    System.out.println("Insert failed for Order " + order.getOrderID());
                    continue;
                }
                input.put(ID_KEY, newId);
                deliveryDockets.add(new DeliveryDocket(input));
                created++;
            } catch (IllegalArgumentException ex) {
                System.out.println("Skipping Order " + order.getOrderID() + ": " + ex.getMessage());
            }
        }

        System.out.println(created + " " + ENTITY_PLURAL.toLowerCase() + " created for " + date
                + " (" + date.getDayOfWeek() + ").");
        if (skipped > 0) System.out.println(skipped + " order(s) already had a docket for this date.");
    }

    /** Prints the day's dockets grouped by delivery area, ready to hand to a delivery person. */
    private void printDeliveryDockets(ArrayList<DeliveryDocket> deliveryDockets, ArrayList<Order> orders,
            ArrayList<Customer> customers, ArrayList<Publication> publications,
            ArrayList<DeliveryArea> deliveryAreas) {
        LocalDate date = readDate("Date to print (yyyy-MM-dd, Enter for today): ");
        if (date == null) return;

        ArrayList<DeliveryDocket> dayDockets = new ArrayList<>();
        for (DeliveryDocket d : deliveryDockets) {
            if (d.getLocalDate().equals(date)) dayDockets.add(d);
        }
        if (dayDockets.isEmpty()) {
            System.out.println("No deliveries for " + date + ". Use option 5 to generate them.");
            return;
        }

        System.out.println("\n========== DELIVERY DOCKET " + date + " (" + date.getDayOfWeek() + ") ==========");
        ArrayList<DeliveryDocket> unassigned = new ArrayList<>(dayDockets);
        for (DeliveryArea area : deliveryAreas) {
            ArrayList<DeliveryDocket> areaDockets = new ArrayList<>();
            for (DeliveryDocket d : dayDockets) {
                Customer customer = findCustomer(customers, d.getCustomerId());
                if (customer != null && customer.getDeliveryAreaId() == area.getDeliveryAreaId()) {
                    areaDockets.add(d);
                }
            }
            unassigned.removeAll(areaDockets);
            printArea("Area " + area.getDeliveryAreaId() + ": " + area.getLocation(),
                    areaDockets, orders, customers, publications);
        }
        // dockets whose customer has no known delivery area are still printed
        printArea("Unassigned area", unassigned, orders, customers, publications);
        System.out.println("\nTotal deliveries: " + dayDockets.size());
        System.out.println("=================================================================");
    }

    private void printArea(String title, ArrayList<DeliveryDocket> areaDockets, ArrayList<Order> orders,
            ArrayList<Customer> customers, ArrayList<Publication> publications) {
        if (areaDockets.isEmpty()) return;

        System.out.println("\n" + title);
        System.out.println(String.format("  %-6s %-20s %-30s %-25s %s", "Docket", "Customer", "Address",
                "Publication", "Delivered"));
        for (DeliveryDocket d : areaDockets) {
            Customer customer = findCustomer(customers, d.getCustomerId());
            Order order = findOrder(orders, d.getOrderId());
            Publication publication = order == null ? null : findPublication(publications, order.getPublicationID());
            System.out.println(String.format("  %-6d %-20s %-30s %-25s [%s]",
                    d.getDeliveryDocketId(),
                    customer == null ? "Customer " + d.getCustomerId() : customer.getName(),
                    customer == null ? "-" : customer.getAddress(),
                    publication == null ? "Publication not found" : publication.getName(),
                    d.getSuccess() ? "X" : " "));
        }
    }

    private void markDeliveryDocket(ArrayList<DeliveryDocket> deliveryDockets) {
        DeliveryDocket existing = selectDocketForDay(deliveryDockets, "mark");
        if (existing == null) return;

        existing.setSuccess(confirm("Was it delivered successfully? (y/n): "));
        if (new DAO().editRow(TABLE, existing, existing.getDeliveryDocketId())) {
            System.out.println("Updated: " + existing);
        } else {
            System.out.println("Update failed");
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

    /** Returns the entered date, today for an empty line, or null (after printing why). */
    private LocalDate readDate(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) return null;
        return parseDate(scanner.nextLine().trim());
    }

    private LocalDate parseDate(String text) {
        if (text.isEmpty()) return LocalDate.now();
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException ex) {
            System.out.println("Invalid date. Use yyyy-MM-dd.");
            return null;
        }
    }

    private boolean confirm(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) return false;
        String answer = scanner.nextLine().trim().toLowerCase();
        return answer.equals("y") || answer.equals("yes");
    }

    private LinkedHashMap<String, Object> takeUserInput(List<String> fields, ArrayList<Order> orders,
            ArrayList<Customer> customers) {
        LinkedHashMap<String, Object> userInput = new LinkedHashMap<>();
        userInput.put(ID_KEY, 0);
        for (String field : fields) {
            if (field.equals("orderId")) {
                orders.forEach(System.out::println);
            } else if (field.equals("customerId")) {
                customers.forEach(System.out::println);
            }
            if (field.equals("date")) {
                System.out.print("Please enter " + field + " (yyyy-MM-dd): ");
            } else if (field.equals("success")) {
                System.out.print("Please enter " + field + " (true/false): ");
            } else {
                System.out.print("Please enter " + field + ": ");
            }
            userInput.put(field, scanner.nextLine().trim());
        }
        return userInput;
    }

    /** Asks for a date first, then lists only that day's dockets to pick from (the full list is too long). */
    private DeliveryDocket selectDocketForDay(ArrayList<DeliveryDocket> deliveryDockets, String action) {
        ArrayList<DeliveryDocket> dayDockets = readDayDockets(deliveryDockets);
        if (dayDockets == null) return null;
        return selectDeliveryDocket(dayDockets, action);
    }

    /** Option 2 also accepts "all" to list every docket. */
    private void viewDocketsForDay(ArrayList<DeliveryDocket> deliveryDockets) {
        System.out.print("Date of the delivery (yyyy-MM-dd, Enter for today, 'all' for every docket): ");
        if (!scanner.hasNextLine()) return;
        String text = scanner.nextLine().trim();
        if (text.equalsIgnoreCase("all")) {
            viewDeliveryDockets(deliveryDockets);
            return;
        }

        LocalDate date = parseDate(text);
        if (date == null) return;
        ArrayList<DeliveryDocket> dayDockets = docketsForDay(deliveryDockets, date);
        if (dayDockets == null) return;
        viewDeliveryDockets(dayDockets);
    }

    /** Returns the dockets for the entered date, or null (after printing why). */
    private ArrayList<DeliveryDocket> readDayDockets(ArrayList<DeliveryDocket> deliveryDockets) {
        LocalDate date = readDate("Date of the delivery (yyyy-MM-dd, Enter for today): ");
        if (date == null) return null;
        return docketsForDay(deliveryDockets, date);
    }

    /** Returns the dockets for the date, or null (after printing why). */
    private ArrayList<DeliveryDocket> docketsForDay(ArrayList<DeliveryDocket> deliveryDockets, LocalDate date) {
        ArrayList<DeliveryDocket> dayDockets = new ArrayList<>();
        for (DeliveryDocket d : deliveryDockets) {
            if (d.getLocalDate().equals(date)) dayDockets.add(d);
        }
        if (dayDockets.isEmpty()) {
            System.out.println("No deliveries for " + date + ". Use option 5 to generate them.");
            return null;
        }
        return dayDockets;
    }

    /** Lists items, asks for an id, and returns the match, or null (after printing why). */
    private DeliveryDocket selectDeliveryDocket(ArrayList<DeliveryDocket> deliveryDockets, String action) {
        viewDeliveryDockets(deliveryDockets);
        System.out.print("Id of the " + ENTITY.toLowerCase() + " to " + action + ": ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid id.");
            return null;
        }
        for (DeliveryDocket d : deliveryDockets) {
            if (d.getDeliveryDocketId() == id) {
                return d;
            }
        }
        System.out.println("No " + ENTITY.toLowerCase() + " with id " + id);
        return null;
    }

    private void validateDocketOrder(DeliveryDocket docket, List<Order> orders) {
        Order order = findOrder(orders, docket.getOrderId());
        if (order == null) {
            throw new IllegalArgumentException("Order ID " + docket.getOrderId() + " was not found.");
        }
        if (order.getCustomerID() != docket.getCustomerId()) {
            throw new IllegalArgumentException("Customer ID does not match the selected order.");
        }
    }

    private static DeliveryDocket findDocketForOrder(List<DeliveryDocket> deliveryDockets, int orderId, LocalDate date) {
        for (DeliveryDocket d : deliveryDockets) {
            if (d.getOrderId() == orderId && d.getLocalDate().equals(date)) return d;
        }
        return null;
    }

    private static Order findOrder(List<Order> orders, int orderId) {
        for (Order order : orders) {
            if (order.getOrderID() == orderId) return order;
        }
        return null;
    }

    private static Customer findCustomer(List<Customer> customers, int customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId() == customerId) return customer;
        }
        return null;
    }

    private static Publication findPublication(List<Publication> publications, int publicationId) {
        for (Publication publication : publications) {
            if (publication.getPublicationID() == publicationId) return publication;
        }
        return null;
    }
}
