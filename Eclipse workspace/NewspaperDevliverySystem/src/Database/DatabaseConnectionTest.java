package Database;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

// "Database" sheet, CONNECTION section (TC40 - TC44).
// Requires MySQL running on localhost:3306 with root/root.
// DatabaseConnection hard-codes its details, so the failure cases call DriverManager
// directly with one detail changed and the others the same as DatabaseConnection.
public class DatabaseConnectionTest {

    private static final String URL = "jdbc:mysql://localhost:3306/newspapersystemdatabase";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    // TC40
    // Test Objective: Verify a connection is made with valid details.
    // Input(s): URL = jdbc:mysql://localhost:3306/newspapersystemdatabase, user = "root", password = "root", MySQL running
    // Expected Output(s): Open connection returned
    @Test
    void connectionMadeWithValidDetails_TC40() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            assertNotNull(conn);
            assertFalse(conn.isClosed());
        }
    }

    // TC41
    // Test Objective: Verify a connection fails with an incorrect password.
    // Input(s): password = "wrong"
    // Expected Output(s): SQLException: Access denied for user
    @Test
    void connectionFailsWithIncorrectPassword_TC41() {
        SQLException ex = assertThrows(SQLException.class,
                () -> DriverManager.getConnection(URL, USER, "wrong"));
        assertTrue(ex.getMessage().contains("Access denied for user"));
    }

    // TC42
    // Test Objective: Verify a connection fails with an incorrect username.
    // Input(s): user = "nobody"
    // Expected Output(s): SQLException: Access denied for user
    @Test
    void connectionFailsWithIncorrectUsername_TC42() {
        SQLException ex = assertThrows(SQLException.class,
                () -> DriverManager.getConnection(URL, "nobody", PASSWORD));
        assertTrue(ex.getMessage().contains("Access denied for user"));
    }

    // TC43
    // Test Objective: Verify a connection fails when the database does not exist.
    // Input(s): URL ends with /nodatabase
    // Expected Output(s): SQLException: Unknown database
    @Test
    void connectionFailsWhenDatabaseDoesNotExist_TC43() {
        SQLException ex = assertThrows(SQLException.class,
                () -> DriverManager.getConnection("jdbc:mysql://localhost:3306/nodatabase", USER, PASSWORD));
        assertTrue(ex.getMessage().contains("Unknown database"));
    }

    // TC44
    // Test Objective: Verify a connection fails when the MySQL server is not running.
    // Input(s): URL = jdbc:mysql://localhost:1/newspapersystemdatabase (nothing listening); manual test: stop the MySQL server
    // Expected Output(s): SQLException: Communications link failure
    @Test
    void connectionFailsWhenServerNotRunning_TC44() {
        SQLException ex = assertThrows(SQLException.class,
                () -> DriverManager.getConnection("jdbc:mysql://localhost:1/newspapersystemdatabase", USER, PASSWORD));
        assertTrue(ex.getMessage().contains("Communications link failure"));
    }

    // TC44 (manual)
    // Test Objective: Verify the real connection fails when the MySQL server is stopped.
    // Input(s): Stop the MySQL server, then run this test
    // Expected Output(s): SQLException
    @Test
    @Disabled("TC44 (real) - manual: stop the MySQL server, then call DatabaseConnection.getConnection().")
    void realServerStopped_TC44() {
        assertThrows(SQLException.class, DatabaseConnection::getConnection);
    }
}
