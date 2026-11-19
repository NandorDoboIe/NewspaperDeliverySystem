package CLI;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.function.Function;

import Bill.Bill;
import Customer.Customer;
import Publication.Publication;
import DeliveryArea.DeliveryArea;
import DeliveryDocket.DeliveryDocket;
import Database.DAO;
import Order.Order;
import User.User;

public class NewspaperCLI {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
    	startUp();
        scanner.close();
    }

    private static void startUp() {
    	ArrayList<Customer> customers = new ArrayList<>();
    	ArrayList<Publication> publications = new ArrayList<>();
    	ArrayList<Order> orders = new ArrayList<>();
    	ArrayList<DeliveryArea> deliveryAreas = new ArrayList<>();
    	ArrayList<DeliveryDocket> deliveryDockets = new ArrayList<>();
    	ArrayList<Bill> bills = new ArrayList<>();
    	ArrayList<User> users = new ArrayList<>();
    	refreshData(customers, publications, orders, deliveryAreas, deliveryDockets, bills, users);
    	
    	User currentUser = new UserCLI(scanner).login(users);
    	if (currentUser == null) {
    		System.out.println("Login failed. Exiting.");
    		return;
    	}
    	
    	if (currentUser.isAdmin()) {
    		mainMenu(customers,publications,orders,deliveryAreas,deliveryDockets,bills,users,currentUser);
    	} else {
    		new DeliveryDocketCLI(scanner).showDeliveryPersonMenu(deliveryDockets, orders, customers, publications, deliveryAreas);
    		System.out.println("Logged out.");
    	}
    }
    
    private static void mainMenu(
    		ArrayList<Customer> customers,
            ArrayList<Publication> publications,
            ArrayList<Order> orders,
            ArrayList<DeliveryArea> deliveryAreas,
            ArrayList<DeliveryDocket> deliveryDockets,
            ArrayList<Bill> bills,
            ArrayList<User> users,
            User currentUser) {
    	
        CustomerCLI customerCLI = new CustomerCLI(scanner);
        PublicationCLI publicationCLI = new PublicationCLI(scanner);
        OrderCLI orderCLI = new OrderCLI(scanner);
        DeliveryAreaCLI deliveryAreaCLI = new DeliveryAreaCLI(scanner);
        DeliveryDocketCLI deliveryDocketCLI = new DeliveryDocketCLI(scanner);
        BillCLI billCLI = new BillCLI(scanner);
        UserCLI userCLI = new UserCLI(scanner);

        while (true) {
            refreshData(customers, publications, orders, deliveryAreas, deliveryDockets, bills, users);
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
                    customerCLI.showMenu(customers, deliveryAreas, orders, deliveryDockets, bills);
                    break;
                case 2:
                    publicationCLI.showMenu(publications, orders);
                    break;
                case 3:
                    orderCLI.showMenu(orders, customers, publications, deliveryDockets);
                    break;
                case 4:
                    deliveryDocketCLI.showMenu(deliveryDockets, orders, customers, publications, deliveryAreas);
                    break;
                case 5:
                    billCLI.showMenu(bills, customers, orders, publications, deliveryDockets);
                    break;
                case 6:
                    deliveryAreaCLI.showMenu(deliveryAreas, customers);
                    break;
                case 7:
                    userCLI.showMenu(users, currentUser);
                    break;
                case 8:
                    if (publications.stream().noneMatch(Publication::isLowStock)) {
                        System.out.println("No publications are low on stock.");
                    }
                    publicationCLI.showLowStockAlerts(publications);
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
            ArrayList<DeliveryArea> deliveryAreas,
            ArrayList<DeliveryDocket> deliveryDockets,
            ArrayList<Bill> bills,
            ArrayList<User> users) {
        DAO dao = new DAO();
        customers.clear();
        customers.addAll(createObjects(dao.readTable("customers"), Customer::new));
        publications.clear();
        publications.addAll(createObjects(dao.readTable("publications"), Publication::new));
        orders.clear();
        orders.addAll(createObjects(dao.readTable("orders"), Order::new));
        deliveryAreas.clear();
        deliveryAreas.addAll(createObjects(dao.readTable("deliveryAreas"), DeliveryArea::new));
        deliveryDockets.clear();
        deliveryDockets.addAll(createObjects(dao.readTable("deliveryDockets"), DeliveryDocket::new));
        bills.clear();
        bills.addAll(createObjects(dao.readTable("bills"), Bill::new));
        users.clear();
        users.addAll(createObjects(dao.readTable("users"), User::new));
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
