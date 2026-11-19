package CLI;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import Customer.Customer;
import Database.DAO;
import DeliveryArea.DeliveryArea;

public class DeliveryAreaCLI {

    // ---- Entity configuration: change these when porting to another object ----
    private static final String TABLE = "deliveryAreas";
    private static final String ID_KEY = "deliveryAreaId";
    private static final String ENTITY = "Delivery Area";
    private static final String ENTITY_PLURAL = "Delivery Areas";
    private static final List<String> FIELDS = List.of("location");
    // ----------------------------------------------------------------------------

    private final Scanner scanner;

    public DeliveryAreaCLI(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showMenu(ArrayList<DeliveryArea> deliveryAreas, ArrayList<Customer> customers) {
        while (true) {
            System.out.println("## " + ENTITY_PLURAL.toUpperCase() + " ##");
            System.out.println("1. Create " + ENTITY);
            System.out.println("2. View " + ENTITY_PLURAL);
            System.out.println("3. Update " + ENTITY);
            System.out.println("4. Delete " + ENTITY);
            System.out.println("0. Back to Main Menu");

            switch (readChoice()) {
            case 1:
                createDeliveryArea(deliveryAreas);
                break;
            case 2:
                viewDeliveryAreas(deliveryAreas);
                break;
            case 3:
                updateDeliveryArea(deliveryAreas);
                break;
            case 4:
                deleteDeliveryArea(deliveryAreas, customers);
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice. Enter a menu number.");
            }
        }
    }

    private void createDeliveryArea(ArrayList<DeliveryArea> deliveryAreas) {
        LinkedHashMap<String, Object> input = takeUserInput(FIELDS);
        try {
            DeliveryArea newDeliveryArea = new DeliveryArea(input); // validates
            int newId = new DAO().addData(TABLE, newDeliveryArea);
            if (newId <= 0) {
                System.out.println("Insert failed");
                return;
            }
            input.put(ID_KEY, newId);
            DeliveryArea saved = new DeliveryArea(input);
            deliveryAreas.add(saved);
            System.out.println(ENTITY + " created and uploaded to the database successfully");
            System.out.println(saved);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void viewDeliveryAreas(ArrayList<DeliveryArea> deliveryAreas) {
        if (deliveryAreas.isEmpty()) {
            System.out.println(ENTITY_PLURAL + " is empty");
            return;
        }
        deliveryAreas.forEach(System.out::println);
    }

    private void updateDeliveryArea(ArrayList<DeliveryArea> deliveryAreas) {
        DeliveryArea existing = selectDeliveryArea(deliveryAreas, "update");
        if (existing == null) return;

        LinkedHashMap<String, Object> input = takeUserInput(FIELDS);
        input.put(ID_KEY, existing.getDeliveryAreaId());
        try {
            DeliveryArea updated = new DeliveryArea(input); // validates
            if (new DAO().editRow(TABLE, updated, existing.getDeliveryAreaId())) {
                deliveryAreas.set(deliveryAreas.indexOf(existing), updated);
                System.out.println("Updated: " + updated);
            } else {
                System.out.println("Update failed");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void deleteDeliveryArea(ArrayList<DeliveryArea> deliveryAreas, ArrayList<Customer> customers) {
        DeliveryArea existing = selectDeliveryArea(deliveryAreas, "delete");
        if (existing == null) return;

        // the database would reject the delete, so explain why first
        int id = existing.getDeliveryAreaId();
        long customerCount = customers.stream().filter(c -> c.getDeliveryAreaId() == id).count();
        if (customerCount > 0) {
            System.out.println("Cannot delete " + ENTITY.toLowerCase() + " " + id + ": it has " + customerCount
                    + " customer(s). Move or delete those first.");
            return;
        }

        if (!confirm("Are you sure you want to delete " + ENTITY.toLowerCase() + " " + id + "? (y/n): ")) {
            System.out.println("Delete cancelled.");
            return;
        }

        if (new DAO().deleteRow(TABLE, existing.getDeliveryAreaId())) {
            deliveryAreas.remove(existing);
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
    private DeliveryArea selectDeliveryArea(ArrayList<DeliveryArea> deliveryAreas, String action) {
        viewDeliveryAreas(deliveryAreas);
        System.out.print("Id of the " + ENTITY.toLowerCase() + " to " + action + ": ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid id.");
            return null;
        }
        for (DeliveryArea d : deliveryAreas) {
            if (d.getDeliveryAreaId() == id) {
                return d;
            }
        }
        System.out.println("No " + ENTITY.toLowerCase() + " with id " + id);
        return null;
    }
}
