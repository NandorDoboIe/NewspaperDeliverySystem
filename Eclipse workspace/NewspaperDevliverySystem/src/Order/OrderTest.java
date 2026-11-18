package Order;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class OrderTest {

    private boolean[] validPattern() {
        return new boolean[] {true, false, true, false, true, false, false};
    }

    // TC01 - Verify a valid order is created with the correct details.
    @Test
    void createValidOrder_TC01() {
        boolean[] pattern = validPattern();
        Order order = new Order(1001, 101, 201, pattern);
        assertEquals(1001, order.getOrderID());
        assertEquals(101, order.getCustomerID());
        assertEquals(201, order.getPublicationID());
        assertArrayEquals(pattern, order.getDeliveryPattern());
    }

    // TC02 - Verify a customer ID of zero is rejected.
    @Test
    void invalidCustomerID_TC02() {
        assertThrows(IllegalArgumentException.class,
                () -> new Order(1001, 0, 201, validPattern()));
    }

    // TC03 - Verify a delivery pattern with no selected days is rejected.
    @Test
    void invalidDeliveryPattern_TC03() {
        boolean[] pattern = {false, false, false, false, false, false, false};
        assertThrows(IllegalArgumentException.class,
                () -> new Order(1001, 101, 201, pattern));
    }

    // TC04 - Verify a negative order ID is rejected.
    @Test
    void invalidNegativeOrderID_TC04() {
        assertThrows(IllegalArgumentException.class,
                () -> new Order(-1, 101, 201, validPattern()));
    }

    // TC05 - Verify zero is accepted as the ID for an order awaiting a database-generated ID.
    @Test
    void validUnsavedOrderID_TC05() {
        Order order = new Order(0, 101, 201, validPattern());
        assertEquals(0, order.getOrderID());
    }

    // TC06 - Verify publication ID zero is rejected.
    @Test
    void invalidZeroPublicationID_TC06() {
        assertThrows(IllegalArgumentException.class,
                () -> new Order(1001, 101, 0, validPattern()));
    }

    // TC07 - Verify a negative publication ID is rejected.
    @Test
    void invalidNegativePublicationID_TC07() {
        assertThrows(IllegalArgumentException.class,
                () -> new Order(1001, 101, -1, validPattern()));
    }

    // TC08 - Verify a negative customer ID is rejected.
    @Test
    void invalidNegativeCustomerID_TC08() {
        assertThrows(IllegalArgumentException.class,
                () -> new Order(1001, -1, 201, validPattern()));
    }

    // TC09 - Verify a seven-day pattern with exactly one delivery day is accepted.
    @Test
    void validOneDayPattern_TC09() {
        boolean[] pattern = {true, false, false, false, false, false, false};
        Order order = new Order(1001, 101, 201, pattern);
        assertArrayEquals(pattern, order.getDeliveryPattern());
    }

    // TC10 - Verify a seven-day pattern with all days selected is accepted.
    @Test
    void validAllDaysPattern_TC10() {
        boolean[] pattern = {true, true, true, true, true, true, true};
        Order order = new Order(1001, 101, 201, pattern);
        assertArrayEquals(pattern, order.getDeliveryPattern());
    }

    // TC11 - Verify a null delivery pattern is rejected.
    @Test
    void invalidNullPattern_TC11() {
        assertThrows(IllegalArgumentException.class,
                () -> new Order(1001, 101, 201, null));
    }

    // TC12 - Verify a six-day delivery pattern is rejected.
    @Test
    void invalidShortPattern_TC12() {
        boolean[] pattern = {true, false, true, false, true, false};
        assertThrows(IllegalArgumentException.class,
                () -> new Order(1001, 101, 201, pattern));
    }

    // TC13 - Verify an eight-day delivery pattern is rejected.
    @Test
    void invalidLongPattern_TC13() {
        boolean[] pattern = {true, false, true, false, true, false, false, false};
        assertThrows(IllegalArgumentException.class,
                () -> new Order(1001, 101, 201, pattern));
    }

    // TC14 - Verify the getter values match the constructor values.
    @Test
    void gettersReturnCorrectValues_TC14() {
        Order order = new Order(5, 10, 15, validPattern());
        assertEquals(5, order.getOrderID());
        assertEquals(10, order.getCustomerID());
        assertEquals(15, order.getPublicationID());
    }

    // TC15 - Verify the customer ID minimum boundary: 1 valid, 0 invalid.
    @Test
    void customerIDBoundary_TC15() {
        Order order = new Order(1001, 1, 201, validPattern());
        assertTrue(order.verifyCustomerID(1));
        assertFalse(order.verifyCustomerID(0));
    }

    // TC16 - Verify the publication ID minimum boundary: 1 valid, 0 invalid.
    @Test
    void publicationIDBoundary_TC16() {
        Order order = new Order(1001, 101, 1, validPattern());
        assertTrue(order.verifyPublicationID(1));
        assertFalse(order.verifyPublicationID(0));
    }

    // TC17 - Verify changing the original array does not alter a created order.
    @Test
    void constructorCopiesDeliveryPattern_TC17() {
        boolean[] pattern = validPattern();
        Order order = new Order(1001, 101, 201, pattern);
        pattern[0] = false;
        assertTrue(order.getDeliveryPattern()[0]);
    }

    // TC18 - Verify changing an array returned by the getter does not alter the order.
    @Test
    void getterReturnsCopy_TC18() {
        Order order = new Order(1001, 101, 201, validPattern());
        boolean[] returned = order.getDeliveryPattern();
        returned[0] = false;
        assertTrue(order.getDeliveryPattern()[0]);
    }

    // TC19 - Verify changing publication and delivery days succeeds.
    @Test
    void validUpdate_TC19() {
        Order order = new Order(1001, 101, 201, validPattern());
        boolean[] updated = {false, true, false, true, false, false, false};
        order.updateDetails(202, updated);
        assertEquals(202, order.getPublicationID());
        assertArrayEquals(updated, order.getDeliveryPattern());
    }

    // TC20 - Verify updating with publication ID zero is rejected without changing order.
    @Test
    void invalidZeroPublicationUpdate_TC20() {
        Order order = new Order(1001, 101, 201, validPattern());
        assertThrows(IllegalArgumentException.class,
                () -> order.updateDetails(0, validPattern()));
        assertEquals(201, order.getPublicationID());
    }

    // TC21 - Verify updating with negative publication ID is rejected.
    @Test
    void invalidNegativePublicationUpdate_TC21() {
        Order order = new Order(1001, 101, 201, validPattern());
        assertThrows(IllegalArgumentException.class,
                () -> order.updateDetails(-1, validPattern()));
        assertEquals(201, order.getPublicationID());
    }

    // TC22 - Verify updating with a null delivery pattern is rejected.
    @Test
    void invalidNullPatternUpdate_TC22() {
        Order order = new Order(1001, 101, 201, validPattern());
        assertThrows(IllegalArgumentException.class,
                () -> order.updateDetails(202, null));
        assertEquals(201, order.getPublicationID());
        assertArrayEquals(validPattern(), order.getDeliveryPattern());
    }

    // TC23 - Verify updating with no selected delivery days is rejected.
    @Test
    void invalidEmptyPatternUpdate_TC23() {
        Order order = new Order(1001, 101, 201, validPattern());
        boolean[] invalid = {false, false, false, false, false, false, false};
        assertThrows(IllegalArgumentException.class,
                () -> order.updateDetails(202, invalid));
        assertEquals(201, order.getPublicationID());
        assertArrayEquals(validPattern(), order.getDeliveryPattern());
    }

    // TC24 - Verify updating with six days is rejected.
    @Test
    void invalidSixDayUpdate_TC24() {
        Order order = new Order(1001, 101, 201, validPattern());
        boolean[] invalid = {true, false, false, false, false, false};
        assertThrows(IllegalArgumentException.class,
                () -> order.updateDetails(202, invalid));
        assertEquals(201, order.getPublicationID());
    }

    // TC25 - Verify updating with eight days is rejected.
    @Test
    void invalidEightDayUpdate_TC25() {
        Order order = new Order(1001, 101, 201, validPattern());
        boolean[] invalid = {true, false, false, false, false, false, false, false};
        assertThrows(IllegalArgumentException.class,
                () -> order.updateDetails(202, invalid));
        assertEquals(201, order.getPublicationID());
    }

    // TC26 - Verify updating does not change order ID or customer ownership.
    @Test
    void updatePreservesOrderAndCustomer_TC26() {
        Order order = new Order(1001, 101, 201, validPattern());
        order.updateDetails(202, validPattern());
        assertEquals(1001, order.getOrderID());
        assertEquals(101, order.getCustomerID());
    }

    // TC27 - Verify the updated pattern is copied, not shared with the caller.
    @Test
    void updateCopiesDeliveryPattern_TC27() {
        Order order = new Order(1001, 101, 201, validPattern());
        boolean[] updated = {false, true, false, false, false, false, false};
        order.updateDetails(202, updated);
        updated[1] = false;
        assertTrue(order.getDeliveryPattern()[1]);
    }

    // TC28 - Verify Mon/Wed/Fri converts to the exact stored SQL string.
    @Test
    void patternToString_TC28() {
        Order order = new Order(1001, 101, 201, validPattern());
        assertEquals("1010100", order.getPatternString());
    }

    // TC29 - Verify all seven days convert correctly.
    @Test
    void allDaysToString_TC29() {
        boolean[] pattern = {true, true, true, true, true, true, true};
        Order order = new Order(1001, 101, 201, pattern);
        assertEquals("1111111", order.getPatternString());
    }

    // TC30 - Verify Sunday only converts to 0000001.
    @Test
    void sundayOnlyToString_TC30() {
        boolean[] pattern = {false, false, false, false, false, false, true};
        Order order = new Order(1001, 101, 201, pattern);
        assertEquals("0000001", order.getPatternString());
    }

    // TC31 - Verify a valid SQL string converts into seven booleans.
    @Test
    void validStringToPattern_TC31() {
        assertArrayEquals(validPattern(), Order.patternFromString("1010100"));
    }

    // TC32 - Verify a null SQL pattern is rejected.
    @Test
    void invalidNullString_TC32() {
        assertThrows(IllegalArgumentException.class,
                () -> Order.patternFromString(null));
    }

    // TC33 - Verify a six-character SQL pattern is rejected.
    @Test
    void invalidShortString_TC33() {
        assertThrows(IllegalArgumentException.class,
                () -> Order.patternFromString("101010"));
    }

    // TC34 - Verify an eight-character SQL pattern is rejected.
    @Test
    void invalidLongString_TC34() {
        assertThrows(IllegalArgumentException.class,
                () -> Order.patternFromString("10101000"));
    }

    // TC35 - Verify values other than 0 and 1 are rejected.
    @Test
    void invalidCharactersString_TC35() {
        assertThrows(IllegalArgumentException.class,
                () -> Order.patternFromString("101X100"));
    }

    // TC36 - Verify an SQL string with no delivery days is rejected.
    @Test
    void invalidNoDeliveryDaysString_TC36() {
        assertThrows(IllegalArgumentException.class,
                () -> Order.patternFromString("0000000"));
    }

    // TC37 - Verify converting a pattern to SQL and back preserves each day.
    @Test
    void patternRoundTrip_TC37() {
        boolean[] pattern = {false, true, false, true, false, true, true};
        Order order = new Order(1001, 101, 201, pattern);
        assertArrayEquals(pattern, Order.patternFromString(order.getPatternString()));
    }

    // TC38 - Verify the order description includes its identifiers and delivery pattern.
    @Test
    void toStringContainsOrderDetails_TC38() {
        Order order = new Order(1001, 101, 201, validPattern());
        String text = order.toString();
        assertTrue(text.contains("1001"));
        assertTrue(text.contains("101"));
        assertTrue(text.contains("201"));
        assertTrue(text.contains("1010100"));
    }
}
