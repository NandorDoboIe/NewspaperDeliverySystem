package CLI;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.function.Function;

import Customer.Customer;
import Publication.Publication;
import DeliveryArea.DeliveryArea;
import Database.DAO;
import Order.Order;

public class NewspaperCLI {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
    	startUp();
        scanner.close();
    }

    private static void startUp() {
    	DAO dao = new DAO();
    	
    	
    	ArrayList<Customer> customers = new ArrayList<>();
    	ArrayList<Publication> publications = new ArrayList<>();
    	ArrayList<Order> orders = new ArrayList<>();
    	ArrayList<DeliveryArea> deliveryAreas = new ArrayList<>();
    	/**ArrayList<Publication> deliveryDockets = createObjects(dao.readTable("deliveryDockets"),Publication::new);
    	ArrayList<Publication> bills = createObjects(dao.readTable("bills"),Publication::new);
    	ArrayList<Publication> deliveryAreas = createObjects(dao.readTable("deliveryAreas"),Publication::new);
    	ArrayList<Publication> users = createObjects(dao.readTable("users"),Publication::new);*/
    	
    	
    	//mainMenu(customers,publications,orders,deliveryDockets,bills,deliveryAreas,users);
    	mainMenu(customers,publications,orders,deliveryAreas);
    }
    
    private static void mainMenu(
    		ArrayList<Customer> customers,
            ArrayList<Publication> publications,
            ArrayList<Order> orders,
            ArrayList<DeliveryArea> deliveryAreas
            /**ArrayList<LinkedHashMap<String, Object>> deliveryDockets,
            ArrayList<LinkedHashMap<String, Object>> bills,
            ArrayList<LinkedHashMap<String, Object>> deliveryAreas,
            ArrayList<LinkedHashMap<String, Object>> users*/) {
    	
        CustomerCLI customerCLI = new CustomerCLI(scanner);
        PublicationCLI publicationCLI = new PublicationCLI(scanner);
        OrderCLI orderCLI = new OrderCLI(scanner);
        //DeliveryDocketCLI deliveryDocketCLI = new DeliveryDocketCLI(scanner);
        //BillCLI billCLI = new BillCLI(scanner);
        //DeliveryAreaCLI deliveryAreaCLI = new DeliveryAreaCLI(scanner);
        //UserCLI userCLI = new UserCLI(scanner);

        while (true) {
            refreshData(customers, publications, orders, deliveryAreas);
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
                    customerCLI.showMenu(customers, deliveryAreas);
                    break;
                case 2:
                    publicationCLI.showMenu(publications);
                    break;
                case 3:
                    orderCLI.showMenu(orders, customers, publications);
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
                    //publicationCLI.showLowStockAlerts(publicationData);
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
    
    private static void refreshData(
            ArrayList<Customer> customers,
            ArrayList<Publication> publications,
            ArrayList<Order> orders,
            ArrayList<DeliveryArea> deliveryAreas) {
        DAO dao = new DAO();
        customers.clear();
        customers.addAll(createObjects(dao.readTable("customers"), Customer::new));
        publications.clear();
        publications.addAll(createObjects(dao.readTable("publications"), Publication::new));
        orders.clear();
        orders.addAll(createObjects(dao.readTable("orders"), Order::new));
        deliveryAreas.clear();
        deliveryAreas.addAll(createObjects(dao.readTable("deliveryAreas"), DeliveryArea::new));
    }

    private static <T> ArrayList<T> createObjects(
            ArrayList<LinkedHashMap<String, Object>> rows,
            Function<LinkedHashMap<String, Object>, T> factory) {

        ArrayList<T> objects = new ArrayList<>();
        for (LinkedHashMap<String, Object> row : rows) {
            objects.add(factory.apply(row));
        }
        return objects;
    }
}
