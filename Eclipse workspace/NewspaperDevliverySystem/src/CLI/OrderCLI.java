package CLI;

import java.util.Scanner;

public class OrderCLI {

    private final Scanner scanner;

    public OrderCLI(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n## ORDERS ##");
            System.out.println("1. Create Order");
            System.out.println("2. View Orders by Customer");
            System.out.println("3. Update Order");
            System.out.println("4. Delete Order");
            System.out.println("0. Back to Main Menu");

            switch (readChoice()) {
                case 1:
                    createOrder();
                    break;
                case 2:
                    viewOrdersByCustomer();
                    break;
                case 3:
                    updateOrder();
                    break;
                case 4:
                    deleteOrder();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Enter a menu number.");
            }
        }
    }

    private void createOrder() {
        System.out.println("Create Order: not connected yet.");
    }

    private void viewOrdersByCustomer() {
        System.out.println("View Orders by Customer: not connected yet.");
    }

    private void updateOrder() {
        System.out.println("Update Order: not connected yet.");
    }

    private void deleteOrder() {
        System.out.println("Delete Order: not connected yet.");
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
