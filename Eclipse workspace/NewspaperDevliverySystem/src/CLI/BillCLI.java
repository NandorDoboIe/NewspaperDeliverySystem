package CLI;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import Bill.Bill;
import Customer.Customer;
import Database.DAO;
import DeliveryDocket.DeliveryDocket;
import Order.Order;
import Publication.Publication;

/** CLI for saved bills and for calculating monthly bills from successful deliveries. */
public class BillCLI {

    private static final String TABLE = "bills";
    private static final String ID_KEY = "billId";
    private static final List<String> FIELDS = List.of("customerId", "amount", "date");

    private final Scanner scanner;

    public BillCLI(Scanner scanner) {
        this.scanner = scanner;
    }

    /** Collections contain the records loaded by DAO. */
    public void showMenu(ArrayList<Bill> bills,
                         ArrayList<Customer> customers,
                         ArrayList<Order> orders,
                         ArrayList<Publication> publications,
                         ArrayList<DeliveryDocket> deliveryDockets) {
        while (true) {
            System.out.println("\n## BILLS ##");
            System.out.println("1. Create Bill");
            System.out.println("2. View Saved Bills");
            System.out.println("3. Update Bill");
            System.out.println("4. Delete Bill");
            System.out.println("5. Calculate Monthly Bill For A Customer");
            System.out.println("6. Calculate Monthly Bills For All Customers");
            System.out.println("0. Back to Main Menu");

            switch (readChoice()) {
                case 1:
                    createBill(bills, customers);
                    break;
                case 2:
                    viewBills(bills);
                    break;
                case 3:
                    updateBill(bills, customers);
                    break;
                case 4:
                    deleteBill(bills);
                    break;
                case 5:
                    calculateCustomerBill(bills, customers, orders, publications, deliveryDockets);
                    break;
                case 6:
                    calculateAllBills(bills, customers, orders, publications, deliveryDockets);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Enter a menu number.");
            }
        }
    }

    private void createBill(ArrayList<Bill> bills, List<Customer> customers) {
        LinkedHashMap<String, Object> input = takeUserInput(FIELDS, customers);
        try {
            Bill newBill = new Bill(input);
            validateBillCustomer(newBill, customers);

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

    private void updateBill(ArrayList<Bill> bills, List<Customer> customers) {
        Bill existing = selectBill(bills, "update");
        if (existing == null) return;

        LinkedHashMap<String, Object> input = takeUserInput(FIELDS, customers);
        input.put(ID_KEY, existing.getBillID());

        try {
            Bill updated = new Bill(input);
            validateBillCustomer(updated, customers);

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

        if (!confirm("Are you sure you want to delete bill " + existing.getBillID() + "? (y/n): ")) {
            System.out.println("Delete cancelled.");
            return;
        }

        if (new DAO().deleteRow(TABLE, existing.getBillID())) {
            bills.remove(existing);
            System.out.println("Bill deleted.");
        } else {
            System.out.println("Bill delete failed.");
        }
    }

    private void calculateCustomerBill(ArrayList<Bill> bills, List<Customer> customers, List<Order> orders,
                                       List<Publication> publications, List<DeliveryDocket> deliveryDockets) {
        int customerId = readId("Enter Customer ID: ");
        if (customerId <= 0) return;

        Customer customer = findCustomer(customers, customerId);
        if (customer == null) {
            System.out.println("No customer with ID " + customerId + ".");
            return;
        }
        YearMonth month = readMonth();
        if (month == null) return;

        try {
            System.out.println(Bill.getPrintableBill(customer, month, deliveryDockets, orders, publications));
            int amount = Bill.calculateTotal(customerId, month, deliveryDockets, orders, publications);
            if (confirm("Save this bill? (y/n): ")) {
                saveMonthlyBill(bills, customerId, amount, month);
            }
        } catch (IllegalArgumentException | IllegalStateException | ArithmeticException ex) {
            System.out.println("Cannot calculate bill: " + ex.getMessage());
        }
    }

    private void calculateAllBills(ArrayList<Bill> bills, List<Customer> customers, List<Order> orders,
                                   List<Publication> publications, List<DeliveryDocket> deliveryDockets) {
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }
        YearMonth month = readMonth();
        if (month == null) return;

        LinkedHashMap<Integer, Integer> amounts = new LinkedHashMap<>();
        int skipped = 0;
        for (Customer customer : customers) {
            try {
                int amount = Bill.calculateTotal(customer.getCustomerId(), month, deliveryDockets, orders, publications);
                if (amount == 0) continue; // nothing delivered, nothing to bill
                System.out.println(Bill.getPrintableBill(customer, month, deliveryDockets, orders, publications));
                amounts.put(customer.getCustomerId(), amount);
            } catch (IllegalArgumentException | IllegalStateException | ArithmeticException ex) {
                System.out.println("Skipping Customer " + customer.getCustomerId() + ": " + ex.getMessage());
                skipped++;
            }
        }

        if (amounts.isEmpty()) {
            System.out.println("No successful deliveries in " + month + ".");
            return;
        }
        long grandTotal = 0;
        System.out.println("\n## TOTALS BY CUSTOMER " + month + " ##");
        for (Integer customerId : amounts.keySet()) {
            System.out.println("Customer " + customerId + ": \u20AC" + amounts.get(customerId));
            grandTotal += amounts.get(customerId);
        }
        System.out.println("Grand total: \u20AC" + grandTotal);
        if (skipped > 0) System.out.println(skipped + " customer(s) could not be billed.");

        if (confirm("Save all " + amounts.size() + " bills? (y/n): ")) {
            for (Integer customerId : amounts.keySet()) {
                saveMonthlyBill(bills, customerId, amounts.get(customerId), month);
            }
        }
    }

    /** Updates the customer's saved bill for that month if there is one, otherwise creates a new bill. */
    private void saveMonthlyBill(ArrayList<Bill> bills, int customerId, int amount, YearMonth month) {
        try {
            Bill existing = findBillForMonth(bills, customerId, month);
            Bill bill = new Bill(existing == null ? 0 : existing.getBillID(), customerId, amount,
                    LocalDate.now().toString());

            if (existing != null) {
                if (new DAO().editRow(TABLE, bill, existing.getBillID())) {
                    bills.set(bills.indexOf(existing), bill);
                    System.out.println("Updated existing bill for " + month + ": " + bill);
                } else {
                    System.out.println("Bill update failed.");
                }
                return;
            }

            int newId = new DAO().addData(TABLE, bill);
            if (newId <= 0) {
                System.out.println("Bill insert failed.");
                return;
            }
            Bill saved = new Bill(newId, customerId, amount, bill.getDate());
            bills.add(saved);
            System.out.println("Bill saved: " + saved);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void validateBillCustomer(Bill bill, List<Customer> customers) {
        if (findCustomer(customers, bill.getCustomerID()) == null) {
            throw new IllegalArgumentException("Customer ID " + bill.getCustomerID() + " was not found.");
        }
    }

    private LinkedHashMap<String, Object> takeUserInput(List<String> fields, List<Customer> customers) {
        LinkedHashMap<String, Object> userInput = new LinkedHashMap<>();
        userInput.put(ID_KEY, 0);
        for (String field : fields) {
            if (field.equals("customerId")) {
                customers.forEach(System.out::println);
            }
            if (field.equals("date")) {
                System.out.print("Please enter " + field + " (yyyy-MM-dd): ");
            } else {
                System.out.print("Please enter " + field + ": ");
            }
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

    private static Bill findBillForMonth(List<Bill> bills, int customerId, YearMonth month) {
        for (Bill bill : bills) {
            if (bill.getCustomerID() == customerId && YearMonth.from(bill.getLocalDate()).equals(month)) return bill;
        }
        return null;
    }

    private static Customer findCustomer(List<Customer> customers, int customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId() == customerId) return customer;
        }
        return null;
    }

    /** Returns the entered month, this month for an empty line, or null (after printing why). */
    private YearMonth readMonth() {
        System.out.print("Month to bill (yyyy-MM, Enter for this month): ");
        if (!scanner.hasNextLine()) return null;
        String text = scanner.nextLine().trim();
        if (text.isEmpty()) return YearMonth.now();
        try {
            return YearMonth.parse(text);
        } catch (DateTimeParseException ex) {
            System.out.println("Invalid month. Use yyyy-MM.");
            return null;
        }
    }

    private boolean confirm(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) return false;
        String answer = scanner.nextLine().trim().toLowerCase();
        return answer.equals("y") || answer.equals("yes");
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
