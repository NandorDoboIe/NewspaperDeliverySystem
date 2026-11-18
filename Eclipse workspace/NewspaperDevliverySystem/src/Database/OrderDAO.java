package Database;

import Order.Order;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** SQL for orders; updates and deletes enforce customer ownership. */
public class OrderDAO {
    public boolean customerExists(int customerId) throws SQLException {
        return exists("SELECT 1 FROM customer WHERE customerId = ?", customerId);
    }

    public boolean publicationExists(int publicationId) throws SQLException {
        return exists("SELECT 1 FROM publications WHERE publicationsId = ?", publicationId);
    }

    private boolean exists(String sql, int id) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) { return rs.next(); }
        }
    }

    public int create(Order order) throws SQLException {
        String sql = "INSERT INTO orders (publicationId, customerId, pattern) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, order.getPublicationID());
            stmt.setInt(2, order.getCustomerID());
            stmt.setString(3, order.getPatternString());
            if (stmt.executeUpdate() != 1) throw new SQLException("Order was not inserted");
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (!keys.next()) throw new SQLException("Order ID not generated");
                return keys.getInt(1);
            }
        }
    }

    public List<Order> findByCustomer(int customerId) throws SQLException {
        String sql = "SELECT ordersId, customerId, publicationId, pattern FROM orders " +
                "WHERE customerId = ? ORDER BY ordersId";
        List<Order> results = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) results.add(fromRow(rs));
            }
        }
        return results;
    }

    public Order findByIdAndCustomer(int orderId, int customerId) throws SQLException {
        String sql = "SELECT ordersId, customerId, publicationId, pattern FROM orders " +
                "WHERE ordersId = ? AND customerId = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? fromRow(rs) : null;
            }
        }
    }

    public boolean update(int orderId, int customerId, int publicationId, boolean[] days) throws SQLException {
        String pattern = new Order(0, customerId, publicationId, days).getPatternString();
        String sql = "UPDATE orders SET publicationId = ?, pattern = ? " +
                "WHERE ordersId = ? AND customerId = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, publicationId);
            stmt.setString(2, pattern);
            stmt.setInt(3, orderId);
            stmt.setInt(4, customerId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int orderId, int customerId) throws SQLException {
        String sql = "DELETE FROM orders WHERE ordersId = ? AND customerId = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, customerId);
            return stmt.executeUpdate() > 0;
        }
    }

    private Order fromRow(ResultSet rs) throws SQLException {
        try {
            return new Order(rs.getInt("ordersId"), rs.getInt("customerId"),
                    rs.getInt("publicationId"), Order.patternFromString(rs.getString("pattern")));
        } catch (IllegalArgumentException e) {
            throw new SQLException("Invalid saved Order record", e);
        }
    }
}
