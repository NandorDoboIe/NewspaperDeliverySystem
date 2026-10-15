package Customer_TDD;

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
    // Input(s): "Arnold","Athlone","0872918648"
    // Expected Output(s): Customer Account saved to database successfully
    @Test
    void validCustomer_TC11(){
        Customer customer = new Customer();
        assertTrue(customer.createCustomer("Arnold","Athlone","0872918648"));
    }

    // TC12
    // Test Objective: Verify customer account creation fails with invalid first name
    // Input(s): "","Athlone","0872918648"
    // Expected Output(s): Customer Account not created
    @Test
    void invalidCustomer_TC12(){
        Customer customer = new Customer();
        assertFalse(customer.createCustomer("","Athlone","0872918648"));
    }

    // TC13
    // Test Objective: Verify customer account creation fails with invalid first name
    // Input(s): "Arnold","","0872918648"
    // Expected Output(s): Customer Account not created
    @Test
    void invalidCustomer_TC13(){
        Customer customer = new Customer();
        assertFalse(customer.createCustomer("Arnold","","0872918648"));
    }

    // TC14
    // Test Objective: Verify customer account creation fails with invalid contact details
    // Input(s): "Arnold","Athlone",""
    // Expected Output(s): Customer Account not created
    @Test
    void invalidCustomer_TC14(){
        Customer customer = new Customer(); 
        assertFalse(customer.createCustomer("Arnold","Athlone",""));
    }

}
