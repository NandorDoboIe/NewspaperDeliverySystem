package Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import Database.DataProvider;

public class Order implements DataProvider {

    private final int ordersId; 
    private final int customerId;
    private int publicationId;
    private boolean[] deliveryPattern; 

    public Order(int ordersId, int customerId, int publicationId, boolean[] deliveryPattern) {
        List<String> errors = getValidationErrors(ordersId, customerId, publicationId, deliveryPattern);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Invalid order details: " + String.join(" ", errors));
        }

        this.ordersId = ordersId;
        this.customerId = customerId;
        this.publicationId = publicationId;
        this.deliveryPattern = Arrays.copyOf(deliveryPattern, 7);
    }

    // ID may be absent before insertion
    public Order(LinkedHashMap<String, Object> row) {
        this(
            optionalId(row),
            toInt(requiredField(row, "customerId"), "customerId"),
            toInt(requiredField(row, "publicationId"), "publicationId"),
            patternFromValue(requiredField(row, "pattern"))
        );
    }

    public Order() {
        this.ordersId = 0;
        this.customerId = 0;
        this.publicationId = 0;
        this.deliveryPattern = new boolean[7];
    }

    public int getOrderID() { return ordersId; }
    public int getCustomerID() { return customerId; }
    public int getPublicationID() { return publicationId; }
    public boolean[] getDeliveryPattern() { return Arrays.copyOf(deliveryPattern, 7); }

    private boolean verifyCustomerID(int id) { return id > 0; }
    private boolean verifyPublicationID(int id) { return id > 0; }

    private boolean verifyDeliveryPattern(boolean[] days) {
        if (days == null || days.length != 7) {
            return false;
        }
        for (boolean day : days) {
            if (day) {
                return true;
            }
        }
        return false;
    }

    // Checks all fields, 
    private List<String> getValidationErrors(int ordersId, int customerId,
                                             int publicationId, boolean[] days) {
        List<String> errors = new ArrayList<>();

        if (ordersId < 0) {
            errors.add("Order ID cannot be negative.");
        }
        if (!verifyCustomerID(customerId)) {
            errors.add("Customer ID must be greater than 0.");
        }
        if (!verifyPublicationID(publicationId)) {
            errors.add("Publication ID must be greater than 0.");
        }
        if (days == null) {
            errors.add("Delivery pattern is required.");
        } else if (days.length != 7) {
            errors.add("Delivery pattern must contain exactly 7 days (Monday to Sunday).");
        } else if (!verifyDeliveryPattern(days)) {
            errors.add("Select at least one delivery day.");
        }
        return errors;
    }

    private List<String> getValidationErrors() {
        return getValidationErrors(ordersId, customerId, publicationId, deliveryPattern);
    }

    private boolean verifyDetails(int ordersId, int customerId,
                                 int publicationId, boolean[] days) {
        return getValidationErrors(ordersId, customerId, publicationId, days).isEmpty();
    }

    private String getValidationMessage(int ordersId, int customerId,
                                       int publicationId, boolean[] days) {
        List<String> errors = getValidationErrors(ordersId, customerId, publicationId, days);
        return errors.isEmpty() ? "Order details are valid." : String.join(" ", errors);
    }

    private String getValidationMessage() {
        return getValidationMessage(ordersId, customerId, publicationId, deliveryPattern);
    }

    public void updateDetails(int newPublicationId, boolean[] newPattern) {
        if (!verifyDetails(ordersId, customerId, newPublicationId, newPattern)) {
            throw new IllegalArgumentException(
                "Invalid order update: "
                + getValidationMessage(ordersId, customerId, newPublicationId, newPattern));
        }
        this.publicationId = newPublicationId;
        this.deliveryPattern = Arrays.copyOf(newPattern, 7);
    }

    // 7 chars stored in MySQL; Mon/Wed/Fri = "1010100".
    public String getPatternString() {
        StringBuilder result = new StringBuilder(7);
        for (boolean day : deliveryPattern) {
            result.append(day ? '1' : '0');
        }
        return result.toString();
    }

    public static boolean[] patternFromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Delivery pattern is required.");
        }
        if (value.length() != 7) {
            throw new IllegalArgumentException("Delivery pattern must contain exactly 7 characters.");
        }
        if (!value.matches("[01]{7}")) {
            throw new IllegalArgumentException("Delivery pattern can contain only 0 and 1.");
        }
        if (!value.contains("1")) {
            throw new IllegalArgumentException("Select at least one delivery day.");
        }
        boolean[] days = new boolean[7];
        for (int i = 0; i < 7; i++) {
            days[i] = value.charAt(i) == '1';
        }
        return days;
    }

    @Override
    public String toString() {
        return "Order " + ordersId + " | Customer: " + customerId
                + " | Publication: " + publicationId
                + " | Mon-Sun: " + getPatternString();
    }

    @Override
    public LinkedHashMap<String, Object> getData() {
        if (!verifyDetails(ordersId, customerId, publicationId, deliveryPattern)) {
            throw new IllegalStateException("Cannot save order: " + getValidationMessage());
        }
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("customerId", customerId);
        data.put("publicationId", publicationId);
        data.put("pattern", getPatternString());
        return data;
    }

    private static boolean[] patternFromValue(Object value) {
        if (value instanceof boolean[]) {
            return (boolean[]) value;
        }
        if (value instanceof String) {
            return patternFromString((String) value);
        }
        throw new IllegalArgumentException("Delivery pattern must be a 7-character 0/1 string or boolean array.");
    }

    private static Object requiredField(LinkedHashMap<String, Object> row, String field) {
        if (row == null) {
            throw new IllegalArgumentException("Order data is required.");
        }
        if (!row.containsKey(field)) {
            throw new IllegalArgumentException("Missing order field: " + field);
        }
        return row.get(field);
    }

    private static int optionalId(LinkedHashMap<String, Object> row) {
        if (row == null) {
            throw new IllegalArgumentException("Order data is required.");
        }
        return row.containsKey("ordersId")
                ? toInt(row.get("ordersId"), "ordersId") : 0;
    }

    private static int toInt(Object value, String field) {
        if (value == null) {
            throw new IllegalArgumentException(field + " must be a whole number.");
        }
        if (value instanceof Number) {
            double number = ((Number) value).doubleValue();
            if (!Double.isFinite(number) || number != Math.rint(number)
                    || number < Integer.MIN_VALUE || number > Integer.MAX_VALUE) {
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
