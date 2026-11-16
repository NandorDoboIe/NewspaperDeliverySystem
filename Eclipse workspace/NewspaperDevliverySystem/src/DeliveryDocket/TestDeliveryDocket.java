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
}
