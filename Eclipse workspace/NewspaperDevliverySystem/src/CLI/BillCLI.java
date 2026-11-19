package CLI;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Bill.Bill;
import Database.DAO;
import DeliveryDocket.DeliveryDocket;
import Order.Order;
import Publication.Publication;

/** CLI for storing bill refs and printing bills from successful deliveries. */
public class BillCLI {

    private static final String TABLE = "bill";
    private static final String ID_KEY = "billId";
    private static final List<String> FIELDS = List.of("customerId", "orderId");

    private final Scanner scanner;

    public BillCLI(Scanner scanner) {
        this.scanner = scanner;
    }

    /** Collections contain the records loaded by DAO. */
    public void showMenu(ArrayList<Bill> bills,
                         ArrayList<Order> orders,
                         ArrayList<Publication> publications,
                         ArrayList<DeliveryDocket> deliveryDockets) {
        while (true) {
            System.out.println("\n## BILLS ##");
            System.out.println("1. Create Bill");
            System.out.println("2. View Saved Bills");
            System.out.println("3. Update Bill");
            System.out.println("4. Delete Bill");
            System.out.println("5. Print Individual Order Bill");
            System.out.println("6. Print Customer Total (All Their Orders)");
            System.out.println("7. Print Bills For All Orders");
            System.out.println("0. Back to Main Menu");

            switch (readChoice()) {
                case 1:
                    createBill(bills, orders);
                    break;
                case 2:
                    viewBills(bills);
                    break;
                case 3:
                    updateBill(bills, orders);
                    break;
                case 4:
                    deleteBill(bills);
                    break;
                case 5:
                    printIndividualBill(orders, publications, deliveryDockets);
                    break;
                case 6:
                    printCustomerBills(orders, publications, deliveryDockets);
                    break;
                case 7:
                    printAllBills(orders, publications, deliveryDockets);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Enter a menu number.");
            }
        }
    }

    private void createBill(ArrayList<Bill> bills, List<Order> orders) {
        LinkedHashMap<String, Object> input = takeUserInput(FIELDS);
        try {
            Bill newBill = new Bill(input);
            validateBillOrder(newBill, orders);

            if (findBillForOrder(bills, newBill.getOrderID()) != null) {
                System.out.println("A saved bill already exists for this order.");
                return;
            }

            // This follows the current CustomerCLI contract: addData returns the new ID.
            int newId = new DAO().addData(TABLE, newBill);
            if (newId <= 0) {
                System.out.println("Bill insert failed.");
                return;
            }

            input.put(ID_KEY, newId);
            Bill saved = new Bill(input);
            bills.add(saved);
            System.out.println("Bill created and uploaded successfully.");
            System.out.println(saved);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void viewBills(List<Bill> bills) {
        if (bills.isEmpty()) {
            System.out.println("No saved bills found.");
            return;
        }
        bills.forEach(System.out::println);
    }

    private void updateBill(ArrayList<Bill> bills, List<Order> orders) {
        Bill existing = selectBill(bills, "update");
        if (existing == null) return;

        LinkedHashMap<String, Object> input = takeUserInput(FIELDS);
        input.put(ID_KEY, existing.getBillID());

        try {
            Bill updated = new Bill(input);
            validateBillOrder(updated, orders);
            Bill duplicate = findBillForOrder(bills, updated.getOrderID());
            if (duplicate != null && duplicate.getBillID() != existing.getBillID()) {
                System.out.println("Another saved bill already exists for that order.");
                return;
            }

            if (new DAO().editRow(TABLE, updated, existing.getBillID())) {
                bills.set(bills.indexOf(existing), updated);
                System.out.println("Updated: " + updated);
            } else {
                System.out.println("Bill update failed.");
            }
        } catch (IllegalArgumentException | IllegalStateException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void deleteBill(ArrayList<Bill> bills) {
        Bill existing = selectBill(bills, "delete");
        if (existing == null) return;

        if (new DAO().deleteRow(TABLE, existing.getBillID())) {
            bills.remove(existing);
            System.out.println("Bill deleted.");
        } else {
            System.out.println("Bill delete failed.");
        }
    }

    private void printIndividualBill(List<Order> orders,
                                     List<Publication> publications,
                                     List<DeliveryDocket> deliveryDockets) {
        int orderId = readId("Enter Order ID: ");
        if (orderId <= 0) return;

        Order order = findOrder(orders, orderId);
        if (order == null) {
            System.out.println("No order with ID " + orderId + ". are they not buying?");
            return;
        }

        try {
            printAndCalculate(order, publications, deliveryDockets);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            System.out.println("Cannot print bill: " + ex.getMessage());
        }
    }

    private void printCustomerBills(List<Order> orders,
                                    List<Publication> publications,
                                    List<DeliveryDocket> deliveryDockets) {
        int customerId = readId("Enter Customer ID: ");
        if (customerId <= 0) return;

        long customerTotal = 0;
        int printed = 0;
        int skipped = 0;
        for (Order order : orders) {
            if (order.getCustomerID() != customerId) continue;

            try {
                customerTotal = Math.addExact(customerTotal,
                    printAndCalculate(order, publications, deliveryDockets));
                printed++;
            } catch (IllegalArgumentException | IllegalStateException | ArithmeticException ex) {
                System.out.println("Skipping Order " + order.getOrderID() + ": " + ex.getMessage());
                skipped++;
            }
        }

        if (printed == 0 && skipped == 0) {
            System.out.println("No orders found for customer " + customerId + ".");
        } else {
            System.out.println("Customer " + customerId + " total for printed orders: €" + customerTotal);
            if (skipped > 0) System.out.println(skipped + " order(s) could not be billed.");
        }
    }

    private void printAllBills(List<Order> orders,
                               List<Publication> publications,
                               List<DeliveryDocket> deliveryDockets) {
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        LinkedHashMap<Integer, Long> customerTotals = new LinkedHashMap<>();
        int skipped = 0;
        for (Order order : orders) {
            try {
                long total = printAndCalculate(order, publications, deliveryDockets);
                customerTotals.put(order.getCustomerID(),
                    Math.addExact(customerTotals.getOrDefault(order.getCustomerID(), 0L), total));
            } catch (IllegalArgumentException | IllegalStateException | ArithmeticException ex) {
                System.out.println("Skipping Order " + order.getOrderID() + ": " + ex.getMessage());
                skipped++;
            }
        }

        long grandTotal = 0;
        System.out.println("\n## TOTALS BY CUSTOMER ##");
        for (Map.Entry<Integer, Long> entry : customerTotals.entrySet()) {
            System.out.println("Customer " + entry.getKey() + ": €" + entry.getValue());
            grandTotal = Math.addExact(grandTotal, entry.getValue());
        }
        System.out.println("Grand total for printed orders: €" + grandTotal);
        if (skipped > 0) System.out.println(skipped + " order(s) could not be billed.");
    }

    private long printAndCalculate(Order order,
                                   List<Publication> publications,
                                   List<DeliveryDocket> deliveryDockets) {
        Publication publication = findPublication(publications, order.getPublicationID());
        if (publication == null) {
            throw new IllegalArgumentException(
                "Publication " + order.getPublicationID() + " was not found.");
        }
        Bill bill = new Bill(0, order.getCustomerID(), order.getOrderID());
        long total = bill.calculateTotal(order, publication, deliveryDockets);
        bill.printBill(order, publication, deliveryDockets);
        return total;
    }

    private void validateBillOrder(Bill bill, List<Order> orders) {
        Order order = findOrder(orders, bill.getOrderID());
        if (order == null) {
            throw new IllegalArgumentException("Order ID " + bill.getOrderID() + " was not found.");
        }
        if (order.getCustomerID() != bill.getCustomerID()) {
            throw new IllegalArgumentException("Customer ID does not match the selected order.");
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

    private Bill selectBill(List<Bill> bills, String action) {
        viewBills(bills);
        if (bills.isEmpty()) return null;

        int id = readId("ID of the bill to " + action + ": ");
        if (id <= 0) return null;

        for (Bill bill : bills) {
            if (bill.getBillID() == id) return bill;
        }
        System.out.println("No bill with ID " + id + ".");
        return null;
    }

    private static Bill findBillForOrder(List<Bill> bills, int orderId) {
        for (Bill bill : bills) {
            if (bill.getOrderID() == orderId) return bill;
        }
        return null;
    }

    private static Order findOrder(List<Order> orders, int orderId) {
        for (Order order : orders) {
            if (order.getOrderID() == orderId) return order;
        }
        return null;
    }

    private static Publication findPublication(List<Publication> publications, int publicationId) {
        for (Publication publication : publications) {
            if (publication.getPublicationID() == publicationId) return publication;
        }
        return null;
    }

    private int readId(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) return -1;
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            if (id > 0) return id;
        } catch (NumberFormatException ex) {
        }
        System.out.println("Invalid ID. Enter a positive whole number.");
        return -1;
    }

    private int readChoice() {
        System.out.print("Enter choice: ");
        if (!scanner.hasNextLine()) return 0;
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }
}
