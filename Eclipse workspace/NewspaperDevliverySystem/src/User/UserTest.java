package User;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.LinkedHashMap;

public class UserTest {

    private User sample() {
        return new User(1, "Admin", "admin123", "0870000001", "admin");
    }

    private LinkedHashMap<String, Object> row() {
        LinkedHashMap<String, Object> row = new LinkedHashMap<>();
        row.put("name", "Danny Driver");
        row.put("password", "delivery1");
        row.put("contact", "0870000002");
        row.put("access_level", "deliveryPerson");
        return row;
    }

    // TC01
    // Test Objective: Verify a valid user stores all supplied details.
    // Input(s): Id = 1, Name = "Admin", Password = "admin123", Contact = "0870000001", Access Level = "admin"
    // Expected Output(s): User created with the supplied details
    @Test
    void createValidUser_TC01() {
        User u = sample();
        assertEquals(1, u.getUserId());
        assertEquals("Admin", u.getName());
        assertEquals("admin123", u.getPassword());
        assertEquals("0870000001", u.getContact());
        assertEquals("admin", u.getAccessLevel());
    }

    // TC02
    // Test Objective: Verify id 0 is accepted before MySQL generates an id.
    // Input(s): Id = 0
    // Expected Output(s): Id 0 accepted
    @Test
    void validId_TC02() {
        assertEquals(0, new User(0, "A", "pass", "1234567", "admin").getUserId());
    }

    // TC03
    // Test Objective: Verify a negative id is rejected.
    // Input(s): Id = -1
    // Expected Output(s): Error message: Invalid ID
    @Test
    void invalidId_TC03() {
        assertThrows(IllegalArgumentException.class, () -> new User(-1, "A", "pass", "1234567", "admin"));
    }

    // TC04
    // Test Objective: Verify the name is accepted when 1 character long.
    // Input(s): Name = "A"
    // Expected Output(s): Name accepted
    @Test
    void validName_TC04() {
        assertEquals("A", new User(1, "A", "pass", "1234567", "admin").getName());
    }

    // TC05
    // Test Objective: Verify the user is rejected when the name is left empty.
    // Input(s): Name = ""
    // Expected Output(s): Error message: Invalid Name
    @Test
    void invalidName_TC05() {
        assertThrows(IllegalArgumentException.class, () -> new User(1, "", "pass", "1234567", "admin"));
    }

    // TC06
    // Test Objective: Verify the user is rejected when the name is only spaces.
    // Input(s): Name = "   "
    // Expected Output(s): Error message: Invalid Name
    @Test
    void invalidName_TC06() {
        assertThrows(IllegalArgumentException.class, () -> new User(1, "   ", "pass", "1234567", "admin"));
    }

    // TC07
    // Test Objective: Verify the user is rejected when the name is missing.
    // Input(s): Name = null
    // Expected Output(s): Error message: Invalid Name
    @Test
    void invalidName_TC07() {
        assertThrows(IllegalArgumentException.class, () -> new User(1, null, "pass", "1234567", "admin"));
    }

    // TC08
    // Test Objective: Verify spaces around the name are removed when stored.
    // Input(s): Name = " Admin "
    // Expected Output(s): Name "Admin"
    @Test
    void nameTrimmed_TC08() {
        assertEquals("Admin", new User(1, " Admin ", "pass", "1234567", "admin").getName());
    }

    // TC09
    // Test Objective: Verify the password is accepted at the minimum length of 4 characters.
    // Input(s): Password = "abcd"
    // Expected Output(s): Password accepted
    @Test
    void validPassword_TC09() {
        assertEquals("abcd", new User(1, "A", "abcd", "1234567", "admin").getPassword());
    }

    // TC10
    // Test Objective: Verify a password shorter than 4 characters is rejected.
    // Input(s): Password = "abc"
    // Expected Output(s): Error message: Invalid Password (at least 4 characters)
    @Test
    void invalidPassword_TC10() {
        assertThrows(IllegalArgumentException.class, () -> new User(1, "A", "abc", "1234567", "admin"));
    }

    // TC11
    // Test Objective: Verify a missing password is rejected.
    // Input(s): Password = null
    // Expected Output(s): Error message: Invalid Password (at least 4 characters)
    @Test
    void invalidPassword_TC11() {
        assertThrows(IllegalArgumentException.class, () -> new User(1, "A", null, "1234567", "admin"));
    }

    // TC12
    // Test Objective: Verify contact details are accepted at the minimum length of 7 digits.
    // Input(s): Contact = "1234567"
    // Expected Output(s): Contact accepted
    @Test
    void validContact_TC12() {
        assertEquals("1234567", new User(1, "A", "pass", "1234567", "admin").getContact());
    }

    // TC13
    // Test Objective: Verify contact details are accepted at the maximum length of 11 digits.
    // Input(s): Contact = "12345678901"
    // Expected Output(s): Contact accepted
    @Test
    void validContact_TC13() {
        assertEquals("12345678901", new User(1, "A", "pass", "12345678901", "admin").getContact());
    }

    // TC14
    // Test Objective: Verify contact details are rejected when fewer than 7 digits.
    // Input(s): Contact = "123456"
    // Expected Output(s): Error message: Invalid Contact
    @Test
    void invalidContact_TC14() {
        assertThrows(IllegalArgumentException.class, () -> new User(1, "A", "pass", "123456", "admin"));
    }

    // TC15
    // Test Objective: Verify contact details are rejected when more than 11 digits.
    // Input(s): Contact = "123456789012"
    // Expected Output(s): Error message: Invalid Contact
    @Test
    void invalidContact_TC15() {
        assertThrows(IllegalArgumentException.class, () -> new User(1, "A", "pass", "123456789012", "admin"));
    }

    // TC16
    // Test Objective: Verify contact details are rejected when containing alphabetic characters.
    // Input(s): Contact = "08700abc01"
    // Expected Output(s): Error message: Invalid Contact
    @Test
    void invalidContact_TC16() {
        assertThrows(IllegalArgumentException.class, () -> new User(1, "A", "pass", "08700abc01", "admin"));
    }

    // TC17
    // Test Objective: Verify missing contact details are rejected.
    // Input(s): Contact = null
    // Expected Output(s): Error message: Invalid Contact
    @Test
    void invalidContact_TC17() {
        assertThrows(IllegalArgumentException.class, () -> new User(1, "A", "pass", null, "admin"));
    }

    // TC18
    // Test Objective: Verify the admin access level is accepted and gives admin rights.
    // Input(s): Access Level = "admin"
    // Expected Output(s): isAdmin returns true
    @Test
    void adminAccess_TC18() {
        assertTrue(new User(1, "A", "pass", "1234567", "admin").isAdmin());
    }

    // TC19
    // Test Objective: Verify the delivery person access level is accepted and does not give admin rights.
    // Input(s): Access Level = "deliveryPerson"
    // Expected Output(s): isAdmin returns false
    @Test
    void deliveryPersonAccess_TC19() {
        User u = new User(1, "A", "pass", "1234567", "deliveryPerson");
        assertEquals("deliveryPerson", u.getAccessLevel());
        assertFalse(u.isAdmin());
    }

    // TC20
    // Test Objective: Verify the admin access level typed in capitals is stored as "admin".
    // Input(s): Access Level = "ADMIN"
    // Expected Output(s): Access Level "admin"
    @Test
    void accessLevelCase_TC20() {
        assertEquals("admin", new User(1, "A", "pass", "1234567", "ADMIN").getAccessLevel());
    }

    // TC21
    // Test Objective: Verify the delivery person access level typed in lower case is stored as "deliveryPerson".
    // Input(s): Access Level = "deliveryperson"
    // Expected Output(s): Access Level "deliveryPerson"
    @Test
    void accessLevelCase_TC21() {
        assertEquals("deliveryPerson", new User(1, "A", "pass", "1234567", "deliveryperson").getAccessLevel());
    }

    // TC22
    // Test Objective: Verify spaces around the access level are ignored.
    // Input(s): Access Level = " deliveryPerson "
    // Expected Output(s): Access Level "deliveryPerson"
    @Test
    void accessLevelTrimmed_TC22() {
        assertEquals("deliveryPerson", new User(1, "A", "pass", "1234567", " deliveryPerson ").getAccessLevel());
    }

    // TC23
    // Test Objective: Verify an access level other than the two types is rejected.
    // Input(s): Access Level = "newsagent"
    // Expected Output(s): Error message: Invalid Access Level (admin or deliveryPerson)
    @Test
    void invalidAccessLevel_TC23() {
        assertThrows(IllegalArgumentException.class, () -> new User(1, "A", "pass", "1234567", "newsagent"));
    }

    // TC24
    // Test Objective: Verify an empty access level is rejected.
    // Input(s): Access Level = ""
    // Expected Output(s): Error message: Invalid Access Level (admin or deliveryPerson)
    @Test
    void invalidAccessLevel_TC24() {
        assertThrows(IllegalArgumentException.class, () -> new User(1, "A", "pass", "1234567", ""));
    }

    // TC25
    // Test Objective: Verify a missing access level is rejected.
    // Input(s): Access Level = null
    // Expected Output(s): Error message: Invalid Access Level (admin or deliveryPerson)
    @Test
    void invalidAccessLevel_TC25() {
        assertThrows(IllegalArgumentException.class, () -> new User(1, "A", "pass", "1234567", null));
    }

    // TC26
    // Test Objective: Verify the correct password is accepted at login.
    // Input(s): Password = "admin123"
    // Expected Output(s): true
    @Test
    void correctPassword_TC26() {
        assertTrue(sample().checkPassword("admin123"));
    }

    // TC27
    // Test Objective: Verify the password check is case-sensitive.
    // Input(s): Password = "ADMIN123"
    // Expected Output(s): false
    @Test
    void wrongCasePassword_TC27() {
        assertFalse(sample().checkPassword("ADMIN123"));
    }

    // TC28
    // Test Objective: Verify an empty password is refused at login.
    // Input(s): Password = ""
    // Expected Output(s): false
    @Test
    void emptyPassword_TC28() {
        assertFalse(sample().checkPassword(""));
    }

    // TC29
    // Test Objective: Verify a missing password is refused at login.
    // Input(s): Password = null
    // Expected Output(s): false
    @Test
    void nullPassword_TC29() {
        assertFalse(sample().checkPassword(null));
    }

    // TC30
    // Test Objective: Verify a row without an id (new user from the CLI) gets id 0.
    // Input(s): Row = {name="Danny Driver", password="delivery1", contact="0870000002", access_level="deliveryPerson"}
    // Expected Output(s): User with id 0
    @Test
    void createFromCliInput_TC30() {
        User u = new User(row());
        assertEquals(0, u.getUserId());
        assertEquals("Danny Driver", u.getName());
    }

    // TC31
    // Test Objective: Verify a row as returned by MySQL is accepted.
    // Input(s): userId = 5L
    // Expected Output(s): User with id 5
    @Test
    void createFromDatabaseRow_TC31() {
        LinkedHashMap<String, Object> r = row();
        r.put("userId", 5L);
        assertEquals(5, new User(r).getUserId());
    }

    // TC32
    // Test Objective: Verify a row with an invalid access level is rejected.
    // Input(s): access_level = "boss"
    // Expected Output(s): Error message: Invalid Access Level (admin or deliveryPerson)
    @Test
    void invalidRowAccessLevel_TC32() {
        LinkedHashMap<String, Object> r = row();
        r.put("access_level", "boss");
        assertThrows(IllegalArgumentException.class, () -> new User(r));
    }

    // TC33
    // Test Objective: Verify a row with an id that is not a number is rejected.
    // Input(s): userId = "abc"
    // Expected Output(s): Error message: Invalid number: abc
    @Test
    void invalidRowId_TC33() {
        LinkedHashMap<String, Object> r = row();
        r.put("userId", "abc");
        assertThrows(IllegalArgumentException.class, () -> new User(r));
    }

    // TC34
    // Test Objective: Verify the user is updated when all details are valid.
    // Input(s): "New Name", "newpass", "0871234567", "deliveryPerson"
    // Expected Output(s): User details updated
    @Test
    void validUpdate_TC34() {
        User u = sample();
        u.updateUser("New Name", "newpass", "0871234567", "deliveryPerson");
        assertEquals("New Name", u.getName());
        assertEquals("newpass", u.getPassword());
        assertEquals("0871234567", u.getContact());
        assertFalse(u.isAdmin());
    }

    // TC35
    // Test Objective: Verify getData returns the four database columns using the database column names.
    // Input(s): User(1, "Admin", "admin123", "0870000001", "admin")
    // Expected Output(s): {name=Admin, password=admin123, contact=0870000001, access_level=admin}
    @Test
    void getData_TC35() {
        LinkedHashMap<String, Object> data = sample().getData();
        assertEquals(4, data.size());
        assertEquals("Admin", data.get("name"));
        assertEquals("admin123", data.get("password"));
        assertEquals("0870000001", data.get("contact"));
        assertEquals("admin", data.get("access_level"));
    }

    // TC36
    // Test Objective: Verify the user description contains its details.
    // Input(s): User(1, "Admin", "admin123", "0870000001", "admin")
    // Expected Output(s): Text containing Admin, 0870000001 and admin
    @Test
    void userDisplay_TC36() {
        String s = sample().toString();
        assertTrue(s.contains("Admin"));
        assertTrue(s.contains("0870000001"));
        assertTrue(s.contains("admin"));
    }

    // TC37
    // Test Objective: Verify the user description never shows the password.
    // Input(s): User with password "admin123"
    // Expected Output(s): Text does not contain admin123
    @Test
    void passwordHidden_TC37() {
        assertFalse(sample().toString().contains("admin123"));
    }
}
