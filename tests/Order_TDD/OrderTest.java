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
}