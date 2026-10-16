package Customer_TDD;

import org.apache.commons.lang3.StringUtils;
public class Customer {

    private String name;
    private String address;
    private String contact;

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
     * @param contact Customers contact
     * @return true if customer is created, otherwise false
     */
    public boolean createCustomer(
            
            String name,
            String address,
            String contact) {
        
        if(this.verifyName(name)
        && this.verifyAddress(address)
        && this.verifyContact(contact)){
            this.name = name;
            this.address = address;
            this.contact = contact;
            return true;
        }
        return false;
    }
}
