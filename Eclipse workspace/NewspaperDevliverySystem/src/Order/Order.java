package Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Database.DataProvider;

public class Order implements DataProvider {

    private final Map<Integer, Order> orders = new LinkedHashMap<>();

    private int orderID;
    private int customerID;
    private int publicationID;
    private boolean[] deliveryPattern;

    public Order() {
    }

    public Order(int orderID, int customerID,
                 int publicationID, boolean[] deliveryPattern) {
        if (orderID <= 0 || customerID <= 0 || publicationID <= 0
                || !verifyDeliveryPattern(deliveryPattern)) {
            throw new IllegalArgumentException("Invalid order details");
        }
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
     * Changes the publication without changing which customer owns the order.
     */
    public void setPublicationID(int publicationID) {
        if (publicationID <= 0) {
            throw new IllegalArgumentException("Invalid publication ID");
        }
        this.publicationID = publicationID;
    }

    /**
     * Updates the delivery days (Monday to Sunday).
     */
    public void setDeliveryPattern(boolean[] deliveryPattern) {
        if (!verifyDeliveryPattern(deliveryPattern)) {
            throw new IllegalArgumentException("Invalid delivery pattern");
        }
        this.deliveryPattern = Arrays.copyOf(deliveryPattern, 7);
    }

    /**
     * Provides the order's data in the same format as Customer.getData().
     * The delivery pattern is seven digits (1 = deliver, 0 = no delivery).
     * The column names must be checked against the team's actual SQL table.
     */
    @Override
    public LinkedHashMap<String, Object> getData() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        if (orderID <= 0 || customerID <= 0 || publicationID <= 0
                || !verifyDeliveryPattern(deliveryPattern)) {
            return data;
        }
        data.put("orderId", orderID);
        data.put("customerId", customerID);
        data.put("publicationId", publicationID);
        StringBuilder days = new StringBuilder();
        for (boolean day : deliveryPattern) {
            days.append(day ? '1' : '0');
        }
        data.put("deliveryPattern", days.toString());
        return data;
    }

    @Override
    public String toString() {
        return "Order ID: " + orderID + "\nCustomer ID: " + customerID
                + "\nPublication ID: " + publicationID
                + "\nDelivery Pattern: " + getData().getOrDefault("deliveryPattern", "Not set");
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
        order.setPublicationID(publicationID);
        order.setDeliveryPattern(deliveryPattern);
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
