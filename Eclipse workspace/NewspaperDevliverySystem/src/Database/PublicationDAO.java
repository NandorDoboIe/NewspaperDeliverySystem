package Database;

import Publication.Publication;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** SQL for publications. no SQL in the CLI. */
public class PublicationDAO {
    public int create(Publication publication) throws SQLException {
        String sql = "INSERT INTO publications (name, type, cost, stock) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, publication.getName());
            stmt.setString(2, publication.getType());
            stmt.setInt(3, publication.getCost());
            stmt.setInt(4, publication.getStock());
            if (stmt.executeUpdate() != 1) throw new SQLException("Publication was not inserted");
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (!keys.next()) throw new SQLException("Publication ID not generated");
                return keys.getInt(1);
            }
        }
    }

    public Publication findById(int id) throws SQLException {
        String sql = "SELECT publicationsId, name, type, cost, stock FROM publications WHERE publicationsId = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? fromRow(rs) : null;
            }
        }
    }

    public List<Publication> findAll() throws SQLException {
        return findList("SELECT publicationsId, name, type, cost, stock FROM publications ORDER BY publicationsId");
    }

    public List<Publication> findLowStock() throws SQLException {
        return findList("SELECT publicationsId, name, type, cost, stock FROM publications WHERE stock < 5 ORDER BY publicationsId");
    }

    private List<Publication> findList(String sql) throws SQLException {
        List<Publication> results = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) results.add(fromRow(rs));
        }
        return results;
    }

    public boolean update(Publication publication) throws SQLException {
        String sql = "UPDATE publications SET name = ?, type = ?, cost = ?, stock = ? WHERE publicationsId = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, publication.getName());
            stmt.setString(2, publication.getType());
            stmt.setInt(3, publication.getCost());
            stmt.setInt(4, publication.getStock());
            stmt.setInt(5, publication.getPublicationID());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM publications WHERE publicationsId = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean restock(int id, int quantity) throws SQLException {
        if (quantity <= 0) throw new IllegalArgumentException("Restock amount must be positive");
        String sql = "UPDATE publications SET stock = stock + ? WHERE publicationsId = ? AND stock <= ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, id);
            stmt.setInt(3, Integer.MAX_VALUE - quantity);
            return stmt.executeUpdate() > 0;
        }
    }

    private Publication fromRow(ResultSet rs) throws SQLException {
        return new Publication(rs.getInt("publicationsId"), rs.getString("name"),
                rs.getString("type"), rs.getInt("cost"), rs.getInt("stock"));
    }
}
