import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    public void testOrderID() {
        boolean[] pattern =
            {true, true, false, true, true, false, false};

        Order order =
            new Order(1, 101, 201, pattern);

        assertEquals(1, order.getOrderID());
    }


    @Test
    public void testCustomerID() {
        boolean[] pattern =
            {true, true, false, true, true, false, false};

        Order order =
            new Order(1, 101, 201, pattern);

        assertEquals(101, order.getCustomerID());
    }


    @Test
    public void testPublicationID() {
        boolean[] pattern =
            {true, true, false, true, true, false, false};

        Order order =
            new Order(1, 101, 201, pattern);

        assertEquals(201, order.getPublicationID());
    }


    @Test
    public void testDeliveryPattern() {
        boolean[] pattern =
            {true, true, false, true, true, false, false};

        Order order =
            new Order(1, 101, 201, pattern);

        assertArrayEquals(pattern, order.getDeliveryPattern());
    }


    @Test
    public void testMondayDelivery() {
        boolean[] pattern =
            {true, true, false, true, true, false, false};

        Order order =
            new Order(1, 101, 201, pattern);

        assertTrue(order.getDeliveryPattern()[0]);
    }


    @Test
    public void testWednesdayNoDelivery() {
        boolean[] pattern =
            {true, true, false, true, true, false, false};

        Order order =
            new Order(1, 101, 201, pattern);

        assertFalse(order.getDeliveryPattern()[2]);
    }


    @Test
    public void testSundayNoDelivery() {
        boolean[] pattern =
            {true, true, false, true, true, false, false};

        Order order =
            new Order(1, 101, 201, pattern);

        assertFalse(order.getDeliveryPattern()[6]);
    }
}
