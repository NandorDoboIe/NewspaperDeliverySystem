
package Database;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnectionTest {

    @Test
    void testDatabaseConnection() throws SQLException {

        try (Connection conn = DatabaseConnection.getConnection()) {

            assertNotNull(conn);
            assertFalse(conn.isClosed());
        }
    }

    @Test
    void testOrdersTableExists() throws SQLException {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT ordersId, publicationId, customerId, pattern "
                 + "FROM orders LIMIT 1");
             ResultSet rs = stmt.executeQuery()) {

            assertNotNull(rs);
        }
    }
}
