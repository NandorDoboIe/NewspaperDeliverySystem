package Database;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * JUnit tests for the "Database" sheet, CONNECTION section (TC40 - TC44).
 *
 * DatabaseConnection hard-codes its URL/user/password, so the failure cases
 * call DriverManager directly with the changed value while keeping the other
 * details identical to DatabaseConnection.
 *
 * Requires MySQL running on localhost:3306 with root/root.
 */
class DatabaseConnectionTest {

	private static final String URL = "jdbc:mysql://localhost:3306/newspapersystemdatabase";
	private static final String USER = "root";
	private static final String PASSWORD = "root";

	@Test // TC40
	void tc40_connectionMadeWithValidDetails() throws SQLException {
		try (Connection conn = DatabaseConnection.getConnection()) {
			assertNotNull(conn);
			assertFalse(conn.isClosed());
		}
	}

	@Test // TC41
	void tc41_connectionFailsWithIncorrectPassword() {
		SQLException ex = assertThrows(SQLException.class,
				() -> DriverManager.getConnection(URL, USER, "wrong"));
		assertTrue(ex.getMessage().contains("Access denied for user"));
	}

	@Test // TC42
	void tc42_connectionFailsWithIncorrectUsername() {
		SQLException ex = assertThrows(SQLException.class,
				() -> DriverManager.getConnection(URL, "nobody", PASSWORD));
		assertTrue(ex.getMessage().contains("Access denied for user"));
	}

	@Test // TC43
	void tc43_connectionFailsWhenDatabaseDoesNotExist() {
		SQLException ex = assertThrows(SQLException.class,
				() -> DriverManager.getConnection("jdbc:mysql://localhost:3306/nodatabase", USER, PASSWORD));
		assertTrue(ex.getMessage().contains("Unknown database"));
	}

	@Test // TC44 (simulated: nothing listens on port 1, so the connection is refused)
	void tc44_connectionFailsWhenServerNotRunning() {
		SQLException ex = assertThrows(SQLException.class,
				() -> DriverManager.getConnection("jdbc:mysql://localhost:1/newspapersystemdatabase", USER, PASSWORD));
		assertTrue(ex.getMessage().contains("Communications link failure"));
	}

	@Test
	@Disabled("TC44 (real) - manual: stop the MySQL server, then call DatabaseConnection.getConnection().")
	void tc44_realServerStopped() {
		assertThrows(SQLException.class, DatabaseConnection::getConnection);
	}
}
