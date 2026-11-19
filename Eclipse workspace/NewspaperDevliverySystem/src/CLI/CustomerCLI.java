package CLI;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import Bill.Bill;
import Database.DAO;
import Customer.Customer;
import DeliveryArea.DeliveryArea;
import DeliveryDocket.DeliveryDocket;
import Order.Order;

public class CustomerCLI {

    // ---- Entity configuration: change these when porting to another object ----
    private static final String TABLE = "customers";
    private static final String ID_KEY = "customerId";
    private static final String ENTITY = "Customer";
    private static final String ENTITY_PLURAL = "Customers";
    private static final List<String> FIELDS = List.of("deliveryAreaId", "name", "address", "contact");
    // ----------------------------------------------------------------------------

    private final Scanner scanner;

    public CustomerCLI(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showMenu(ArrayList<Customer> customers, ArrayList<DeliveryArea> deliveryAreas,
            ArrayList<Order> orders, ArrayList<DeliveryDocket> deliveryDockets, ArrayList<Bill> bills) {
        while (true) {
            System.out.println("## " + ENTITY_PLURAL.toUpperCase() + " ##");
            System.out.println("1. Create " + ENTITY);
            System.out.println("2. View " + ENTITY_PLURAL);
            System.out.println("3. Update " + ENTITY);
            System.out.println("4. Delete " + ENTITY);
            System.out.println("0. Back to Main Menu");

            switch (readChoice()) {
            case 1:
                createCustomer(customers, deliveryAreas);
                break;
            case 2:
                viewCustomers(customers);
                break;
            case 3:
                updateCustomer(customers, deliveryAreas);
                break;
            case 4:
                deleteCustomer(customers, orders, deliveryDockets, bills);
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice. Enter a menu number.");
            }
        }
    }

    private void createCustomer(ArrayList<Customer> customers, ArrayList<DeliveryArea> deliveryAreas) {
        LinkedHashMap<String, Object> input = takeUserInput(FIELDS, deliveryAreas);
        try {
            Customer newCustomer = new Customer(input); // validates
            validateDeliveryArea(newCustomer, deliveryAreas);
            int newId = new DAO().addData(TABLE, newCustomer);
            if (newId <= 0) {
                System.out.println("Insert failed");
                return;
            }
            input.put(ID_KEY, newId);
            Customer saved = new Customer(input);
            customers.add(saved);
            System.out.println(ENTITY + " created and uploaded to the database successfully");
            System.out.println(saved);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void viewCustomers(ArrayList<Customer> customers) {
        if (customers.isEmpty()) {
            System.out.println(ENTITY_PLURAL + " is empty");
            return;
        }
        customers.forEach(System.out::println);
    }

    private void updateCustomer(ArrayList<Customer> customers, ArrayList<DeliveryArea> deliveryAreas) {
        Customer existing = selectCustomer(customers, "update");
        if (existing == null) return;

        LinkedHashMap<String, Object> input = takeUserInput(FIELDS, deliveryAreas);
        input.put(ID_KEY, existing.getCustomerId());
        try {
            Customer updated = new Customer(input); // validates
            validateDeliveryArea(updated, deliveryAreas);
            if (new DAO().editRow(TABLE, updated, existing.getCustomerId())) {
                customers.set(customers.indexOf(existing), updated);
                System.out.println("Updated: " + updated);
            } else {
                System.out.println("Update failed");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void deleteCustomer(ArrayList<Customer> customers, ArrayList<Order> orders,
            ArrayList<DeliveryDocket> deliveryDockets, ArrayList<Bill> bills) {
        Customer existing = selectCustomer(customers, "delete");
        if (existing == null) return;

        // the database would reject the delete, so explain why first
        int id = existing.getCustomerId();
        long orderCount = orders.stream().filter(o -> o.getCustomerID() == id).count();
        long docketCount = deliveryDockets.stream().filter(d -> d.getCustomerId() == id).count();
        long billCount = bills.stream().filter(b -> b.getCustomerID() == id).count();
        if (orderCount + docketCount + billCount > 0) {
            System.out.println("Cannot delete " + ENTITY.toLowerCase() + " " + id + ": it has " + orderCount
                    + " order(s), " + docketCount + " delivery docket(s) and " + billCount
                    + " bill(s). Delete those first.");
            return;
        }

        if (!confirm("Are you sure you want to delete " + ENTITY.toLowerCase() + " " + id + "? (y/n): ")) {
            System.out.println("Delete cancelled.");
            return;
        }

        if (new DAO().deleteRow(TABLE, existing.getCustomerId())) {
            customers.remove(existing);
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

    private void validateDeliveryArea(Customer customer, List<DeliveryArea> deliveryAreas) {
        for (DeliveryArea area : deliveryAreas) {
            if (area.getDeliveryAreaId() == customer.getDeliveryAreaId()) return;
        }
        throw new IllegalArgumentException("Delivery Area ID " + customer.getDeliveryAreaId() + " was not found.");
    }

    private LinkedHashMap<String, Object> takeUserInput(List<String> fields, ArrayList<DeliveryArea> deliveryAreas) {
        LinkedHashMap<String, Object> userInput = new LinkedHashMap<>();
        userInput.put(ID_KEY, 0);
        for (String field : fields) {
            if (field.equals("deliveryAreaId")) {
                if (deliveryAreas.isEmpty()) {
                    System.out.println("Delivery Areas is empty");
                }
                deliveryAreas.forEach(System.out::println);
            }
            System.out.print("Please enter " + field + ": ");
            userInput.put(field, scanner.nextLine().trim());
        }
        return userInput;
    }

    /** Lists items, asks for an id, and returns the match, or null (after printing why). */
    private Customer selectCustomer(ArrayList<Customer> customers, String action) {
        viewCustomers(customers);
        System.out.print("Id of the " + ENTITY.toLowerCase() + " to " + action + ": ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid id.");
            return null;
        }
        for (Customer p : customers) {
            if (p.getCustomerId() == id) {
                return p;
            }
        }
        System.out.println("No " + ENTITY.toLowerCase() + " with id " + id);
        return null;
    }
}
