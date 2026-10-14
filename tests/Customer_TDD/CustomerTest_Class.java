package Customer_TDD;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CustomerTest_Class {
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
}
