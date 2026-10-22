package User;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestUser {

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
	    User user = new User();
	    assertTrue(user.verifyId(24));
	}

	// Test Number: TC03
	// Test Objective: Verify that a negative user ID is invalid
	// Input(s): -21
	// Expected Output: false
	@Test
	void invalidId_TC03() {
	    User user = new User();
	    assertFalse(user.verifyId(-21));
	}
	

	// Test Number: TC04
	// Test Objective: Verify the minimum boundary for a user's name
	// Input(s): "J"
	// Expected Output: true
	@Test
	void verifyNameMinBoundary_TC04() {
	    User user = new User();
	    assertTrue(user.verifyName("J"));
	}

	// Test Number: TC05
	// Test Objective: Verify a valid user's name
	// Input(s): "John Doe"
	// Expected Output: true
	@Test
	void verifyValidName_TC05() {
	    User user = new User();
	    assertTrue(user.verifyName("John Doe"));
	}

	// Test Number: TC06
	// Test Objective: Verify that a blank name is invalid
	// Input(s): " "
	// Expected Output: false
	@Test
	void invalidName_TC06() {
	    User user = new User();
	    assertFalse(user.verifyName(" "));
	}

	// Test Number: TC07
	// Test Objective: Verify a valid password
	// Input(s): "a12345678"
	// Expected Output: true
	@Test
	void verifyPassword_TC07() {
	    User user = new User();
	    assertTrue(user.verifyPassword("a12345678"));
	}

	// Test Number: TC08
	// Test Objective: Verify that a blank password is invalid
	// Input(s): " "
	// Expected Output: false
	@Test
	void invalidPassword_Blank_TC08() {
	    User user = new User();
	    assertFalse(user.verifyPassword(" "));
	}

	// Test Number: TC09
	// Test Objective: Verify that a password below the minimum length is invalid
	// Input(s): "12345"
	// Expected Output: false
	@Test
	void invalidPassword_NotExceedLimit_TC09() {
	    User user = new User();
	    assertFalse(user.verifyPassword("12345"));
	}

	// Test Number: TC10
	// Test Objective: Verify a valid contact number
	// Input(s): "0812345678"
	// Expected Output: true
	@Test
	void verifyContact_TC10() {
	    User user = new User();
	    assertTrue(user.verifyContact("0812345678"));
	}

	// Test Number: TC11
	// Test Objective: Verify that a contact number with insufficient digits is invalid
	// Input(s): "082224"
	// Expected Output: false
	@Test
	void invalidContact_TC11() {
	    User user = new User();
	    assertFalse(user.verifyContact("082224"));
	}

	// Test Number: TC12
	// Test Objective: Verify that the newspaper delivery person access level is valid
	// Input(s): "newspaper delivery person"
	// Expected Output: true
	@Test
	void verifyValidNewspaperDeliveryPerson_TC12() {
	    User user = new User();
	    assertTrue(user.verifyAccessLevel("newspaper delivery person"));
	}

	// Test Number: TC13
	// Test Objective: Verify that the news agent access level is valid
	// Input(s): "news agent"
	// Expected Output: true
	@Test
	void verifyValidNewsAgent_TC13() {
	    User user = new User();
	    assertTrue(user.verifyAccessLevel("news agent"));
	}

	// Test Number: TC14
	// Test Objective: Verify that the admin access level is valid
	// Input(s): "admin"
	// Expected Output: true
	@Test
	void verifyValidAdmin_TC14() {
	    User user = new User();
	    assertTrue(user.verifyAccessLevel("admin"));
	}

	// Test Number: TC15
	// Test Objective: Verify that an unrecognised access level is invalid
	// Input(s): "customer"
	// Expected Output: false
	@Test
	void invalidAccessLevel_TC15() {
	    User user = new User();
	    assertFalse(user.verifyAccessLevel("customer"));
	}

	// Test Number: TC16
	// Test Objective: Verify that an empty access level is invalid
	// Input(s): ""
	// Expected Output: false
	@Test
	void invalidEmptyAccessLevel_TC16() {
	    User user = new User();
	    assertFalse(user.verifyAccessLevel(""));
	}

	// Test Number: TC17
	// Test Objective: Verify that a null access level is invalid
	// Input(s): null
	// Expected Output: false
	@Test
	void invalidNullAccessLevel_TC17() {
	    User user = new User();
	    assertFalse(user.verifyAccessLevel(null));
	}
	
	// Test Number: TC18
	// Test Objective: Verify that account is created
	// Input(s): 
	// Expected Output: true, not null when read user id 1
    @Test
    void createUserAccount_TC18() {
        User user = new User();

        assertTrue(user.createUserAccount(
            1, "John Doe", "password123",
            "0812345678", "news agent"
        ));

        assertNotNull(user.readUserAccount(1));
    }

    // Test Number: TC19
    // Test Objective: Verify that account info is read by id
 	// Input(s): new user account(2, "Jane Doe", "password123", "0823456789", "admin"), read by id 2
 	// Expected Output: "Jane Doe"
    @Test
    void readUserAccount_TC19() {
        User user = new User();

        user.createUserAccount(
            2, "Jane Doe", "password123",
            "0823456789", "admin"
        );

        User result = user.readUserAccount(2);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
    }

    // Test Number: TC20
    // Test Objective: Verify that a user account is updated by id
 	// Input(s): 2
 	// Expected Output: name="John Smith", contact = 
    @Test
    void updateUserAccount_TC20() {
        User user = new User();

        user.createUserAccount(
            3, "John Doe", "password123",
            "0812345678", "news agent"
        );

        assertTrue(user.updateUserAccount(
            3, "John Smith", "newpass123",
            "0834567890", "admin"
        ));

        User result = user.readUserAccount(3);

        assertNotNull(result);
        assertEquals("John Smith", result.getName());
        assertEquals("0834567890", result.getContact());
        assertEquals("admin", result.getAccess_level());
    }

    // Test Number: TC21
    // Test Objective: Verify that a user account is deleted by id
 	// Input(s): delete user id 4
 	// Expected Output: null when read user id 4
    @Test
    void deleteUserAccount_TC21() {
        User user = new User();

        user.createUserAccount(
            4, "John Doe", "password123",
            "0812345678", "news agent"
        );

        assertTrue(user.deleteUserAccount(4));
        assertNull(user.readUserAccount(4));
    }
    
    
}

