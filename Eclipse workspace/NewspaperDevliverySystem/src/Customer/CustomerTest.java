package Customer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.LinkedHashMap;

public class CustomerTest {
    // TC01
    // Test Objective: Verify the customer name is accepted when 1 character long.
    // Input(s): Customer Name = "A"
    // Expected Output(s): Customer name accepted
    @Test
    void validName_TC01(){
        Customer customer = new Customer(2, 1, "A", "Dublin", "0871234567");
        assertEquals("A", customer.getName());
    }

    // TC02
    // Test Objective: Verify the customer name is accepted when more than 1 character long.
    // Input(s): Customer Name = "Arnold"
    // Expected Output(s): Customer name accepted
    @Test
    void validName_TC02(){
        Customer customer = new Customer(2, 1, "Arnold", "Dublin", "0871234567");
        assertEquals("Arnold", customer.getName());
    }

    // TC03
    // Test Objective: Verify the customer is rejected when the name is left empty.
    // Input(s): Customer Name = ""
    // Expected Output(s): Error message: Invalid Name
    @Test
    void invalidName_TC03(){
        assertThrows(IllegalArgumentException.class, () -> new Customer(2, 1, "", "Dublin", "0871234567"));
    }

    // TC04
    // Test Objective: Verify the customer is rejected when the name is missing.
    // Input(s): Customer Name = null
    // Expected Output(s): Error message: Invalid Name
    @Test
    void invalidName_TC04(){
        assertThrows(IllegalArgumentException.class, () -> new Customer(2, 1, null, "Dublin", "0871234567"));
    }

    // TC05
    // Test Objective: Verify the customer address is accepted when 1 character long.
    // Input(s): Customer Address = "A"
    // Expected Output(s): Customer address accepted
    @Test
    void validAddress_TC05(){
        Customer customer = new Customer(2, 1, "Arnold", "A", "0871234567");
        assertEquals("A", customer.getAddress());
    }

    // TC06
    // Test Objective: Verify the customer address is accepted when more than 1 character long.
    // Input(s): Customer Address = "Athlone"
    // Expected Output(s): Customer address accepted
    @Test
    void validAddress_TC06(){
        Customer customer = new Customer(2, 1, "Arnold", "Athlone", "0871234567");
        assertEquals("Athlone", customer.getAddress());
    }

    // TC07
    // Test Objective: Verify the customer is rejected when the address is left empty.
    // Input(s): Customer Address = ""
    // Expected Output(s): Error message: Invalid Address
    @Test
    void invalidAddress_TC07(){
        assertThrows(IllegalArgumentException.class, () -> new Customer(2, 1, "Arnold", "", "0871234567"));
    }

    // TC08
    // Test Objective: Verify the customer is rejected when the address is missing.
    // Input(s): Customer Address = null
    // Expected Output(s): Error message: Invalid Address
    @Test
    void invalidAddress_TC08(){
        assertThrows(IllegalArgumentException.class, () -> new Customer(2, 1, "Arnold", null, "0871234567"));
    }

    // TC09
    // Test Objective: Verify contact details are accepted when 10 digits
    // Input(s): Contact Details = "0872918648"
    // Expected Output(s): Contact Details Accepted
    @Test
    void validContact_TC09(){
        Customer customer = new Customer(2, 1, "Arnold", "Dublin", "0872918648");
        assertEquals("0872918648", customer.getContact());
    }

    // TC10
    // Test Objective: Verify contact details are accepted at the minimum length of 7 digits
    // Input(s): Contact Details = "1234567"
    // Expected Output(s): Contact Details Accepted
    @Test
    void validContact_TC10(){
        Customer customer = new Customer(2, 1, "Arnold", "Dublin", "1234567");
        assertEquals("1234567", customer.getContact());
    }

    // TC11
    // Test Objective: Verify contact details are accepted at the maximum length of 11 digits
    // Input(s): Contact Details = "12345678901"
    // Expected Output(s): Contact Details Accepted
    @Test
    void validContact_TC11(){
        Customer customer = new Customer(2, 1, "Arnold", "Dublin", "12345678901");
        assertEquals("12345678901", customer.getContact());
    }

    // TC12
    // Test Objective: Verify contact details are rejected when fewer than 7 digits
    // Input(s): Contact Details = "123456"
    // Expected Output(s): Error message: Invalid Contact
    @Test
    void invalidContact_TC12(){
        assertThrows(IllegalArgumentException.class, () -> new Customer(2, 1, "Arnold", "Dublin", "123456"));
    }

    // TC13
    // Test Objective: Verify contact details are rejected when more than 11 digits
    // Input(s): Contact Details = "123456789012"
    // Expected Output(s): Error message: Invalid Contact
    @Test
    void invalidContact_TC13(){
        assertThrows(IllegalArgumentException.class, () -> new Customer(2, 1, "Arnold", "Dublin", "123456789012"));
    }

    // TC14
    // Test Objective: Verify contact details are rejected when containing alphabetic characters
    // Input(s): Contact Details = "0872A18648"
    // Expected Output(s): Error message: Invalid Contact
    @Test
    void invalidContact_TC14(){
        assertThrows(IllegalArgumentException.class, () -> new Customer(2, 1, "Arnold", "Dublin", "0872A18648"));
    }

    // TC15
    // Test Objective: Verify contact details are rejected when missing
    // Input(s): Contact Details = null
    // Expected Output(s): Error message: Invalid Contact
    @Test
    void invalidContact_TC15(){
        assertThrows(IllegalArgumentException.class, () -> new Customer(2, 1, "Arnold", "Dublin", null));
    }

    // TC16
    // Test Objective: Verify id is accepted when it is larger than 0
    // Input(s): Id = 1
    // Expected Output(s): Id Accepted
    @Test
    void validId_TC16(){
        Customer customer = new Customer(1, 1, "Arnold", "Dublin", "0872918648");
        assertEquals(1, customer.getCustomerId());
    }

    // TC17
    // Test Objective: Verify id 0 is accepted for a customer not yet saved to the database
    // Input(s): Id = 0
    // Expected Output(s): Id Accepted
    @Test
    void validId_TC17(){
        Customer customer = new Customer(0, 1, "Arnold", "Dublin", "0872918648");
        assertEquals(0, customer.getCustomerId());
    }

    // TC18
    // Test Objective: Verify id is rejected if it is negative
    // Input(s): Id = -1
    // Expected Output(s): Error message: Invalid ID
    @Test
    void invalidId_TC18(){
        assertThrows(IllegalArgumentException.class, () -> new Customer(-1, 1, "Arnold", "Dublin", "0872918648"));
    }

    // TC19
    // Test Objective: Verify the delivery area id is accepted when it is larger than 0
    // Input(s): Delivery Area Id = 3
    // Expected Output(s): Delivery Area Id Accepted
    @Test
    void validDeliveryAreaId_TC19(){
        Customer customer = new Customer(2, 3, "Arnold", "Dublin", "0872918648");
        assertEquals(3, customer.getDeliveryAreaId());
    }

    // TC20
    // Test Objective: Verify the delivery area id is rejected if it is negative
    // Input(s): Delivery Area Id = -1
    // Expected Output(s): Error message: Invalid Delivery Area ID
    @Test
    void invalidDeliveryAreaId_TC20(){
        assertThrows(IllegalArgumentException.class, () -> new Customer(2, -1, "Arnold", "Dublin", "0872918648"));
    }

    // TC21
    // Test Objective: Verify customer is updated when all details are valid
    // Input(s): 2, "Arnold", "Dublin", "0871234567"
    // Expected Output(s): Customer details updated
    @Test
    void validCustomer_TC21(){
        Customer customer = new Customer(2, 1, "A", "D", "0875334767");
        customer.updateCustomer(2, "Arnold", "Dublin", "0871234567");
        assertEquals(2, customer.getDeliveryAreaId());
        assertEquals("Arnold", customer.getName());
        assertEquals("Dublin", customer.getAddress());
        assertEquals("0871234567", customer.getContact());
    }

    // TC22
    // Test Objective: Verify update is rejected when the name is empty
    // Input(s): Name = ""
    // Expected Output(s): Error message: Invalid Name
    @Test
    void invalidName_TC22(){
        Customer customer = new Customer(2, 1, "A", "D", "0875334767");
        assertThrows(IllegalArgumentException.class, () -> customer.updateCustomer(1, "", "Dublin", "0871234567"));
    }

    // TC23
    // Test Objective: Verify update is rejected when the address is empty
    // Input(s): Address = ""
    // Expected Output(s): Error message: Invalid Address
    @Test
    void invalidAddress_TC23() {
        Customer customer = new Customer(2, 1, "A", "D", "0875334767");
        assertThrows(IllegalArgumentException.class, () -> customer.updateCustomer(1, "Arnold", "", "0871234567"));
    }

    // TC24
    // Test Objective: Verify update is rejected when the contact details are empty
    // Input(s): Contact = ""
    // Expected Output(s): Error message: Invalid Contact
    @Test
    void invalidContact_TC24() {
        Customer customer = new Customer(2, 1, "A", "D", "0875334767");
        assertThrows(IllegalArgumentException.class, () -> customer.updateCustomer(1, "Arnold", "Dublin", ""));
    }

    // TC25
    // Test Objective: Verify update is rejected when the contact details are fewer than 7 digits
    // Input(s): Contact = "123456"
    // Expected Output(s): Error message: Invalid Contact
    @Test
    void invalidContact_TC25() {
        Customer customer = new Customer(2, 1, "A", "D", "0875334767");
        assertThrows(IllegalArgumentException.class, () -> customer.updateCustomer(1, "Arnold", "Dublin", "123456"));
    }

    // TC26
    // Test Objective: Verify update is rejected when the delivery area id is negative
    // Input(s): Delivery Area Id = -1
    // Expected Output(s): Error message: Invalid Delivery Area ID
    @Test
    void invalidDeliveryAreaId_TC26() {
        Customer customer = new Customer(2, 1, "A", "D", "0875334767");
        assertThrows(IllegalArgumentException.class, () -> customer.updateCustomer(-1, "Arnold", "Dublin", "0871234567"));
    }

    // TC27
    // Test Objective: Verify the correct Id is retrieved
    // Input(s): Id = 2
    // Expected Output(s): Id 2 is returned
    @Test
    void getId_TC27() {
        Customer customer = new Customer(2, 1, "Arnold", "Dublin", "0871234567");
        assertEquals(2, customer.getCustomerId());
    }

    // TC28
    // Test Objective: Verify the correct name is retrieved
    // Input(s): Name = "Arnold"
    // Expected Output(s): Name Arnold is returned
    @Test
    void getName_TC28() {
        Customer customer = new Customer(2, 1, "Arnold", "Dublin", "0871234567");
        assertEquals("Arnold", customer.getName());
    }

    // TC29
    // Test Objective: Verify the correct Address is retrieved
    // Input(s): Address = "Dublin"
    // Expected Output(s): Address Dublin is returned
    @Test
    void getAddress_TC29() {
        Customer customer = new Customer(2, 1, "Arnold", "Dublin", "0871234567");
        assertEquals("Dublin", customer.getAddress());
    }

    // TC30
    // Test Objective: Verify the correct Contact is retrieved
    // Input(s): Contact = "0871234567"
    // Expected Output(s): Contact 0871234567 is returned
    @Test
    void getContact_TC30() {
        Customer customer = new Customer(2, 1, "Arnold", "Dublin", "0871234567");
        assertEquals("0871234567", customer.getContact());
    }

    // TC31
    // Test Objective: Verify the correct Delivery Area Id is retrieved
    // Input(s): Delivery Area Id = 1
    // Expected Output(s): Delivery Area Id 1 is returned
    @Test
    void getDeliveryAreaId_TC31() {
        Customer customer = new Customer(2, 1, "Arnold", "Dublin", "0871234567");
        assertEquals(1, customer.getDeliveryAreaId());
    }

    // TC32
    // Test Objective: Verify a customer is created from a database row
    // Input(s): Row = {customerId=5, deliveryAreaId=2, name="Arnold", address="Dublin", contact="0871234567"}
    // Expected Output(s): Customer with the row's details
    @Test
    void createFromRow_TC32() {
        LinkedHashMap<String, Object> row = new LinkedHashMap<>();
        row.put("customerId", 5);
        row.put("deliveryAreaId", 2);
        row.put("name", "Arnold");
        row.put("address", "Dublin");
        row.put("contact", "0871234567");
        Customer customer = new Customer(row);
        assertEquals(5, customer.getCustomerId());
        assertEquals(2, customer.getDeliveryAreaId());
        assertEquals("Arnold", customer.getName());
        assertEquals("Dublin", customer.getAddress());
        assertEquals("0871234567", customer.getContact());
    }

    // TC33
    // Test Objective: Verify a customer is created from CLI input where the ids are typed as text
    // Input(s): Row = {customerId="0", deliveryAreaId="2", ...}
    // Expected Output(s): Customer with id 0 and delivery area 2
    @Test
    void createFromCliInput_TC33() {
        LinkedHashMap<String, Object> row = new LinkedHashMap<>();
        row.put("customerId", "0");
        row.put("deliveryAreaId", "2");
        row.put("name", "Arnold");
        row.put("address", "Dublin");
        row.put("contact", "0871234567");
        Customer customer = new Customer(row);
        assertEquals(0, customer.getCustomerId());
        assertEquals(2, customer.getDeliveryAreaId());
    }

    // TC34
    // Test Objective: Verify a row with a delivery area id that is not a number is rejected
    // Input(s): deliveryAreaId = "abc"
    // Expected Output(s): Error message: Invalid number: abc
    @Test
    void invalidRow_TC34() {
        LinkedHashMap<String, Object> row = new LinkedHashMap<>();
        row.put("customerId", 0);
        row.put("deliveryAreaId", "abc");
        row.put("name", "Arnold");
        row.put("address", "Dublin");
        row.put("contact", "0871234567");
        assertThrows(IllegalArgumentException.class, () -> new Customer(row));
    }

    // TC35
    // Test Objective: Verify getData returns the four database columns and not the id
    // Input(s): Customer(2, 1, "Arnold", "Dublin", "0871234567")
    // Expected Output(s): {deliveryAreaId=1, name=Arnold, address=Dublin, contact=0871234567}
    @Test
    void getData_TC35() {
        LinkedHashMap<String, Object> data = new Customer(2, 1, "Arnold", "Dublin", "0871234567").getData();
        assertEquals(4, data.size());
        assertEquals(1, data.get("deliveryAreaId"));
        assertEquals("Arnold", data.get("name"));
        assertEquals("Dublin", data.get("address"));
        assertEquals("0871234567", data.get("contact"));
        assertFalse(data.containsKey("customerId"));
    }

    // TC36
    // Test Objective: Verify the customer description contains its details
    // Input(s): Customer(2, 1, "Arnold", "Dublin", "0871234567")
    // Expected Output(s): Text containing the id, name, address and contact
    @Test
    void customerDisplay_TC36() {
        String text = new Customer(2, 1, "Arnold", "Dublin", "0871234567").toString();
        assertTrue(text.contains("2"));
        assertTrue(text.contains("Arnold"));
        assertTrue(text.contains("Dublin"));
        assertTrue(text.contains("0871234567"));
    }
}
