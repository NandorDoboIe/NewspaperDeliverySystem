package DelArea;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestDelArea {

	// Test Number: TC01
		// Test Objective: Verify the minimum boundary for user ID
		// Input(s): 1
		// Expected Output: true
		@Test
		void verifyIdMinBoundary_TC01() {
		    User user = new User();
		    assertTrue(user.verifyId(1));
		}

		// Test Number: TC02
		// Test Objective: Verify a valid user ID
		// Input(s): 24
		// Expected Output: true
		@Test
		void verifyValidId_TC02() {
		    DelArea user = new DelArea();
		    assertTrue(user.verifyId(24));
		}

		// Test Number: TC03
		// Test Objective: Verify that a negative user ID is invalid
		// Input(s): -21
		// Expected Output: false
		@Test
		void invalidId_TC03() {
			DelArea user = new DelArea();
		    assertFalse(user.verifyId(-21));
		}

		// Test Number: TC04
		// Test Objective: Verify the minimum boundary for a user's name
		// Input(s): "J"
		// Expected Output: true
		@Test
		void verifyNameMinBoundary_TC04() {
			DelArea user = new DelArea();
		    assertTrue(user.verifyLocation("A"));
		}

		// Test Number: TC05
		// Test Objective: Verify a valid location
		// Input(s): "Athlone"
		// Expected Output: true
		@Test
		void verifyValidName_TC05() {
			DelArea user = new DelArea();
		    assertTrue(user.verifyLocation("Athlone"));
		}

		// Test Number: TC06
		// Test Objective: Verify that a blank location is invalid
		// Input(s): " "
		// Expected Output: false
		@Test
		void invalidName_TC06() {
			DelArea user = new DelArea();
		    assertFalse(user.verifyLocation(" "));
		}
		
		// Test Number: TC07
		// Test Objective: Verify that a delivery area is created
		// Input(s): 1, "Athlone"
		// Expected Output: true 
	    @Test
	    void createDelArea_TC07() {
	        DelArea area = new DelArea();

	        assertTrue(area.createDelArea(1, "Athlone"));

	        assertNotNull(area.readDelArea(1));
	        assertEquals("Athlone", area.readDelArea(1).getLocation());
	    }

	    // Test Number: TC08
	    // Test Objective: Verify that a delivery area is read
	 	// Input(s): 2
	 	// Expected Output: "Dublin"
	    @Test
	    void readDelArea_TC08() {
	        DelArea area = new DelArea();

	        area.createDelArea(2, "Dublin");

	        DelArea result = area.readDelArea(2);

	        assertNotNull(result);
	        assertEquals("Dublin", result.getLocation());
	    }

	    // Test Number: TC09
	    // Test Objective: Verify that a delivery area is updated
	 	// Input(s): (3, "Athlone"); update to (3,"Galway"), search by entering id 3
	 	// Expected Output: "Galway"
	    @Test
	    void updateDelArea_TC09() {
	        DelArea area = new DelArea();

	        area.createDelArea(3, "Athlone");

	        assertTrue(area.updateDelArea(3, "Galway"));

	        DelArea result = area.readDelArea(3);

	        assertNotNull(result);
	        assertEquals("Galway", result.getLocation());
	    }

	    // Test Number: TC10
	    // Test Objective: Verify that a delivery area is read
	 	// Input(s): delete delivery id 4
	 	// Expected Output: null when read id 4
	    @Test
	    void deleteDelArea_TC10() {
	        DelArea area = new DelArea();

	        area.createDelArea(4, "Athlone");

	        assertTrue(area.deleteDelArea(4));
	        assertNull(area.readDelArea(4));
	    }
}
