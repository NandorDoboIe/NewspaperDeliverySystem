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
        Customer customer = new Customer(2, "A", "Dublin", "0871234567");
        assertEquals("A", customer.getName());
    }

    // TC02
    // Test Objective: Verify the customer name is accepted when more 1 character long.
    // Input(s): Customer Name = "Arnold"
    // Expected Output(s): Customer name accepted
    @Test
    void validName_TC02(){
    	Customer customer = new Customer(2, "Arnold", "Dublin", "0871234567");
        assertEquals("Arnold", customer.getName());
    }

    // TC03
    // Test Objective: Verify the is rejected when customer name is not left empty.
    // Input(s): Customer Name = ""
    // Expected Output(s): Error message: Invalid customer details
    @Test
    void invalidName_TC03(){
    	assertThrows(IllegalArgumentException.class, () -> new Customer(2, "", "Dublin", "0871234567"));
    }

    // TC04
    // Test Objective: Verify the customer address is accepted when 1 character long.
    // Input(s): Customer Address = "A"
    // Expected Output(s): Customer address accepted
    @Test
    void validAddress_TC04(){
    	Customer customer = new Customer(2, "Arnold", "A", "0871234567");
        assertEquals("A", customer.getAddress());

    }

    // TC05
    /// Test Objective: Verify the customer address is accepted when 1 character long.
    // Input(s): Customer Address = "Athlone"
    // Expected Output(s): Customer address accepted
    @Test
    void validAddress_TC05(){
    	Customer customer = new Customer(2, "Arnold", "Athlone", "0871234567");
        assertEquals("Athlone", customer.getAddress());
    }

    // TC06
    // Test Objective: Verify the is rejected when customer address is not left empty.
    // Input(s): Customer Address = ""
    // Expected Output(s): Error message: Invalid customer details
    @Test
    void invalidAddress_TC06(){
    	assertThrows(IllegalArgumentException.class, () -> new Customer(2, "Arnold", "", "0871234567"));
    }

    // TC07
    // Test Objective: Verify contact details is accepted when exactly 10 digits
    // Input(s): Contact Details = "0872918648"
    // Expected Output(s): Contact Details Accepted
    @Test
    void validContact_TC07(){
    	Customer customer = new Customer(2, "Arnold", "Dublin", "0872918648");
        assertEquals("0872918648", customer.getContact());
    }

    // TC08
    // Test Objective: Verify contact details is rejected when fewer than 10 digits
    // Input(s): Contact Details = "087291868"
    // Expected Output(s): Error message: Invalid customer details
    @Test
    void invalidContact_TC08(){
    	assertThrows(IllegalArgumentException.class, () -> new Customer(2, "Arnold", "Dublin", "087291868"));
    }

    // TC09
    // Test Objective: Verify contact details is rejected when more than 10 digits
    // Input(s): Contact Details = "0872842918648"
    // Expected Output(s): Error message: Invalid customer details
    @Test
    void invalidContact_TC09(){
    	assertThrows(IllegalArgumentException.class, () -> new Customer(2, "Arnold", "Dublin", "0872842918648"));
    }

    // TC10
    // Test Objective: Verify contact details is rejected when containing alphabetic characters
    // Input(s): Contact Details = "0872A18648"
    // Expected Output(s): Error message: Invalid customer details
    @Test
    void invalidContact_TC10(){
    	assertThrows(IllegalArgumentException.class, () -> new Customer(2, "Arnold", "Dublin", "0872A18648"));
    }

    // TC11
    // Test Objective: Verify id is accepted when it is larger than 0
    // Input(s): Id = 1
    // Expected Output(s): Id Accepted
    @Test
    void validId_TC11(){
    	Customer customer = new Customer(1, "Arnold", "Dublin", "0872918648");
        assertEquals(1, customer.getCustomerId());
    }

    // TC12
    // Test Objective: Verify id is rejected if its 0
    // Input(s): Id = 0
    // Expected Output(s): Error message: Invalid customer details
    @Test
    void invalidId_TC12(){
        assertThrows(IllegalArgumentException.class, () -> new Customer(0, "Arnold", "Dublin", "0872918648"));
    }

    // TC13
    // Test Objective: Verify customer is updated when all details are valid
    // Input(s): "Arnold", "Dublin", "0871234567"
    // Expected Output(s): Customer details updated
    @Test
    void validCustomer_TC13(){
    	Customer customer = new Customer(2, "A", "D", "0875334767");
    	customer.updateCustomer("Arnold", "Dublin", "0871234567");
        assertEquals("Arnold", customer.getName());
        assertEquals("Dublin", customer.getAddress());
        assertEquals("0871234567", customer.getContact());
    }

    // TC14
    // Test Objective: Verify update is rejected when the name is empty
    // Input(s): Name = ""
    // Expected Output(s): Error message: Invalid customer details
    @Test
    void invalidName_TC14(){
    	Customer customer = new Customer(2, "A", "D", "0875334767");
    	assertThrows(IllegalArgumentException.class, () -> customer.updateCustomer("", "Dublin", "0871234567"));
    }
    
    // TC15
    // Test Objective: Verify update is rejected when the address is empty
    // Input(s): Address = ""
    // Expected Output(s): Error message: Invalid customer details
    @Test
    void invalidAddress_TC15() {
    	Customer customer = new Customer(2, "A", "D", "0875334767");
    	assertThrows(IllegalArgumentException.class, () -> customer.updateCustomer("Arnold", "", "0871234567"));
    }
    
    // TC16
    // Test Objective: Verify update is rejected when the contact details are empty
    // Input(s): Contact = ""
    // Expected Output(s): Error message: Invalid customer details
    @Test
    void invalidContact_TC16() {
    	Customer customer = new Customer(2, "A", "D", "0875334767");
    	assertThrows(IllegalArgumentException.class, () -> customer.updateCustomer("Arnold", "Dublin", ""));
    }
    
    // TC17
    // Test Objective: Verify update is rejected when the contact details are fewer than 10 digits
    // Input(s): Contact = "087123456"
    // Expected Output(s): Error message: Invalid customer details
    void invalidContact_TC17() {
    	Customer customer = new Customer(2, "A", "D", "0875334767");
    	assertThrows(IllegalArgumentException.class, () -> customer.updateCustomer("Arnold", "Dublin", "087123456"));
    }
    
    // TC18
    // Test Objective: Verify the correct Id is retrieved
    // Input(s): Id = 2
    // Expected Output(s): Id 2 is returned
    @Test
    void getId_TC18() {
    	Customer customer = new Customer(2, "Arnold", "Dublin", "0871234567");
    	assertEquals(2, customer.getCustomerId());
    }
    
    // TC19
    // Test Objective: Verify the correct name is retrieved
    // Input(s): name = "Arnold"
    // Expected Outputs(s): Name Arnold is returned
    @Test
    void getName_TC19() {
    	Customer customer = new Customer(2, "Arnold", "Dublin", "0871234567");
    	assertEquals("Arnold", customer.getName());
    }
    
    // TC20
    // Test Objective: Verify the correct Address is retrieved
    // Input(s): Address = "Dublin"
    // Expected Output(s): Address Dublin is returned
    @Test
    void getAddress_TC20() {
    	Customer customer = new Customer(2, "Arnold", "Dublin", "0871234567");
    	assertEquals("Dublin", customer.getAddress());
    }
    
    // TC21
    // Test Objective: Verify the correct Contact is retrieved
    // Input(s): Contact = "0871234567"
    // Expected Output(s): Contact 0871234567 is returned
    @Test
    void getContact_TC21() {
    	Customer customer = new Customer(2, "Arnold", "Dublin", "0871234567");
    	assertEquals("0871234567", customer.getContact());
    }
}