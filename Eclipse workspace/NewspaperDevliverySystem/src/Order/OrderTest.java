
package Order;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class OrderTest {

    // TC01
    // Verify an order can be created with valid details.
    @Test
    void createValidOrder_TC01() {

        boolean[] pattern = {
            true, false, true, false,
            true, false, false
        };

        Order order = new Order(1001, 101, 201, pattern);

        assertEquals(1001, order.getOrderID());
        assertEquals(101, order.getCustomerID());
        assertEquals(201, order.getPublicationID());
        assertArrayEquals(pattern, order.getDeliveryPattern());
    }

    // TC02
    // Verify an invalid customer ID is rejected.
    @Test
    void invalidCustomerID_TC02() {

        boolean[] pattern = {
            true, false, false, false,
            false, false, false
        };

        assertThrows(IllegalArgumentException.class, () ->
            new Order(1001, 0, 201, pattern)
        );
    }

    // TC03
    // Verify an invalid delivery pattern is rejected.
    @Test
    void invalidDeliveryPattern_TC03() {

        boolean[] pattern = {
            false, false, false, false,
            false, false, false
        };

        assertThrows(IllegalArgumentException.class, () ->
            new Order(1001, 101, 201, pattern)
        );
    }
}
