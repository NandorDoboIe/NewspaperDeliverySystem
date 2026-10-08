public class Order {

    private int orderID;
    private int customerID;
    private int publicationID;
    private boolean[] deliveryPattern;

    public Order(int orderID, int customerID,
                 int publicationID, boolean[] deliveryPattern) {

        this.orderID = orderID;
        this.customerID = customerID;
        this.publicationID = publicationID;
        this.deliveryPattern = deliveryPattern;
    }

    public int getOrderID() {
        return orderID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public int getPublicationID() {
        return publicationID;
    }

    public boolean[] getDeliveryPattern() {
        return deliveryPattern;
    }

    public void setPublicationID(int publicationID) {
        this.publicationID = publicationID;
    }

    public void setDeliveryPattern(boolean[] deliveryPattern) {
        this.deliveryPattern = deliveryPattern;
    }
}
