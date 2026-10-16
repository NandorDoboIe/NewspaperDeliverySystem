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

}
