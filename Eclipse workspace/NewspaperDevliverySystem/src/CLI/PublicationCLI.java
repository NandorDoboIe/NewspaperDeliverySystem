package CLI;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import Database.DAO;
import Publication.Publication;

public class PublicationCLI {

    // ---- Entity configuration: change these when porting to another object ----
    private static final String TABLE = "publications";
    private static final String ID_KEY = "publicationsId";
    private static final String ENTITY = "Publication";
    private static final String ENTITY_PLURAL = "Publications";
    private static final List<String> FIELDS = List.of("name", "type", "cost", "stock");
    // ----------------------------------------------------------------------------

    private static final int LOW_STOCK_THRESHOLD = 5;

    private final Scanner scanner;

    public PublicationCLI(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showMenu(ArrayList<Publication> publications) {
        while (true) {
            System.out.println("## " + ENTITY_PLURAL.toUpperCase() + " ##");
            System.out.println("1. Create " + ENTITY);
            System.out.println("2. View " + ENTITY_PLURAL);
            System.out.println("3. Update " + ENTITY);
            System.out.println("4. Delete " + ENTITY);
            System.out.println("5. Restock " + ENTITY);
            System.out.println("0. Back to Main Menu");
            showLowStockAlerts(publications);

            switch (readChoice()) {
            case 1:
                createPublication(publications);
                break;
            case 2:
                viewPublications(publications);
                break;
            case 3:
                updatePublication(publications);
                break;
            case 4:
                deletePublication(publications);
                break;
            case 5:
                restockPublication(publications);
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice. Enter a menu number.");
            }
        }
    }

    private void createPublication(ArrayList<Publication> publications) {
        LinkedHashMap<String, Object> input = takeUserInput(FIELDS);
        try {
            Publication newPublication = new Publication(input); // validates
            int newId = new DAO().addData(TABLE, newPublication);
            if (newId <= 0) {
                System.out.println("Insert failed");
                return;
            }
            input.put(ID_KEY, newId);
            Publication saved = new Publication(input);
            publications.add(saved);
            System.out.println(ENTITY + " created and uploaded to the database successfully");
            System.out.println(saved);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void viewPublications(ArrayList<Publication> publications) {
        if (publications.isEmpty()) {
            System.out.println(ENTITY_PLURAL + " is empty");
            return;
        }
        publications.forEach(System.out::println);
    }

    private void updatePublication(ArrayList<Publication> publications) {
        Publication existing = selectPublication(publications, "update");
        if (existing == null) return;

        LinkedHashMap<String, Object> input = takeUserInput(FIELDS);
        input.put(ID_KEY, existing.getPublicationID());
        try {
            Publication updated = new Publication(input); // validates
            if (new DAO().editRow(TABLE, updated, existing.getPublicationID())) {
                publications.set(publications.indexOf(existing), updated);
                System.out.println("Updated: " + updated);
            } else {
                System.out.println("Update failed");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void deletePublication(ArrayList<Publication> publications) {
        Publication existing = selectPublication(publications, "delete");
        if (existing == null) return;

        if (new DAO().deleteRow(TABLE, existing.getPublicationID())) {
            publications.remove(existing);
            System.out.println("Deleted.");
        } else {
            System.out.println("Delete failed");
        }
    }

    private void restockPublication(ArrayList<Publication> publications) {
        Publication existing = selectPublication(publications, "restock");
        if (existing == null) return;

        LinkedHashMap<String, Object> input = takeUserInput(List.of("stock"));
        try {
            int stock = Integer.parseInt(input.get("stock").toString());
            existing.updateDetails(existing.getName(), existing.getType(), existing.getCost(), stock);
        } catch (IllegalArgumentException ex) { // also covers NumberFormatException
            System.out.println("Invalid stock: " + ex.getMessage());
        }
    }

    public void showLowStockAlerts(ArrayList<Publication> publications) {
        publications.stream()
                .filter(p -> p.getStock() < LOW_STOCK_THRESHOLD)
                .forEach(p -> System.out.println("Stock of " + p.getName()
                        + " (id: " + p.getPublicationID() + ") is low, please restock!"));
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
    private Publication selectPublication(ArrayList<Publication> publications, String action) {
        viewPublications(publications);
        System.out.print("Id of the " + ENTITY.toLowerCase() + " to " + action + ": ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid id.");
            return null;
        }
        for (Publication p : publications) {
            if (p.getPublicationID() == id) {
                return p;
            }
        }
        System.out.println("No " + ENTITY.toLowerCase() + " with id " + id);
        return null;
    }
}
