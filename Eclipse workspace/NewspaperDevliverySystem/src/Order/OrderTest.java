package Order;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.LinkedHashMap;

public class OrderTest {

    // Monday, Wednesday, Friday
    private boolean[] validPattern() {
        return new boolean[] {true, false, true, false, true, false, false};
    }

    private LinkedHashMap<String, Object> row() {
        LinkedHashMap<String, Object> row = new LinkedHashMap<>();
        row.put("orderId", 7);
        row.put("customerId", 101);
        row.put("publicationId", 201);
        row.put("pattern", "1010100");
        return row;
    }

    // TC01
    // Test Objective: Verify a valid order is created with the correct details.
    // Input(s): Order Id = 1001, Customer Id = 101, Publication Id = 201, Pattern = Mon/Wed/Fri
    // Expected Output(s): Order created with the supplied details
    @Test
    void createValidOrder_TC01() {
        boolean[] pattern = validPattern();
        Order order = new Order(1001, 101, 201, pattern);
        assertEquals(1001, order.getOrderID());
        assertEquals(101, order.getCustomerID());
        assertEquals(201, order.getPublicationID());
        assertArrayEquals(pattern, order.getDeliveryPattern());
    }

    // TC02
    // Test Objective: Verify a customer ID of zero is rejected.
    // Input(s): Customer Id = 0
    // Expected Output(s): Error message: Customer ID must be greater than 0.
    @Test
    void invalidCustomerID_TC02() {
        assertThrows(IllegalArgumentException.class, () -> new Order(1001, 0, 201, validPattern()));
    }

    // TC03
    // Test Objective: Verify a delivery pattern with no selected days is rejected.
    // Input(s): Pattern = no days selected
    // Expected Output(s): Error message: Select at least one delivery day.
    @Test
    void invalidDeliveryPattern_TC03() {
        boolean[] pattern = {false, false, false, false, false, false, false};
        assertThrows(IllegalArgumentException.class, () -> new Order(1001, 101, 201, pattern));
    }

    // TC04
    // Test Objective: Verify a negative order ID is rejected.
    // Input(s): Order Id = -1
    // Expected Output(s): Error message: Order ID cannot be negative.
    @Test
    void invalidNegativeOrderID_TC04() {
        assertThrows(IllegalArgumentException.class, () -> new Order(-1, 101, 201, validPattern()));
    }

    // TC05
    // Test Objective: Verify zero is accepted as the ID for an order awaiting a database-generated ID.
    // Input(s): Order Id = 0
    // Expected Output(s): Order Id 0 accepted
    @Test
    void validUnsavedOrderID_TC05() {
        Order order = new Order(0, 101, 201, validPattern());
        assertEquals(0, order.getOrderID());
    }

    // TC06
    // Test Objective: Verify publication ID zero is rejected.
    // Input(s): Publication Id = 0
    // Expected Output(s): Error message: Publication ID must be greater than 0.
    @Test
    void invalidZeroPublicationID_TC06() {
        assertThrows(IllegalArgumentException.class, () -> new Order(1001, 101, 0, validPattern()));
    }

    // TC07
    // Test Objective: Verify a negative publication ID is rejected.
    // Input(s): Publication Id = -1
    // Expected Output(s): Error message: Publication ID must be greater than 0.
    @Test
    void invalidNegativePublicationID_TC07() {
        assertThrows(IllegalArgumentException.class, () -> new Order(1001, 101, -1, validPattern()));
    }

    // TC08
    // Test Objective: Verify a negative customer ID is rejected.
    // Input(s): Customer Id = -1
    // Expected Output(s): Error message: Customer ID must be greater than 0.
    @Test
    void invalidNegativeCustomerID_TC08() {
        assertThrows(IllegalArgumentException.class, () -> new Order(1001, -1, 201, validPattern()));
    }

    // TC09
    // Test Objective: Verify a seven-day pattern with exactly one delivery day is accepted.
    // Input(s): Pattern = Monday only
    // Expected Output(s): Pattern accepted
    @Test
    void validOneDayPattern_TC09() {
        boolean[] pattern = {true, false, false, false, false, false, false};
        Order order = new Order(1001, 101, 201, pattern);
        assertArrayEquals(pattern, order.getDeliveryPattern());
    }

    // TC10
    // Test Objective: Verify a seven-day pattern with all days selected is accepted.
    // Input(s): Pattern = every day
    // Expected Output(s): Pattern accepted
    @Test
    void validAllDaysPattern_TC10() {
        boolean[] pattern = {true, true, true, true, true, true, true};
        Order order = new Order(1001, 101, 201, pattern);
        assertArrayEquals(pattern, order.getDeliveryPattern());
    }

    // TC11
    // Test Objective: Verify a null delivery pattern is rejected.
    // Input(s): Pattern = null
    // Expected Output(s): Error message: Delivery pattern is required.
    @Test
    void invalidNullPattern_TC11() {
        assertThrows(IllegalArgumentException.class, () -> new Order(1001, 101, 201, null));
    }

    // TC12
    // Test Objective: Verify a six-day delivery pattern is rejected.
    // Input(s): Pattern = 6 days
    // Expected Output(s): Error message: Delivery pattern must contain exactly 7 days
    @Test
    void invalidShortPattern_TC12() {
        boolean[] pattern = {true, false, true, false, true, false};
        assertThrows(IllegalArgumentException.class, () -> new Order(1001, 101, 201, pattern));
    }

    // TC13
    // Test Objective: Verify an eight-day delivery pattern is rejected.
    // Input(s): Pattern = 8 days
    // Expected Output(s): Error message: Delivery pattern must contain exactly 7 days
    @Test
    void invalidLongPattern_TC13() {
        boolean[] pattern = {true, false, true, false, true, false, false, false};
        assertThrows(IllegalArgumentException.class, () -> new Order(1001, 101, 201, pattern));
    }

    // TC14
    // Test Objective: Verify the getter values match the constructor values.
    // Input(s): Order Id = 5, Customer Id = 10, Publication Id = 15
    // Expected Output(s): 5, 10 and 15 are returned
    @Test
    void gettersReturnCorrectValues_TC14() {
        Order order = new Order(5, 10, 15, validPattern());
        assertEquals(5, order.getOrderID());
        assertEquals(10, order.getCustomerID());
        assertEquals(15, order.getPublicationID());
    }

    // TC15
    // Test Objective: Verify the customer ID minimum boundary: 1 is valid.
    // Input(s): Customer Id = 1
    // Expected Output(s): Customer Id 1 accepted
    @Test
    void customerIDBoundary_TC15() {
        Order order = new Order(1001, 1, 201, validPattern());
        assertEquals(1, order.getCustomerID());
    }

    // TC16
    // Test Objective: Verify the publication ID minimum boundary: 1 is valid.
    // Input(s): Publication Id = 1
    // Expected Output(s): Publication Id 1 accepted
    @Test
    void publicationIDBoundary_TC16() {
        Order order = new Order(1001, 101, 1, validPattern());
        assertEquals(1, order.getPublicationID());
    }

    // TC17
    // Test Objective: Verify changing the original array does not alter a created order.
    // Input(s): Pattern array changed after the order is created
    // Expected Output(s): Order keeps its original pattern
    @Test
    void constructorCopiesDeliveryPattern_TC17() {
        boolean[] pattern = validPattern();
        Order order = new Order(1001, 101, 201, pattern);
        pattern[0] = false;
        assertTrue(order.getDeliveryPattern()[0]);
    }

    // TC18
    // Test Objective: Verify changing an array returned by the getter does not alter the order.
    // Input(s): Returned pattern array changed
    // Expected Output(s): Order keeps its original pattern
    @Test
    void getterReturnsCopy_TC18() {
        Order order = new Order(1001, 101, 201, validPattern());
        boolean[] returned = order.getDeliveryPattern();
        returned[0] = false;
        assertTrue(order.getDeliveryPattern()[0]);
    }

    // TC19
    // Test Objective: Verify changing publication and delivery days succeeds.
    // Input(s): Publication Id = 202, Pattern = Tue/Thu
    // Expected Output(s): Order updated
    @Test
    void validUpdate_TC19() {
        Order order = new Order(1001, 101, 201, validPattern());
        boolean[] updated = {false, true, false, true, false, false, false};
        order.updateDetails(202, updated);
        assertEquals(202, order.getPublicationID());
        assertArrayEquals(updated, order.getDeliveryPattern());
    }

    // TC20
    // Test Objective: Verify updating with publication ID zero is rejected without changing the order.
    // Input(s): Publication Id = 0
    // Expected Output(s): Error message: Invalid order update; publication stays 201
    @Test
    void invalidZeroPublicationUpdate_TC20() {
        Order order = new Order(1001, 101, 201, validPattern());
        assertThrows(IllegalArgumentException.class, () -> order.updateDetails(0, validPattern()));
        assertEquals(201, order.getPublicationID());
    }

    // TC21
    // Test Objective: Verify updating with a negative publication ID is rejected.
    // Input(s): Publication Id = -1
    // Expected Output(s): Error message: Invalid order update; publication stays 201
    @Test
    void invalidNegativePublicationUpdate_TC21() {
        Order order = new Order(1001, 101, 201, validPattern());
        assertThrows(IllegalArgumentException.class, () -> order.updateDetails(-1, validPattern()));
        assertEquals(201, order.getPublicationID());
    }

    // TC22
    // Test Objective: Verify updating with a null delivery pattern is rejected.
    // Input(s): Pattern = null
    // Expected Output(s): Error message: Invalid order update; order unchanged
    @Test
    void invalidNullPatternUpdate_TC22() {
        Order order = new Order(1001, 101, 201, validPattern());
        assertThrows(IllegalArgumentException.class, () -> order.updateDetails(202, null));
        assertEquals(201, order.getPublicationID());
        assertArrayEquals(validPattern(), order.getDeliveryPattern());
    }

    // TC23
    // Test Objective: Verify updating with no selected delivery days is rejected.
    // Input(s): Pattern = no days selected
    // Expected Output(s): Error message: Invalid order update; order unchanged
    @Test
    void invalidEmptyPatternUpdate_TC23() {
        Order order = new Order(1001, 101, 201, validPattern());
        boolean[] invalid = {false, false, false, false, false, false, false};
        assertThrows(IllegalArgumentException.class, () -> order.updateDetails(202, invalid));
        assertEquals(201, order.getPublicationID());
        assertArrayEquals(validPattern(), order.getDeliveryPattern());
    }

    // TC24
    // Test Objective: Verify updating with six days is rejected.
    // Input(s): Pattern = 6 days
    // Expected Output(s): Error message: Invalid order update; publication stays 201
    @Test
    void invalidSixDayUpdate_TC24() {
        Order order = new Order(1001, 101, 201, validPattern());
        boolean[] invalid = {true, false, false, false, false, false};
        assertThrows(IllegalArgumentException.class, () -> order.updateDetails(202, invalid));
        assertEquals(201, order.getPublicationID());
    }

    // TC25
    // Test Objective: Verify updating with eight days is rejected.
    // Input(s): Pattern = 8 days
    // Expected Output(s): Error message: Invalid order update; publication stays 201
    @Test
    void invalidEightDayUpdate_TC25() {
        Order order = new Order(1001, 101, 201, validPattern());
        boolean[] invalid = {true, false, false, false, false, false, false, false};
        assertThrows(IllegalArgumentException.class, () -> order.updateDetails(202, invalid));
        assertEquals(201, order.getPublicationID());
    }

    // TC26
    // Test Objective: Verify updating does not change order ID or customer ownership.
    // Input(s): Publication Id = 202
    // Expected Output(s): Order Id 1001 and Customer Id 101 unchanged
    @Test
    void updatePreservesOrderAndCustomer_TC26() {
        Order order = new Order(1001, 101, 201, validPattern());
        order.updateDetails(202, validPattern());
        assertEquals(1001, order.getOrderID());
        assertEquals(101, order.getCustomerID());
    }

    // TC27
    // Test Objective: Verify the updated pattern is copied, not shared with the caller.
    // Input(s): Pattern array changed after the update
    // Expected Output(s): Order keeps the updated pattern
    @Test
    void updateCopiesDeliveryPattern_TC27() {
        Order order = new Order(1001, 101, 201, validPattern());
        boolean[] updated = {false, true, false, false, false, false, false};
        order.updateDetails(202, updated);
        updated[1] = false;
        assertTrue(order.getDeliveryPattern()[1]);
    }

    // TC28
    // Test Objective: Verify Mon/Wed/Fri converts to the exact stored SQL string.
    // Input(s): Pattern = Mon/Wed/Fri
    // Expected Output(s): "1010100"
    @Test
    void patternToString_TC28() {
        Order order = new Order(1001, 101, 201, validPattern());
        assertEquals("1010100", order.getPatternString());
    }

    // TC29
    // Test Objective: Verify all seven days convert correctly.
    // Input(s): Pattern = every day
    // Expected Output(s): "1111111"
    @Test
    void allDaysToString_TC29() {
        boolean[] pattern = {true, true, true, true, true, true, true};
        Order order = new Order(1001, 101, 201, pattern);
        assertEquals("1111111", order.getPatternString());
    }

    // TC30
    // Test Objective: Verify Sunday only converts to 0000001.
    // Input(s): Pattern = Sunday only
    // Expected Output(s): "0000001"
    @Test
    void sundayOnlyToString_TC30() {
        boolean[] pattern = {false, false, false, false, false, false, true};
        Order order = new Order(1001, 101, 201, pattern);
        assertEquals("0000001", order.getPatternString());
    }

    // TC31
    // Test Objective: Verify a valid SQL string converts into seven booleans.
    // Input(s): "1010100"
    // Expected Output(s): Mon/Wed/Fri pattern
    @Test
    void validStringToPattern_TC31() {
        assertArrayEquals(validPattern(), Order.patternFromString("1010100"));
    }

    // TC32
    // Test Objective: Verify a null SQL pattern is rejected.
    // Input(s): null
    // Expected Output(s): Error message: Delivery pattern is required.
    @Test
    void invalidNullString_TC32() {
        assertThrows(IllegalArgumentException.class, () -> Order.patternFromString(null));
    }

    // TC33
    // Test Objective: Verify a six-character SQL pattern is rejected.
    // Input(s): "101010"
    // Expected Output(s): Error message: Delivery pattern must contain exactly 7 characters.
    @Test
    void invalidShortString_TC33() {
        assertThrows(IllegalArgumentException.class, () -> Order.patternFromString("101010"));
    }

    // TC34
    // Test Objective: Verify an eight-character SQL pattern is rejected.
    // Input(s): "10101000"
    // Expected Output(s): Error message: Delivery pattern must contain exactly 7 characters.
    @Test
    void invalidLongString_TC34() {
        assertThrows(IllegalArgumentException.class, () -> Order.patternFromString("10101000"));
    }

    // TC35
    // Test Objective: Verify values other than 0 and 1 are rejected.
    // Input(s): "101X100"
    // Expected Output(s): Error message: Delivery pattern can contain only 0 and 1.
    @Test
    void invalidCharactersString_TC35() {
        assertThrows(IllegalArgumentException.class, () -> Order.patternFromString("101X100"));
    }

    // TC36
    // Test Objective: Verify an SQL string with no delivery days is rejected.
    // Input(s): "0000000"
    // Expected Output(s): Error message: Select at least one delivery day.
    @Test
    void invalidNoDeliveryDaysString_TC36() {
        assertThrows(IllegalArgumentException.class, () -> Order.patternFromString("0000000"));
    }

    // TC37
    // Test Objective: Verify converting a pattern to SQL and back preserves each day.
    // Input(s): Pattern = Tue/Thu/Sat/Sun
    // Expected Output(s): Same pattern returned
    @Test
    void patternRoundTrip_TC37() {
        boolean[] pattern = {false, true, false, true, false, true, true};
        Order order = new Order(1001, 101, 201, pattern);
        assertArrayEquals(pattern, Order.patternFromString(order.getPatternString()));
    }

    // TC38
    // Test Objective: Verify the order description includes its identifiers and delivery pattern.
    // Input(s): Order(1001, 101, 201, Mon/Wed/Fri)
    // Expected Output(s): Text containing 1001, 101, 201 and 1010100
    @Test
    void toStringContainsOrderDetails_TC38() {
        Order order = new Order(1001, 101, 201, validPattern());
        String text = order.toString();
        assertTrue(text.contains("1001"));
        assertTrue(text.contains("101"));
        assertTrue(text.contains("201"));
        assertTrue(text.contains("1010100"));
    }

    // TC39
    // Test Objective: Verify an order is created from a database row with a pattern string.
    // Input(s): Row = {orderId=7, customerId=101, publicationId=201, pattern="1010100"}
    // Expected Output(s): Order 7 with the row's details
    @Test
    void createFromRow_TC39() {
        Order order = new Order(row());
        assertEquals(7, order.getOrderID());
        assertEquals(101, order.getCustomerID());
        assertEquals(201, order.getPublicationID());
        assertArrayEquals(validPattern(), order.getDeliveryPattern());
    }

    // TC40
    // Test Objective: Verify a row without an order id (new order from the CLI) gets id 0.
    // Input(s): Row without orderId, ids typed as text
    // Expected Output(s): Order Id 0
    @Test
    void createFromCliInput_TC40() {
        LinkedHashMap<String, Object> row = row();
        row.remove("orderId");
        row.put("customerId", "101");
        row.put("publicationId", "201");
        assertEquals(0, new Order(row).getOrderID());
    }

    // TC41
    // Test Objective: Verify a row missing the customer id is rejected.
    // Input(s): Row without customerId
    // Expected Output(s): Error message: Missing order field: customerId
    @Test
    void missingRowField_TC41() {
        LinkedHashMap<String, Object> row = row();
        row.remove("customerId");
        assertThrows(IllegalArgumentException.class, () -> new Order(row));
    }

    // TC42
    // Test Objective: Verify a row with a decimal id is rejected.
    // Input(s): customerId = 2.5
    // Expected Output(s): Error message: customerId must be a whole number within the integer range.
    @Test
    void decimalRowId_TC42() {
        LinkedHashMap<String, Object> row = row();
        row.put("customerId", 2.5);
        assertThrows(IllegalArgumentException.class, () -> new Order(row));
    }

    // TC43
    // Test Objective: Verify a row with an invalid pattern string is rejected.
    // Input(s): pattern = "abc"
    // Expected Output(s): Error message: Delivery pattern must contain exactly 7 characters.
    @Test
    void invalidRowPattern_TC43() {
        LinkedHashMap<String, Object> row = row();
        row.put("pattern", "abc");
        assertThrows(IllegalArgumentException.class, () -> new Order(row));
    }

    // TC44
    // Test Objective: Verify getData returns the three database columns and not the id.
    // Input(s): Order(1001, 101, 201, Mon/Wed/Fri)
    // Expected Output(s): {customerId=101, publicationId=201, pattern=1010100}
    @Test
    void getData_TC44() {
        LinkedHashMap<String, Object> data = new Order(1001, 101, 201, validPattern()).getData();
        assertEquals(3, data.size());
        assertEquals(101, data.get("customerId"));
        assertEquals(201, data.get("publicationId"));
        assertEquals("1010100", data.get("pattern"));
    }

    // TC45
    // Test Objective: Verify an empty order cannot be saved.
    // Input(s): new Order()
    // Expected Output(s): Error message: Cannot save order
    @Test
    void emptyOrderNotSaved_TC45() {
        assertThrows(IllegalStateException.class, () -> new Order().getData());
    }
}
