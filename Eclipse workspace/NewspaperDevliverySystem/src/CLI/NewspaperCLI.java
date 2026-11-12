package CLI;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

import Database.DAO;

public class NewspaperCLI {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
    	startUp();
        scanner.close();
    }

    private static void startUp() {
    	DAO dao = new DAO();
    	
    	ArrayList<LinkedHashMap<String,Object>> customerData = dao.readTable("customer");
    	ArrayList<LinkedHashMap<String,Object>> publicationData = dao.readTable("publications");
    	ArrayList<LinkedHashMap<String,Object>> orderData = dao.readTable("orders");
    	ArrayList<LinkedHashMap<String,Object>> deliveryDocketData = dao.readTable("deliveryDocket");
    	ArrayList<LinkedHashMap<String,Object>> BillData = dao.readTable("bill");
    	ArrayList<LinkedHashMap<String,Object>> DeliveryAreaData = dao.readTable("deliveryArea");
    	ArrayList<LinkedHashMap<String,Object>> UserData = dao.readTable("user");
    	
    	
    	
    	mainMenu(customerData,publicationData,orderData,deliveryDocketData,BillData,DeliveryAreaData,UserData);
    }
    
    private static void mainMenu(
    		ArrayList<LinkedHashMap<String, Object>> customerData,
            ArrayList<LinkedHashMap<String, Object>> publicationData,
            ArrayList<LinkedHashMap<String, Object>> orderData,
            ArrayList<LinkedHashMap<String, Object>> deliveryDocketData,
            ArrayList<LinkedHashMap<String, Object>> billData,
            ArrayList<LinkedHashMap<String, Object>> deliveryAreaData,
            ArrayList<LinkedHashMap<String, Object>> userData) {
    	
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
                    //customerCLI.showMenu(customerData);
                    break;
                case 2:
                    publicationCLI.showMenu(publicationData);
                    break;
                case 3:
                    orderCLI.showMenu(orderData);
                    break;
                case 4:
                    //deliveryDocketCLI.showMenu(deliveryDocketData);
                    break;
                case 5:
                    //billCLI.showMenu(BillData);
                    break;
                case 6:
                    //deliveryAreaCLI.showMenu(DeliveryAreaData);
                    break;
                case 7:
                    //userCLI.showMenu(UserData);
                    break;
                case 8:
                    publicationCLI.showLowStockAlerts(publicationData);
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
