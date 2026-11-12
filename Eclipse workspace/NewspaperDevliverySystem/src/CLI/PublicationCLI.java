package CLI;

import java.util.Scanner;

public class PublicationCLI {

    private final Scanner scanner;

    public PublicationCLI(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showMenu() {
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
    }

    private void viewPublications() {
        System.out.println("View Publications: not connected yet.");
    }

    private void updatePublication() {
        System.out.println("Update Publication: not connected yet.");
    }

    private void deletePublication() {
        System.out.println("Delete Publication: not connected yet.");
    }

    private void restockPublication() {
        System.out.println("Restock Publication: not connected yet.");
    }

    public void showLowStockAlerts() {
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
