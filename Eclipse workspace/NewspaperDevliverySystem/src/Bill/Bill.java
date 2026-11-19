package Bill;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import Database.DataProvider;
import DeliveryDocket.DeliveryDocket;
import Order.Order;
import Publication.Publication;

// bill for an order, calculated successful deliveries.
public class Bill implements DataProvider {

    private final int billId;      // 0 means not yet saved to MySQL
    private final int customerId;
    private final int orderId;

    public Bill(int billId, int customerId, int orderId) {
        List<String> errors = getValidationErrors(billId, customerId, orderId);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Invalid bill details: " + String.join(" ", errors));
        }
        this.billId = billId;
        this.customerId = customerId;
        this.orderId = orderId;
    }

    // Accepts one row DAO or map from the CLI. optionalID = construction will fail because the ID is missing.
    public Bill(LinkedHashMap<String, Object> row) {
        this(
            optionalId(row),
            toInt(requiredField(row, "customerId"), "customerId"),
            toInt(requiredField(row, "orderId"), "orderId")
        );
    }

    // For testing!; an empty bill cannot be saved before data supplied.
    public Bill() {
        this.billId = 0;
        this.customerId = 0;
        this.orderId = 0;
    }

    public int getBillID() { return billId; }
    public int getCustomerID() { return customerId; }
    public int getOrderID() { return orderId; }

    //Count successful delivery dockets belonging to this customer and order,
    //* publication's cost.
    // The CLI must supply only the dockets for the BILL PERIOD charged.

    public long calculateTotal(Order order, Publication publication,
                               List<DeliveryDocket> dockets) {
        checkRelatedRecords(order, publication);
        if (dockets == null) {
            throw new IllegalArgumentException("Delivery dockets are required.");
        }

        long successfulDeliveries = 0;
        for (DeliveryDocket docket : dockets) {
            if (docket == null) {
                throw new IllegalArgumentException("Delivery docket cannot be null.");
            }
            if (docket.getCustomerId() == customerId
                    && docket.getOrderId() == orderId
                    && docket.getSuccess()) {
                successfulDeliveries++;
            }
        }
        return successfulDeliveries * (long) publication.getCost();
    }

    public String getPrintableBill(Order order, Publication publication,
                                   List<DeliveryDocket> dockets) {
        long total = calculateTotal(order, publication, dockets);
        return "========== CUSTOMER BILL ==========" + System.lineSeparator()
                + "Customer ID: " + customerId + System.lineSeparator()
                + "Order ID: " + orderId + System.lineSeparator()
                + "Total Cost: €" + total + System.lineSeparator()
                + "===================================";
    }

    public void printBill(Order order, Publication publication,
                          List<DeliveryDocket> dockets) {
        System.out.println(getPrintableBill(order, publication, dockets));
    }

    @Override
    public LinkedHashMap<String, Object> getData() {
        List<String> errors = getValidationErrors(billId, customerId, orderId);
        if (!errors.isEmpty()) {
            throw new IllegalStateException("Cannot save bill: " + String.join(" ", errors));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("customerId", customerId);
        data.put("orderId", orderId);
        return data;
    }

    @Override
    public String toString() {
        return "Bill " + billId + " | Customer ID: " + customerId + " | Order ID: " + orderId;
    }

    private void checkRelatedRecords(Order order, Publication publication) {
        List<String> errors = getValidationErrors(billId, customerId, orderId);
        if (!errors.isEmpty()) {
            throw new IllegalStateException("Cannot calculate bill: " + String.join(" ", errors));
        }
        if (order == null) {
            throw new IllegalArgumentException("Order details are required.");
        }
        if (publication == null) {
            throw new IllegalArgumentException("Publication details are required to calculate cost.");
        }
        if (order.getOrderID() != orderId) {
            throw new IllegalArgumentException("Order ID does not match this bill.");
        }
        if (order.getCustomerID() != customerId) {
            throw new IllegalArgumentException("Customer ID does not match the order.");
        }
        if (publication.getPublicationID() != order.getPublicationID()) {
            throw new IllegalArgumentException("Publication ID does not match the order.");
        }
        if (publication.getCost() <= 0) {
            throw new IllegalArgumentException("Publication cost must be greater than 0.");
        }
    }

    private List<String> getValidationErrors(int billId, int customerId, int orderId) {
        List<String> errors = new ArrayList<>();
        if (billId < 0) errors.add("Bill ID cannot be negative.");
        if (customerId <= 0) errors.add("Customer ID must be greater than 0.");
        if (orderId <= 0) errors.add("Order ID must be greater than 0.");
        return errors;
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
