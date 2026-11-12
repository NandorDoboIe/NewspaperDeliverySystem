package CLI;

import java.util.Scanner;

public class NewspaperCLI {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        mainMenu();
        scanner.close();
    }

    private static void mainMenu() {
        //CustomerCLI customerCLI = new CustomerCLI(scanner);
        PublicationCLI publicationCLI = new PublicationCLI(scanner);
        OrderCLI orderCLI = new OrderCLI(scanner);
        //DeliveryDocketCLI deliveryDocketCLI = new DeliveryDocketCLI(scanner);
        //BillCLI billCLI = new BillCLI(scanner);
        //DeliveryAreaCLI deliveryAreaCLI = new DeliveryAreaCLI(scanner);
        //UserCLI userCLI = new UserCLI(scanner);

        while (true) {
            System.out.println("\n## NEWSPAPER DELIVERY SYSTEM ##");
            System.out.println("1. Customers");
            System.out.println("2. Publications");
            System.out.println("3. Orders");
            System.out.println("4. Delivery Dockets");
            System.out.println("5. Bills");
            System.out.println("6. Delivery Areas");
            System.out.println("7. Users");
            System.out.println("8. Low Stock Alerts");
            System.out.println("0. Exit");

            switch (readChoice()) {
                case 1:
                    customerCLI.showMenu();
                    break;
                case 2:
                    publicationCLI.showMenu();
                    break;
                case 3:
                    orderCLI.showMenu();
                    break;
                case 4:
                    deliveryDocketCLI.showMenu();
                    break;
                case 5:
                    billCLI.showMenu();
                    break;
                case 6:
                    deliveryAreaCLI.showMenu();
                    break;
                case 7:
                    userCLI.showMenu();
                    break;
                case 8:
                    publicationCLI.showLowStockAlerts();
                    break;
                case 0:
                    System.out.println("Where do you think your going? There's work to do!!!");
                    return;
                default:
                    System.out.println("Invalid option. Enter a menu number.");
            }
        }
    }

    private static int readChoice() {
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
