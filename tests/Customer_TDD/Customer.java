package Customer_TDD;

import org.apache.commons.lang3.StringUtils;
public class Customer {

    private String name;
    private String address;
    private int customerID;
    private String contact;
    /**
     * Verifies that a customer ID has been entered
     * and is unique
     * 
     * @param customerID Customers ID
     * @param CustomerIDs List of all the customer IDs
     * @return true if valid, otherwise false
     */
    public boolean verifyCustomerID(int customerID, int[] CustomerIDs){
        if(customerID < 1){
            return false;
        }
        for(int id : CustomerIDs){
            if(customerID == id){
                return false;
            }
        }
        return true;
    }

    /**
     * Verifies that a customer name has
     * at least one character.
     * 
     * @param name Customers name
     * @return true if valid, otherwise false
     */
    public boolean verifyName(String name){
        return !name.equals("");
    }

    /**
     * Verifies that a customer address has
     * at least one character.
     * 
     * @param address Customers address
     * @return true if valid, otherwise false
     */
    public boolean verifyAddress(String address){
        return !address.equals("");
    }

    /**
     * Verifies that a customer contact details are
     * ten characters
     * and numeric
     * @param contact Customers contact details
     * @return true if valid, otherwise false
     */
    public boolean verifyContact(String contact){
        if(contact.length() == 10){
            return StringUtils.isNumeric(contact);
        }
        return false;
    }

    /**
     * Verifies that the customer was successfully saved.
     *
     * @param saveSuccessful true if the customer was saved
     * @return true if saved, otherwise false
     */
    public boolean verifySave(boolean saveSuccessful) {
        return saveSuccessful;
    }

    /**
     * Creates an customer when all validation rules are satisfied.
     *
     * @param name Customers name
     * @param address Customers address
     * @param customerID Customers ID
     * @param contact Customers contact
     * @return true if customer is created, otherwise false
     */
    public boolean createCustomer(
            
            String name,
            String address,
            int customerID,
            String contact) {
        
        int[] placeholder = {1,2};
        if(this.verifyName(name)
        && this.verifyAddress(address)
        && this.verifyCustomerID(customerID, placeholder)
        && this.verifyContact(contact)){
            this.name = name;
            this.address = address;
            this.customerID = customerID;
            this.contact = contact;
            return true;
        }
        return false;
    }
}
