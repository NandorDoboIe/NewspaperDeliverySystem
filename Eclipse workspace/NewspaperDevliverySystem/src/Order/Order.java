import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Order {

    private final Map<Integer, Order> orders = new LinkedHashMap<>();

    private int orderID;
    private int customerID;
    private int publicationID;
    private boolean[] deliveryPattern;

    public Order() {
    }

    private Order(int orderID, int customerID,
                  int publicationID, boolean[] deliveryPattern) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.publicationID = publicationID;
        this.deliveryPattern = Arrays.copyOf(deliveryPattern, 7);
    }

    public int getOrderID() { return orderID; }
    public int getCustomerID() { return customerID; }
    public int getPublicationID() { return publicationID; }
    public boolean[] getDeliveryPattern() {
        return deliveryPattern == null ? null : Arrays.copyOf(deliveryPattern, 7);
    }


    /**
     * Validates that the order ID is valid
     * and unique.
     */
    public boolean verifyOrderID(int orderID, boolean unique) {
        return orderID > 0
                && unique;
    }

    /**
     * Validates that the customer ID is valid
     * and the customer exists.
     */
    public boolean verifyCustomerID(int customerID, boolean customerExists) {
        return customerID > 0
                && customerExists;
    }

    /**
     * Validates that the publication ID is valid
     * and the publication exists.
     */
    public boolean verifyPublicationID(int publicationID,
                                       boolean publicationExists) {
        return publicationID > 0
                && publicationExists;
    }

    /**
     * Validates that the delivery pattern contains
     * exactly seven days and at least one day is selected.
     */
    public boolean verifyDeliveryPattern(boolean[] deliveryPattern) {
        if (deliveryPattern == null || deliveryPattern.length != 7) {
            return false;
        }

        for (boolean day : deliveryPattern) {
            if (day) {
                return true;
            }
        }

        return false;
    }

    /**
     * Validates that the order was successfully saved.
     */
    public boolean verifySave(boolean saveSuccessful) {
        return saveSuccessful;
    }

    /**
     * Creates an order if all validation rules pass.
     */
    public boolean createOrder(
            int orderID,
            int customerID,
            int publicationID,
            boolean[] deliveryPattern,
            boolean uniqueOrderID,
            boolean customerExists,
            boolean publicationExists,
            boolean saveSuccessful) {

        if (!verifyOrderID(orderID, uniqueOrderID)
                || !verifyCustomerID(customerID, customerExists)
                || !verifyPublicationID(publicationID, publicationExists)
                || !verifyDeliveryPattern(deliveryPattern)
                || !verifySave(saveSuccessful)
                || orders.containsKey(orderID)) {
            return false;
        }

        orders.put(orderID, new Order(orderID, customerID,
                publicationID, deliveryPattern));
        return true;
    }

    /**
     * Reads only orders belonging to an existing customer.
     */
    public List<Order> readOrders(int customerID, boolean customerExists) {
        List<Order> results = new ArrayList<>();
        if (!verifyCustomerID(customerID, customerExists)) {
            return results;
        }
        for (Order order : orders.values()) {
            if (order.customerID == customerID) {
                results.add(order);
            }
        }
        return results;
    }

    /**
     * Reads one order, but only if it belongs to the customer.
     */
    public Order readOrder(int orderID, int customerID, boolean customerExists) {
        if (!verifyCustomerID(customerID, customerExists)) {
            return null;
        }
        Order order = orders.get(orderID);
        return order != null && order.customerID == customerID ? order : null;
    }

    /**
     * Updates publication and delivery days without changing the customer.
     */
    public boolean updateOrder(int orderID, int customerID,
                               int publicationID, boolean[] deliveryPattern,
                               boolean customerExists, boolean publicationExists,
                               boolean updateSuccessful) {
        Order order = readOrder(orderID, customerID, customerExists);
        if (order == null
                || !verifyPublicationID(publicationID, publicationExists)
                || !verifyDeliveryPattern(deliveryPattern)
                || !updateSuccessful) {
            return false;
        }
        order.publicationID = publicationID;
        order.deliveryPattern = Arrays.copyOf(deliveryPattern, 7);
        return true;
    }

    /**
     * Deletes an order only when it belongs to the entered customer.
     */
    public boolean deleteOrder(int orderID, int customerID,
                               boolean customerExists, boolean deleteSuccessful) {
        if (readOrder(orderID, customerID, customerExists) == null
                || !deleteSuccessful) {
            return false;
        }
        orders.remove(orderID);
        return true;
    }
}
