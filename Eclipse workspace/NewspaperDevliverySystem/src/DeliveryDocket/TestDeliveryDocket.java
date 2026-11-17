package DeliveryDocket;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestDeliveryDocket {
	@Test
	void verifyIdMinBoundary_TC01() {
	    DeliveryDocket del = new DeliveryDocket();
	    assertTrue(del.verifyId(1));
	}
	
	@Test
	void verifyValidId_TC02() {
		DeliveryDocket del = new DeliveryDocket();
	    assertTrue(del.verifyId(24));
	}
	
	@Test
	void invalidId_TC03() {
		DeliveryDocket del = new DeliveryDocket();
	    assertFalse(del.verifyId(-21));
	}
	
	@Test
	void verifyOrderIdMinBoundary_TC04() {
	    DeliveryDocket del = new DeliveryDocket();
	    assertTrue(del.verifyOrderId(1));
	}
	
	@Test
	void verifyValidOrderId_TC05() {
		DeliveryDocket del = new DeliveryDocket();
	    assertTrue(del.verifyOrderId(24));
	}
	
	@Test
	void invalidOrderId_TC06() {
		DeliveryDocket del = new DeliveryDocket();
	    assertFalse(del.verifyOrderId(-21));
	}
	
	@Test
	void verifyCustomerIdMinBoundary_TC07() {
	    DeliveryDocket del = new DeliveryDocket();
	    assertTrue(del.verifyCustomerId(1));
	}
	
	@Test
	void verifyValidCustomerId_TC08() {
		DeliveryDocket del = new DeliveryDocket();
	    assertTrue(del.verifyCustomerId(24));
	}
	
	@Test
	void invalidCustomerId_TC09() {
		DeliveryDocket del = new DeliveryDocket();
	    assertFalse(del.verifyCustomerId(-21));
	}
	
	@Test
	void createDeliveryDocket_TC10() {
		DeliveryDocket del = new DeliveryDocket();

        assertTrue(del.createDeliveryDocket(1, 22,33));

        assertNotNull(del.readDeliveryDocket(1));
        assertEquals(22, del.readDeliveryDocket(1).getOrder_id());
        assertEquals(33, del.readDeliveryDocket(1).getCustomer_id());
	}
	
	@Test
	void readDeliveryDocket_TC11() {
		DeliveryDocket del = new DeliveryDocket();

        del.createDeliveryDocket(2, 33,44);

        DeliveryDocket result = del.readDeliveryDocket(2);

        assertNotNull(result);
        assertEquals(33, result.getOrder_id());
        assertEquals(44, result.getCustomer_id());
	}
}
