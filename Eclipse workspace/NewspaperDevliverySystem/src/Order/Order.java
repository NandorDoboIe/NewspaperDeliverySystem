package Order;
import java.util.Arrays;
/** One order obj
 */
public class Order {
    private final int ordersId; 
    private final int customerId;
    private int publicationId;
    private boolean[] deliveryPattern; // Monday through Sunday
    public Order(int ordersId, int customerId, int publicationId, boolean[] deliveryPattern) {
        if (ordersId < 0 || !verifyCustomerID(customerId) ||
                !verifyPublicationID(publicationId) || !verifyDeliveryPattern(deliveryPattern)) {
            throw new IllegalArgumentException("Invalid order details");
        }
        this.ordersId = ordersId;
        this.customerId = customerId;
        this.publicationId = publicationId;
        this.deliveryPattern = Arrays.copyOf(deliveryPattern, 7);
    }
    public int getOrderID() { return ordersId; }
    public int getCustomerID() { return customerId; }
    public int getPublicationID() { return publicationId; }
    public boolean[] getDeliveryPattern() { return Arrays.copyOf(deliveryPattern, 7); }
    public boolean verifyCustomerID(int id) { return id > 0; }
    public boolean verifyPublicationID(int id) { return id > 0; }
    public boolean verifyDeliveryPattern(boolean[] days) {
        if (days == null || days.length != 7) return false;
        for (boolean day : days) if (day) return true;
        return false;
    }
    public void updateDetails(int newPublicationId, boolean[] newPattern) {
        if (!verifyPublicationID(newPublicationId) || !verifyDeliveryPattern(newPattern)) {
            throw new IllegalArgumentException("Invalid order update");
        }
        publicationId = newPublicationId;
        deliveryPattern = Arrays.copyOf(newPattern, 7);
    }
    /** 7 char SQL rep - Mon/Wed/Fri = 1010100. */
    public String getPatternString() {
        StringBuilder result = new StringBuilder(7);
        for (boolean day : deliveryPattern) result.append(day ? '1' : '0');
        return result.toString();
    }
    public static boolean[] patternFromString(String value) {
        if (value == null || !value.matches("[01]{7}") || !value.contains("1")) {
            throw new IllegalArgumentException("Stored delivery pattern must be seven 0/1 values with one delivery day");
        }
        boolean[] days = new boolean[7];
        for (int i = 0; i < 7; i++) days[i] = value.charAt(i) == '1';
        return days;
    }
    @Override
    public String toString() {
        return "Order " + ordersId + " | Customer: " + customerId +
                " | Publication: " + publicationId + " | Mon-Sun: " + getPatternString();
    }
}
