package Bill;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Customer.Customer;
import Database.DataProvider;
import DeliveryDocket.DeliveryDocket;
import Order.Order;
import Publication.Publication;

// bill for one customer, amount calculated from a month of successful deliveries.
public class Bill implements DataProvider {

    private final int billId;      // 0 means not yet saved to MySQL
    private final int customerId;
    private final int amount;      // whole euros, same as Publication cost
    private final String date;     // yyyy-MM-dd, the day the bill was issued

    public Bill(int billId, int customerId, int amount, String date) {
        List<String> errors = getValidationErrors(billId, customerId, amount, date);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Invalid bill details: " + String.join(" ", errors));
        }
        this.billId = billId;
        this.customerId = customerId;
        this.amount = amount;
        this.date = date.trim();
    }

    // Accepts one row DAO or map from the CLI. optionalID = construction will fail because the ID is missing.
    public Bill(LinkedHashMap<String, Object> row) {
        this(
            optionalId(row),
            toInt(requiredField(row, "customerId"), "customerId"),
            toInt(requiredField(row, "amount"), "amount"),
            dateFromValue(requiredField(row, "date"))
        );
    }

    // For testing!; an empty bill cannot be saved before data supplied.
    public Bill() {
        this.billId = 0;
        this.customerId = 0;
        this.amount = 0;
        this.date = null;
    }

    public int getBillID() { return billId; }
    public int getCustomerID() { return customerId; }
    public int getAmount() { return amount; }
    public String getDate() { return date; }
    public LocalDate getLocalDate() { return LocalDate.parse(date); }

    // Counts the customer's successful delivery dockets in the month,
    // * the cost of the publication on each docket's order.
    // Failed deliveries and other months are never charged.
    public static int calculateTotal(int customerId, YearMonth month, List<DeliveryDocket> dockets,
                                     List<Order> orders, List<Publication> publications) {
        int total = 0;
        for (Map.Entry<Publication, Integer> line
                : countDeliveries(customerId, month, dockets, orders, publications).entrySet()) {
            total = Math.addExact(total, Math.multiplyExact(line.getKey().getCost(), line.getValue()));
        }
        return total;
    }

    public static String getPrintableBill(Customer customer, YearMonth month, List<DeliveryDocket> dockets,
                                          List<Order> orders, List<Publication> publications) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer details are required.");
        }
        LinkedHashMap<Publication, Integer> lines =
            countDeliveries(customer.getCustomerId(), month, dockets, orders, publications);

        StringBuilder bill = new StringBuilder();
        bill.append("========== CUSTOMER BILL ").append(month).append(" ==========").append(System.lineSeparator());
        bill.append("Customer ID: ").append(customer.getCustomerId()).append(System.lineSeparator());
        bill.append("Name: ").append(customer.getName()).append(System.lineSeparator());
        bill.append("Address: ").append(customer.getAddress()).append(System.lineSeparator());
        if (lines.isEmpty()) {
            bill.append("No successful deliveries this month.").append(System.lineSeparator());
        }
        for (Map.Entry<Publication, Integer> line : lines.entrySet()) {
            Publication publication = line.getKey();
            bill.append(String.format("  %-25s %3d x \u20AC%d = \u20AC%d", publication.getName(), line.getValue(),
                    publication.getCost(), publication.getCost() * line.getValue())).append(System.lineSeparator());
        }
        int failed = countFailed(customer.getCustomerId(), month, dockets);
        if (failed > 0) {
            bill.append("Failed deliveries (not charged): ").append(failed).append(System.lineSeparator());
        }
        bill.append("Total Cost: \u20AC")
            .append(calculateTotal(customer.getCustomerId(), month, dockets, orders, publications))
            .append(System.lineSeparator());
        bill.append("===========================================");
        return bill.toString();
    }

    @Override
    public LinkedHashMap<String, Object> getData() {
        List<String> errors = getValidationErrors(billId, customerId, amount, date);
        if (!errors.isEmpty()) {
            throw new IllegalStateException("Cannot save bill: " + String.join(" ", errors));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("customerId", customerId);
        data.put("amount", amount);
        data.put("date", date);
        return data;
    }

    @Override
    public String toString() {
        return "Bill " + billId + " | Customer ID: " + customerId + " | Amount: \u20AC" + amount + " | Date: " + date;
    }

    // publication -> number of successful deliveries, in the order first delivered
    private static LinkedHashMap<Publication, Integer> countDeliveries(int customerId, YearMonth month,
            List<DeliveryDocket> dockets, List<Order> orders, List<Publication> publications) {
        checkRelatedRecords(customerId, month, dockets, orders, publications);

        LinkedHashMap<Publication, Integer> lines = new LinkedHashMap<>();
        for (DeliveryDocket docket : dockets) {
            if (docket == null) {
                throw new IllegalArgumentException("Delivery docket cannot be null.");
            }
            if (docket.getCustomerId() != customerId
                    || !YearMonth.from(docket.getLocalDate()).equals(month)
                    || !docket.getSuccess()) {
                continue;
            }
            Order order = findOrder(orders, docket.getOrderId());
            if (order == null) {
                throw new IllegalArgumentException("Order " + docket.getOrderId() + " was not found.");
            }
            Publication publication = findPublication(publications, order.getPublicationID());
            if (publication == null) {
                throw new IllegalArgumentException("Publication " + order.getPublicationID() + " was not found.");
            }
            if (publication.getCost() <= 0) {
                throw new IllegalArgumentException("Publication cost must be greater than 0.");
            }
            lines.put(publication, lines.getOrDefault(publication, 0) + 1);
        }
        return lines;
    }

    private static int countFailed(int customerId, YearMonth month, List<DeliveryDocket> dockets) {
        int failed = 0;
        for (DeliveryDocket docket : dockets) {
            if (docket.getCustomerId() == customerId
                    && YearMonth.from(docket.getLocalDate()).equals(month)
                    && !docket.getSuccess()) {
                failed++;
            }
        }
        return failed;
    }

    private static void checkRelatedRecords(int customerId, YearMonth month, List<DeliveryDocket> dockets,
                                            List<Order> orders, List<Publication> publications) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("Customer ID must be greater than 0.");
        }
        if (month == null) {
            throw new IllegalArgumentException("Bill month is required.");
        }
        if (dockets == null) {
            throw new IllegalArgumentException("Delivery dockets are required.");
        }
        if (orders == null) {
            throw new IllegalArgumentException("Order details are required.");
        }
        if (publications == null) {
            throw new IllegalArgumentException("Publication details are required to calculate cost.");
        }
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

    private static List<String> getValidationErrors(int billId, int customerId, int amount, String date) {
        List<String> errors = new ArrayList<>();
        if (billId < 0) errors.add("Bill ID cannot be negative.");
        if (customerId <= 0) errors.add("Customer ID must be greater than 0.");
        if (amount < 0) errors.add("Amount cannot be negative.");
        if (!verifyDate(date)) errors.add("Date must be a real date in yyyy-MM-dd format.");
        return errors;
    }

    // only real dates in yyyy-MM-dd format
    private static boolean verifyDate(String date) {
        if (date == null) {
            return false;
        }
        try {
            LocalDate.parse(date.trim());
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    private static Object requiredField(LinkedHashMap<String, Object> row, String field) {
        if (row == null) throw new IllegalArgumentException("Bill data is required.");
        if (!row.containsKey(field)) {
            throw new IllegalArgumentException("Missing bill field: " + field);
        }
        return row.get(field);
    }
    // constuctor failed without id, optionalID: makes the Bill constructor work with new bills coming from the CLI and existing bills retrieved from MySQL, without requiring CLI to supply a temp ID.
    private static int optionalId(LinkedHashMap<String, Object> row) {
        if (row == null) throw new IllegalArgumentException("Bill data is required.");
        return row.containsKey("billId") ? toInt(row.get("billId"), "billId") : 0;
    }

    // MySQL returns a java.sql.Date, the CLI supplies a String
    private static String dateFromValue(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Date must be a real date in yyyy-MM-dd format.");
        }
        return String.valueOf(value).trim();
    }

    private static int toInt(Object value, String field) {
        if (value == null) {
            throw new IllegalArgumentException(field + " must be a whole number.");
        }
        if (value instanceof Number) {
            double n = ((Number) value).doubleValue();
            if (!Double.isFinite(n) || n != Math.rint(n)
                    || n < Integer.MIN_VALUE || n > Integer.MAX_VALUE) {
                throw new IllegalArgumentException(field + " must be a whole number within the integer range.");
            }
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value).trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(field + " must be a whole number: " + value, ex);
        }
    }
}
