package CLI;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

import Database.DAO;
import Publication.Publication;

public class PublicationCLI {

    private final Scanner scanner;

    public PublicationCLI(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showMenu(ArrayList<LinkedHashMap<String, Object>> publicationData) {
        while (true) {
            System.out.println("\n## PUBLICATIONS ##");
            System.out.println("1. Create Publication");
            System.out.println("2. View Publications");
            System.out.println("3. Update Publication");
            System.out.println("4. Delete Publication");
            System.out.println("5. Restock Publication");
            System.out.println("0. Back to Main Menu");

            switch (readChoice()) {
                case 1:
                    createPublication();
                    break;
                case 2:
                    viewPublications();
                    break;
                case 3:
                    updatePublication();
                    break;
                case 4:
                    deletePublication();
                    break;
                case 5:
                    restockPublication();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Enter a menu number.");
            }
        }
    }

    private void createPublication() {
        System.out.println("Create Publication: not connected yet.");
        DAO dao = new DAO();
        Publication newPublication = new Publication();
        boolean success = dao.addData("publications", newPublication);
        
        if(success) {
        	System.out.println("Publication uploaded to the database sucessfully");
        } else {
        	System.out.println("Insert failed");
        }
    }

    private void viewPublications() {
        System.out.println("View Publications: not connected yet.");
        DAO dao = new DAO();
        ArrayList<LinkedHashMap<String,Object>> publicationsData = dao.readTable("publications");
        for(LinkedHashMap<String,Object> row: publicationsData) {
        	System.out.println(row.get("name")+"-"+row.get("type")+"-"+row.get("cost")+"-"+row.get("stock"));
        }
    }

    private void updatePublication() {
        System.out.println("Update Publication: not connected yet.");
        DAO dao = new DAO();
        Publication updatedPublication = new Publication();
        
        boolean success = dao.editRow("publications", updatedPublication ,updatedPublication.getPublicationID());
        if(success) {
        	System.out.println("Publication updated to the database sucessfully");
        } else {
        	System.out.println("Update failed");
        }
    }

    private void deletePublication() {
        System.out.println("Delete Publication: not connected yet.");
        
        DAO dao = new DAO();
        Publication toBeDeletedPublication = new Publication();
        boolean success = dao.deleteRow("publications", toBeDeletedPublication.getPublicationID());
    }

    private void restockPublication() {
        System.out.println("Restock Publication: not connected yet.");
    }

    public void showLowStockAlerts(ArrayList<LinkedHashMap<String, Object>> publicationData) {
        System.out.println("Low Stock Alerts: not connected yet.");
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
}
