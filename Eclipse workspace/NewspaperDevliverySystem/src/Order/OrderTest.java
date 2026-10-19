package Order;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class OrderTest {

    // TC01
    // Test Objective: Verify order ID is accepted when valid and unique
    // Input(s): Order ID = 1001, Unique = true
    // Expected Output(s): Order ID accepted
    @Test
    void validOrderID_TC01() {
        Order order = new Order();
        assertTrue(order.verifyOrderID(1001, true));
    }

    // TC02
    // Test Objective: Verify order ID is rejected when duplicated
    // Input(s): Order ID = 1001, Unique = false
    // Expected Output(s): Error message: Duplicate Order ID
    @Test
    void invalidDuplicateOrderID_TC02() {
        Order order = new Order();
        assertFalse(order.verifyOrderID(1001, false));
    }

    // TC03
    // Test Objective: Verify order ID is rejected when empty
    // Input(s): Order ID = 0
    // Expected Output(s): Error message: Invalid Order ID
    @Test
    void invalidEmptyOrderID_TC03() {
        Order order = new Order();
        assertFalse(order.verifyOrderID(0, true));
    }

    // TC04
    // Test Objective: Verify customer ID is accepted when customer exists
    // Input(s): Customer ID = 101, Customer Exists = true
    // Expected Output(s): Customer ID accepted
    @Test
    void validCustomerID_TC04() {
        Order order = new Order();
        assertTrue(order.verifyCustomerID(101, true));
    }

    // TC05
    // Test Objective: Verify customer ID is rejected when customer does not exist
    // Input(s): Customer ID = 999, Customer Exists = false
    // Expected Output(s): Error message: Invalid Customer
    @Test
    void invalidCustomer_TC05() {
        Order order = new Order();
        assertFalse(order.verifyCustomerID(999, false));
    }

    // TC06
    // Test Objective: Verify customer ID is rejected when empty
    // Input(s): Customer ID = 0
    // Expected Output(s): Error message: Invalid Customer ID
    @Test
    void invalidEmptyCustomerID_TC06() {
        Order order = new Order();
        assertFalse(order.verifyCustomerID(0, true));
    }

    // TC07
    // Test Objective: Verify publication ID is accepted when publication exists
    // Input(s): Publication ID = 201, Publication Exists = true
    // Expected Output(s): Publication ID accepted
    @Test
    void validPublicationID_TC07() {
        Order order = new Order();
        assertTrue(order.verifyPublicationID(201, true));
    }

    // TC08
    // Test Objective: Verify publication ID is rejected when publication does not exist
    // Input(s): Publication ID = 999, Publication Exists = false
    // Expected Output(s): Error message: Invalid Publication
    @Test
    void invalidPublication_TC08() {
        Order order = new Order();
        assertFalse(order.verifyPublicationID(999, false));
    }

    // TC09
    // Test Objective: Verify publication ID is rejected when empty
    // Input(s): Publication ID = 0
    // Expected Output(s): Error message: Invalid Publication ID
    @Test
    void invalidEmptyPublicationID_TC09() {
        Order order = new Order();
        assertFalse(order.verifyPublicationID(0, true));
    }

    // TC10
    // Test Objective: Verify delivery pattern is accepted when one valid day is selected
    // Input(s): Monday = true
    // Expected Output(s): Delivery pattern accepted
    @Test
    void validDeliveryPatternOneDay_TC10() {
        Order order = new Order();
        boolean[] pattern = {true, false, false, false, false, false, false};
        assertTrue(order.verifyDeliveryPattern(pattern));
    }

    // TC11
    // Test Objective: Verify delivery pattern is accepted when multiple days are selected
    // Input(s): Monday, Wednesday and Friday = true
    // Expected Output(s): Delivery pattern accepted
    @Test
    void validDeliveryPatternMultipleDays_TC11() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, true, false, false};
        assertTrue(order.verifyDeliveryPattern(pattern));
    }

    // TC12
    // Test Objective: Verify delivery pattern is accepted when all days are selected
    // Input(s): Monday-Sunday = true
    // Expected Output(s): Delivery pattern accepted
    @Test
    void validDeliveryPatternAllDays_TC12() {
        Order order = new Order();
        boolean[] pattern = {true, true, true, true, true, true, true};
        assertTrue(order.verifyDeliveryPattern(pattern));
    }

    // TC13
    // Test Objective: Verify delivery pattern is rejected when no day is selected
    // Input(s): All delivery days = false
    // Expected Output(s): Error message: Invalid Delivery Pattern
    @Test
    void invalidEmptyDeliveryPattern_TC13() {
        Order order = new Order();
        boolean[] pattern = {false, false, false, false, false, false, false};
        assertFalse(order.verifyDeliveryPattern(pattern));
    }

    // TC14
    // Test Objective: Verify delivery pattern is rejected when it is not 7 days long
    // Input(s): Delivery pattern contains 3 values
    // Expected Output(s): Error message: Invalid Delivery Pattern
    @Test
    void invalidDeliveryPatternLength_TC14() {
        Order order = new Order();
        boolean[] pattern = {true, false, true};
        assertFalse(order.verifyDeliveryPattern(pattern));
    }

    // TC15
    // Test Objective: Verify successful database save is accepted
    // Input(s): Save Successful = true
    // Expected Output(s): Order save accepted
    @Test
    void validSave_TC15() {
        Order order = new Order();
        assertTrue(order.verifySave(true));
    }

    // TC16
    // Test Objective: Verify failed database save is rejected
    // Input(s): Save Successful = false
    // Expected Output(s): Error message: Order not saved
    @Test
    void invalidSave_TC16() {
        Order order = new Order();
        assertFalse(order.verifySave(false));
    }

    // TC17
    // Test Objective: Verify order is created when all details are valid
    // Expected Output(s): Order created successfully
    @Test
    void createValidOrder_TC17() {
        Order order = new Order();
        boolean[] pattern = {true, true, true, true, true, false, false};

        assertTrue(order.createOrder(
                1001, 101, 201, pattern,
                true, true, true, true));
    }

    // TC18
    // Test Objective: Verify order creation fails with invalid customer
    // Input(s): Customer Exists = false
    // Expected Output(s): Order not created
    @Test
    void createOrderInvalidCustomer_TC18() {
        Order order = new Order();
        boolean[] pattern = {true, true, true, true, true, false, false};

        assertFalse(order.createOrder(
                1001, 999, 201, pattern,
                true, false, true, true));
    }

    // TC19
    // Test Objective: Verify order creation fails with invalid publication
    // Input(s): Publication Exists = false
    // Expected Output(s): Order not created
    @Test
    void createOrderInvalidPublication_TC19() {
        Order order = new Order();
        boolean[] pattern = {true, true, true, true, true, false, false};

        assertFalse(order.createOrder(
                1001, 101, 999, pattern,
                true, true, false, true));
    }

    // TC20
    // Test Objective: Verify order creation fails with invalid delivery pattern
    // Input(s): No delivery days selected
    // Expected Output(s): Order not created
    @Test
    void createOrderInvalidDeliveryPattern_TC20() {
        Order order = new Order();
        boolean[] pattern = {false, false, false, false, false, false, false};

        assertFalse(order.createOrder(
                1001, 101, 201, pattern,
                true, true, true, true));
    }

    // TC21
    // Test Objective: Verify order creation fails with duplicate order ID
    // Input(s): Unique Order ID = false
    // Expected Output(s): Order not created
    @Test
    void createOrderDuplicateOrderID_TC21() {
        Order order = new Order();
        boolean[] pattern = {true, true, true, true, true, false, false};

        assertFalse(order.createOrder(
                1001, 101, 201, pattern,
                false, true, true, true));
    }

    // TC22
    // Test Objective: Verify orders can be retrieved for an existing customer
    // Input(s): Customer ID = 101
    // Expected Output(s): Customer orders retrieved
    @Test
    void read_orderscanberetrievedforanexistingcustomer_TC22() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertEquals(1, order.readOrders(101, true).size());
    }

    // TC23
    // Test Objective: Verify correct order ID is retrieved
    // Input(s): Order ID = 1001, Customer ID = 101
    // Expected Output(s): Correct order ID retrieved
    @Test
    void read_correctorderIDisretrieved_TC23() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertEquals(1001, order.readOrders(101, true).get(0).getOrderID());
    }

    // TC24
    // Test Objective: Verify correct publication ID is retrieved
    // Input(s): Publication ID = 201
    // Expected Output(s): Correct publication ID retrieved
    @Test
    void read_correctpublicationIDisretrieved_TC24() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertEquals(201, order.readOrders(101, true).get(0).getPublicationID());
    }

    // TC25
    // Test Objective: Verify correct customer ID is retrieved
    // Input(s): Customer ID = 101
    // Expected Output(s): Correct customer ID retrieved
    @Test
    void read_correctcustomerIDisretrieved_TC25() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertEquals(101, order.readOrders(101, true).get(0).getCustomerID());
    }

    // TC26
    // Test Objective: Verify correct delivery pattern is retrieved
    // Input(s): Monday and Wednesday selected
    // Expected Output(s): Correct delivery pattern retrieved
    @Test
    void read_correctdeliverypatternisretrieved_TC26() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertArrayEquals(pattern, order.readOrders(101, true).get(0).getDeliveryPattern());
    }

    // TC27
    // Test Objective: Verify only the selected customer orders are retrieved
    // Input(s): Customer IDs = 101 and 102
    // Expected Output(s): Only customer 101 order is shown
    @Test
    void read_onlytheselectedcustomerordersareretrieved_TC27() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertTrue(order.createOrder(1002, 102, 202, pattern, true, true, true, true));
        assertEquals(1, order.readOrders(101, true).size());
        assertEquals(1001, order.readOrders(101, true).get(0).getOrderID());
    }

    // TC28
    // Test Objective: Verify invalid customer cannot retrieve orders
    // Input(s): Customer Exists = false
    // Expected Output(s): No orders retrieved
    @Test
    void read_invalidcustomercannotretrieveorders_TC28() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertTrue(order.readOrders(101, false).isEmpty());
    }

    // TC29
    // Test Objective: Verify a customer with no orders retrieves none
    // Input(s): Customer ID = 103, no orders
    // Expected Output(s): No orders retrieved
    @Test
    void read_acustomerwithnoordersretrievesnone_TC29() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.readOrders(103, true).isEmpty());
    }

    // TC30
    // Test Objective: Verify an order ID that does not exist is rejected
    // Input(s): Order ID = 9999
    // Expected Output(s): Order not retrieved
    @Test
    void read_anorderIDthatdoesnotexistisrejected_TC30() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertNull(order.readOrder(9999, 101, true));
    }

    // TC31
    // Test Objective: Verify missing order information is not returned
    // Input(s): Invalid delivery pattern
    // Expected Output(s): Invalid order not created or retrieved
    @Test
    void read_missingorderinformationisnotreturned_TC31() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        boolean[] invalid = {false, false, false, false, false, false, false};
        assertFalse(order.createOrder(1001, 101, 201, invalid, true, true, true, true));
        assertTrue(order.readOrders(101, true).isEmpty());
    }

    // TC32
    // Test Objective: Verify existing customer can update an order
    // Input(s): Customer ID = 101
    // Expected Output(s): Order update successful
    @Test
    void update_existingcustomercanupdateanorder_TC32() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertTrue(order.updateOrder(1001, 101, 202, pattern, true, true, true));
    }

    // TC33
    // Test Objective: Verify an existing order can be updated
    // Input(s): Order ID = 1001
    // Expected Output(s): Order update successful
    @Test
    void update_anexistingordercanbeupdated_TC33() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertTrue(order.updateOrder(1001, 101, 202, pattern, true, true, true));
    }

    // TC34
    // Test Objective: Verify existing publication is accepted
    // Input(s): Publication ID = 202
    // Expected Output(s): Publication updated
    @Test
    void update_existingpublicationisaccepted_TC34() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertTrue(order.updateOrder(1001, 101, 202, pattern, true, true, true));
        assertEquals(202, order.readOrder(1001, 101, true).getPublicationID());
    }

    // TC35
    // Test Objective: Verify empty publication ID is rejected
    // Input(s): Publication ID = 0
    // Expected Output(s): Order not updated
    @Test
    void update_emptypublicationIDisrejected_TC35() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertFalse(order.updateOrder(1001, 101, 0, pattern, true, true, true));
    }

    // TC36
    // Test Objective: Verify valid delivery pattern is accepted
    // Input(s): New delivery pattern = Tuesday only
    // Expected Output(s): Delivery pattern updated
    @Test
    void update_validdeliverypatternisaccepted_TC36() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        boolean[] changed = {false, true, false, false, false, false, false};
        assertTrue(order.updateOrder(1001, 101, 201, changed, true, true, true));
        assertArrayEquals(changed, order.readOrder(1001, 101, true).getDeliveryPattern());
    }

    // TC37
    // Test Objective: Verify order remains associated with original customer
    // Input(s): Customer ID = 101
    // Expected Output(s): Customer ID remains 101
    @Test
    void update_orderremainsassociatedwithoriginalcustomer_TC37() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertTrue(order.updateOrder(1001, 101, 202, pattern, true, true, true));
        assertEquals(101, order.readOrder(1001, 101, true).getCustomerID());
    }

    // TC38
    // Test Objective: Verify invalid customer cannot update an order
    // Input(s): Customer Exists = false
    // Expected Output(s): Order not updated
    @Test
    void update_invalidcustomercannotupdateanorder_TC38() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertFalse(order.updateOrder(1001, 101, 202, pattern, false, true, true));
    }

    // TC39
    // Test Objective: Verify missing order cannot be updated
    // Input(s): Order ID = 9999
    // Expected Output(s): Order not updated
    @Test
    void update_missingordercannotbeupdated_TC39() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertFalse(order.updateOrder(9999, 101, 202, pattern, true, true, true));
    }

    // TC40
    // Test Objective: Verify missing publication cannot update an order
    // Input(s): Publication Exists = false
    // Expected Output(s): Order not updated
    @Test
    void update_missingpublicationcannotupdateanorder_TC40() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertFalse(order.updateOrder(1001, 101, 999, pattern, true, false, true));
    }

    // TC41
    // Test Objective: Verify invalid delivery pattern cannot update an order
    // Input(s): No days selected
    // Expected Output(s): Order not updated
    @Test
    void update_invaliddeliverypatterncannotupdateanorder_TC41() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        boolean[] invalid = {false, false, false, false, false, false, false};
        assertFalse(order.updateOrder(1001, 101, 202, invalid, true, true, true));
    }

    // TC42
    // Test Objective: Verify unsuccessful update leaves order unchanged
    // Input(s): Update Successful = false
    // Expected Output(s): Order not updated
    @Test
    void update_unsuccessfulupdateleavesorderunchanged_TC42() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertFalse(order.updateOrder(1001, 101, 202, pattern, true, true, false));
        assertEquals(201, order.readOrder(1001, 101, true).getPublicationID());
    }

    // TC43
    // Test Objective: Verify order belonging to another customer cannot be updated
    // Input(s): Order owned by Customer 101; requested by 102
    // Expected Output(s): Order not updated
    @Test
    void update_orderbelongingtoanothercustomercannotbeupd_TC43() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertFalse(order.updateOrder(1001, 102, 202, pattern, true, true, true));
    }

    // TC44
    // Test Objective: Verify existing customer can delete an order
    // Input(s): Customer ID = 101
    // Expected Output(s): Order deleted
    @Test
    void delete_existingcustomercandeleteanorder_TC44() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertTrue(order.deleteOrder(1001, 101, true, true));
    }

    // TC45
    // Test Objective: Verify existing order can be deleted
    // Input(s): Order ID = 1001
    // Expected Output(s): Order deleted
    @Test
    void delete_existingordercanbedeleted_TC45() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertTrue(order.deleteOrder(1001, 101, true, true));
    }

    // TC46
    // Test Objective: Verify order belongs to the entered customer
    // Input(s): Order ID = 1001, Customer ID = 101
    // Expected Output(s): Order deleted
    @Test
    void delete_orderbelongstotheenteredcustomer_TC46() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertTrue(order.deleteOrder(1001, 101, true, true));
    }

    // TC47
    // Test Objective: Verify a deleted order cannot be retrieved
    // Input(s): Order ID = 1001
    // Expected Output(s): Order not found
    @Test
    void delete_adeletedordercannotberetrieved_TC47() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertTrue(order.deleteOrder(1001, 101, true, true));
        assertNull(order.readOrder(1001, 101, true));
    }

    // TC48
    // Test Objective: Verify deleting an order leaves other orders unchanged
    // Input(s): Customer 101 has two orders
    // Expected Output(s): Other order remains
    @Test
    void delete_deletinganorderleavesotherordersunchanged_TC48() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertTrue(order.createOrder(1002, 101, 202, pattern, true, true, true, true));
        assertTrue(order.deleteOrder(1001, 101, true, true));
        assertNotNull(order.readOrder(1002, 101, true));
    }

    // TC49
    // Test Objective: Verify invalid customer cannot delete an order
    // Input(s): Customer Exists = false
    // Expected Output(s): Order not deleted
    @Test
    void delete_invalidcustomercannotdeleteanorder_TC49() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertFalse(order.deleteOrder(1001, 101, false, true));
    }

    // TC50
    // Test Objective: Verify missing order cannot be deleted
    // Input(s): Order ID = 9999
    // Expected Output(s): Order not deleted
    @Test
    void delete_missingordercannotbedeleted_TC50() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertFalse(order.deleteOrder(9999, 101, true, true));
    }

    // TC51
    // Test Objective: Verify order belonging to another customer cannot be deleted
    // Input(s): Customer ID = 102 for order owned by 101
    // Expected Output(s): Order not deleted
    @Test
    void delete_orderbelongingtoanothercustomercannotbedel_TC51() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertFalse(order.deleteOrder(1001, 102, true, true));
        assertNotNull(order.readOrder(1001, 101, true));
    }

    // TC52
    // Test Objective: Verify an unsuccessful deletion reports failure
    // Input(s): Delete Successful = false
    // Expected Output(s): Order not deleted
    @Test
    void delete_anunsuccessfuldeletionreportsfailure_TC52() {
        Order order = new Order();
        boolean[] pattern = {true, false, true, false, false, false, false};
        assertTrue(order.createOrder(1001, 101, 201, pattern, true, true, true, true));
        assertFalse(order.deleteOrder(1001, 101, true, false));
        assertNotNull(order.readOrder(1001, 101, true));
    }
}
