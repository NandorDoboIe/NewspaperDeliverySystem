package CLI;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

import Database.DAO;
import Publication.Publication;

public class OrderCLI {

    private final Scanner scanner;

    public OrderCLI(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showMenu(ArrayList<LinkedHashMap<String, Object>> orderData) {
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
        
        DAO dao = new DAO();
        Publication newOrder = new Publication();
        boolean success = dao.addData("order", newOrder);
        
        if(success) {
        	System.out.println("Order uploaded to the database sucessfully");
        } else {
        	System.out.println("Insert failed");
        }
    }

    private void viewOrdersByCustomer() {
        System.out.println("View Orders by Customer: not connected yet.");
        
        DAO dao = new DAO();
        ArrayList<LinkedHashMap<String,Object>> orderData = dao.readTable("orders");
        for(LinkedHashMap<String,Object> row: orderData) {
        	System.out.println(row.get("name")+"-"+row.get("type")+"-"+row.get("cost")+"-"+row.get("stock"));
        }
    }

    private void updateOrder() {
        System.out.println("Update Order: not connected yet.");
        
        DAO dao = new DAO();
        Publication updatedOrder = new Publication();
        
        boolean success = dao.editRow("orders", updatedOrder ,updatedOrder.getPublicationID());
        if(success) {
        	System.out.println("Order updated to the database sucessfully");
        } else {
        	System.out.println("Update failed");
        }
    }

    private void deleteOrder() {
        System.out.println("Delete Order: not connected yet.");
        
        DAO dao = new DAO();
        Publication toBeDeletedOrder = new Publication();
        boolean success = dao.deleteRow("orders", toBeDeletedOrder.getPublicationID());
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
