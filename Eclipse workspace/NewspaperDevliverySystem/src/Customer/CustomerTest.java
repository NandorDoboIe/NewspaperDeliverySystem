package Customer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CustomerTest {
    // TC01
    // Test Objective: Verify the customer name is accepted when 1 character long.
    // Input(s): Customer Name = "A"
    // Expected Output(s): Customer name accepted
    @Test
    void validName_TC01(){
        Customer customer = new Customer();
        assertTrue(customer.verifyName("A"));
    }

    // TC02
    // Test Objective: Verify the customer name is accepted when more 1 character long.
    // Input(s): Customer Name = "Arnold"
    // Expected Output(s): Customer name accepted
    @Test
    void validName_TC02(){
        Customer customer = new Customer();
        assertTrue(customer.verifyName("Arnold"));
    }

    // TC03
    // Test Objective: Verify the is rejected when customer name is not left empty.
    // Input(s): Customer Name = ""
    // Expected Output(s): Error message: Invalid Customer Name
    @Test
    void invalidName_TC03(){
        Customer customer = new Customer();
        assertFalse(customer.verifyName(""));
    }

    // TC04
    // Test Objective: Verify the customer address is accepted when 1 character long.
    // Input(s): Customer Address = "A"
    // Expected Output(s): Customer address accepted
    @Test
    void validAddress_TC04(){
        Customer customer = new Customer();
        assertTrue(customer.verifyAddress("A"));
    }

    // TC05
    /// Test Objective: Verify the customer address is accepted when 1 character long.
    // Input(s): Customer Address = "Athlone"
    // Expected Output(s): Customer address accepted
    @Test
    void validName_TC05(){
        Customer customer = new Customer();
        assertTrue(customer.verifyAddress("Athlone"));
    }

    // TC06
    // Test Objective: Verify the is rejected when customer address is not left empty.
    // Input(s): Customer Address = ""
    // Expected Output(s): Error message: Invalid Customer Name
    @Test
    void invalidAddress_TC06(){
        Customer customer = new Customer();
        assertFalse(customer.verifyAddress(""));
    }

    // TC07
    // Test Objective: Verify contact details is accepted when exactly 10 digits
    // Input(s): Contact Details = "0872918648"
    // Expected Output(s): Contact Details Accepted
    @Test
    void validContact_TC07(){
        Customer customer = new Customer();
        assertTrue(customer.verifyContact("0872918648"));
    }

    // TC08
    // Test Objective: Verify contact details is rejected when fewer than 10 digits
    // Input(s): Contact Details = "087291868"
    // Expected Output(s): Error message: Invalid Contact Details
    @Test
    void invalidContact_TC08(){
        Customer customer = new Customer();
        assertFalse(customer.verifyContact("087291868"));
    }

    // TC09
    // Test Objective: Verify contact details is rejected when more than 10 digits
    // Input(s): Contact Details = "0872842918648"
    // Expected Output(s): Error message: Invalid Contact Details
    @Test
    void invalidContact_TC09(){
        Customer customer = new Customer();
        assertFalse(customer.verifyContact("0872842918648"));
    }

    // TC10
    // Test Objective: Verify contact details is rejected when containing alphabetic characters
    // Input(s): Contact Details = "0872A18648"
    // Expected Output(s): Error message: Invalid Contact Details
    @Test
    void invalidContact_TC10(){
        Customer customer = new Customer();
        assertFalse(customer.verifyContact("0872A18648"));
    }

    // TC11
    // Test Objective: Verify customer account is saved to database when all details are valid
    // Input(s): 1, "Arnold", "Athlone", "0872918648"
    // Expected Output(s): Customer Account saved to database successfully
    @Test
    void validCustomer_TC11(){
        Customer customer = new Customer();
        assertTrue(customer.createCustomer(1, "Arnold","Athlone","0872918648"));
    }

    // TC12
    // Test Objective: Verify customer account creation fails with invalid first name
    // Input(s): 1, "","Athlone","0872918648"
    // Expected Output(s): Customer Account not created
    @Test
    void invalidCustomer_TC12(){
        Customer customer = new Customer();
        assertFalse(customer.createCustomer(1, "","Athlone","0872918648"));
    }

    // TC13
    // Test Objective: Verify customer account creation fails with invalid first name
    // Input(s): 1, "Arnold","","0872918648"
    // Expected Output(s): Customer Account not created
    @Test
    void invalidCustomer_TC13(){
        Customer customer = new Customer();
        assertFalse(customer.createCustomer(1, "Arnold","","0872918648"));
    }

    // TC14
    // Test Objective: Verify customer account creation fails with invalid contact details
    // Input(s): 1, "Arnold","Athlone",""
    // Expected Output(s): Customer Account not created
    @Test
    void invalidCustomer_TC14(){
        Customer customer = new Customer(); 
        assertFalse(customer.createCustomer(1, "Arnold","Athlone",""));
    }
    
    // TC19
    // Test Objective: Verify an existing customer is updated when all details are valid
    // Input(s): ID = 2, Name = "Arnold", Address = "Dublin", Contact = "0871234567"
    // Expected Output(s): Customer details updated
    @Test
    void validCustomer_TC19() {
    	Customer customer = new Customer();
    	customer.createCustomer(1, "Arnold", "Dublin", "0871234567");
    	customer.createCustomer(2, "Arnold", "Dublin", "0871234567");
    	assertTrue(customer.updateCustomer(2, "Arnold", "Dublin", "0871234567"));
    }
    
    // TC20
    // Test Objective: Verify update is rejected when the customer does not exist
    // Input(s): ID = 99 CustomerIDs=[1,2]
    // Expected Output(s): Error message: Customer does not exist
    @Test
    void invalidID_TC20() {
    	Customer customer = new Customer();
    	customer.createCustomer(1, "Arnold", "Dublin", "0871234567");
    	customer.createCustomer(2, "Arnold", "Dublin", "0871234567");
    	assertFalse(customer.updateCustomer(99, "Arnold", "Dublin", "0871234567"));
    }
    
    // TC21
    // Test Objective: Verify update is rejected when the name is empty
    // Input(s): ID = 2, Name = ""
    // Expected Output(s): Error message: Invalid Customer Name
    void invalidName_TC21() {
    	Customer customer = new Customer();
    	customer.createCustomer(1, "Arnold", "Dublin", "0871234567");
    	customer.createCustomer(2, "Arnold", "Dublin", "0871234567");
    	assertFalse(customer.updateCustomer(2, "", "Dublin", "0871234567"));
    }
    
    // TC22
    // Test Objective: Verify update is rejected when the address is empty
    // Input(s): ID = 2, Address = ""
    // Expected Output(s): Error message: Invalid Customer Address
    @Test
    void invalidAddress_TC22() {
    	Customer customer = new Customer();
    	customer.createCustomer(1, "Arnold", "Dublin", "0871234567");
    	customer.createCustomer(2, "Arnold", "Dublin", "0871234567");
    	assertFalse(customer.updateCustomer(2, "Arnold", "", "0871234567"));
    }
    
    // TC23
    // Test Objective: Verify update is rejected when the contact details are empty
    // Input(s): ID = 2, Contact = ""
    // Expected Outputs(s): Error message: Invalid Contact Details
    @Test
    void invalidContact_TC23() {
    	Customer customer = new Customer();
    	customer.createCustomer(1, "Arnold", "Dublin", "0871234567");
    	customer.createCustomer(2, "Arnold", "Dublin", "0871234567");
    	assertFalse(customer.updateCustomer(2, "Arnold", "Dublin", ""));
    }
    
    // TC24
    // Test Objective: Verify update is rejected when the contact details are fewer than 10 digits
    // Input(s): ID = 2, Contact = "087123456"
    // Expected Output(s): Error message: Invalid Contact Details
    @Test
    void invalidContact_TC24() {
    	Customer customer = new Customer();
    	customer.createCustomer(1, "Arnold", "Dublin", "0871234567");
    	customer.createCustomer(2, "Arnold", "Dublin", "0871234567");
    	assertFalse(customer.updateCustomer(2, "Arnold", "Dublin", "087123456"));
    }
    
    // TC27
    // Test Objective: Verify the correct customer is retrieved for an existing ID
    // Input(s): ID = 2 CustomerIDs=[1,2]
    // Expected Output(s): Customer with ID 2 is returned
    @Test
    void validId_TC27() {
    	Customer customer = new Customer();
    	customer.createCustomer(1, "Arnold", "Dublin", "0871234567");
    	customer.createCustomer(2, "Arnold", "Dublin", "0871234567");
    	assertEquals(2, customer.readCustomer(2).getCustomerId());
    }
    
    // TC28
    // Test Objective: Verify the customer ID, name, address and contact details are all retrieved
    // Input(s): ID = 2 (Arnold,Athlone,0872918648)
    // Expected Output(s): ID = 2, Name = "Arnold", Address = "Athlone", Contact = "0872918648" returned
    @Test
    void validOutputs_TC28() {
    	Customer customer = new Customer();
    	customer.createCustomer(2, "Arnold", "Dublin", "0871234567");
    	assertEquals(2, customer.readCustomer(2).getCustomerId());
    	assertEquals("Arnold", customer.readCustomer(2).getName());
    	assertEquals("Dublin", customer.readCustomer(2).getAddress());
    	assertEquals("0871234567", customer.readCustomer(2).getContact());
    }
    
    // TC29
    // Test Objective: Verify retrieval fails when the customer does not exist
    // Input(s): ID = 99 CustomerIDs=[1,2]
    // Expected Output(s): Error message: Customer does not exist
    @Test
    void invalidId_TC29() {
    	Customer customer = new Customer();
    	customer.createCustomer(1, "Arnold", "Dublin", "0871234567");
    	customer.createCustomer(2, "Arnold", "Dublin", "0871234567");
    	assertNull(customer.readCustomer(99));
    }
    
    // TC30
    // Test Objective: Verify retrieval fails when customer details are missing
    // Input(s): ID = 2, record with missing name/address/contact
    // Expected Output(s): Error message: Customer details missing
    @Test
    void invalidDetails_TC30() {
    	Customer customer = new Customer();
    	customer.createCustomer(2, null, null, null);
    	assertNull(customer.readCustomer(2));
    }
    
    // TC31
    // Test Objective: Verify the correct customer is selected for deletion
    // Input(s): ID = 2 CustomerIDs=[1,2]
    // Expected Output(s): Customer with ID 2 selected
    @Test
    void ValidId_TC31() {
    	Customer customer = new Customer();
    	customer.createCustomer(1, "Arnold", "Dublin", "0871234567");
    	customer.createCustomer(2, "Arnold", "Dublin", "0871234567");
    	assertTrue(customer.deleteCustomer(2));
    }
    
    // TC32
    // Test Objective: Verify a deleted customer can no longer be retrieved
    // Input(s): Delete ID 2 then retrieve ID 2
    // Expected Output(s): Error message: Customer does not exist
    @Test
    void ValidId_TC32() {
    	Customer customer = new Customer();
    	customer.createCustomer(1, "Arnold", "Dublin", "0871234567");
    	customer.createCustomer(2, "Arnold", "Dublin", "0871234567");
    	customer.deleteCustomer(2);
    	assertNull(customer.readCustomer(2));
    }
    
    // TC33
    // Test Objective: Verify deletion fails when the customer does not exist
    // Input(s): ID = 99 CustomerIDs=[1,2]
    // Expected Output(s): Error message: Customer does not exist
    @Test
    void invalidId_TC33() {
    	Customer customer = new Customer();
    	customer.createCustomer(1, "Arnold", "Dublin", "0871234567");
    	customer.createCustomer(2, "Arnold", "Dublin", "0871234567");
    	assertFalse(customer.deleteCustomer(99));
    }

    
}
