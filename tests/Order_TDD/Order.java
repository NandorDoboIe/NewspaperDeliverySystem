public class Order {

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

        return verifyOrderID(orderID, uniqueOrderID)
                && verifyCustomerID(customerID, customerExists)
                && verifyPublicationID(publicationID, publicationExists)
                && verifyDeliveryPattern(deliveryPattern)
                && verifySave(saveSuccessful);
    }
}