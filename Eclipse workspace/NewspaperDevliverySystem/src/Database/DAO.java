package Database;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.sql.Statement;

public class DAO {
	
	private static final Set<String> ALLOWED_TABLES = Set.of("customers","publications","orders","deliveryDockets","deliveryAreas","users","bills");
	

	  public int addData(String tableName, DataProvider entity) {
	      checkForAllowedTables(tableName);

	      LinkedHashMap<String, Object> data = entity.getData();

	      if (data == null || data.isEmpty()) {
	          System.out.println("No data to insert");
	          return -1;
	      }

	      String columns = String.join(", ", data.keySet());
	      String placeholders = String.join(", ", Collections.nCopies(data.size(), "?"));

	      String sql = "INSERT INTO newspapersystemdatabase." + tableName
	              + " (" + columns + ") VALUES (" + placeholders + ")";

	      try (Connection conn = DatabaseConnection.getConnection();
	           PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

	          int index = 1;
	          for (Map.Entry<String, Object> entry : data.entrySet()) {
	              stmt.setObject(index, entry.getValue());
	              index++;
	          }

	          if (stmt.executeUpdate() == 0) {
	              return -1;
	          }

	          try (ResultSet keys = stmt.getGeneratedKeys()) {
	              if (keys.next()) {
	                  return keys.getInt(1);
	              }
	          }
	          return -1;

	      } catch (SQLException e) {
	          e.printStackTrace();
	          return -1;
	      }
	  }
	
	public ArrayList<LinkedHashMap<String, Object>> readTable(String tableName) {
		checkForAllowedTables(tableName);
		
		String sql = "SELECT * FROM newspapersystemdatabase."+tableName;
		ArrayList<LinkedHashMap<String,Object>> rows = new ArrayList<>();
		
		try(Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()){
			
			ResultSetMetaData meta = rs.getMetaData();
			int coloumnCount = meta.getColumnCount();
			
			while(rs.next()) {
				LinkedHashMap<String,Object> row = new LinkedHashMap<>();
				for(int i = 1; i<=coloumnCount; i++) {
					row.put(meta.getColumnLabel(i),rs.getObject(i));
				}
				rows.add(row);
			}
		
		} catch(SQLException e) {
			e.printStackTrace();
			return rows;
		}
		return rows;
	}
	
	public boolean deleteRow(String tableName, int id) {
		checkForAllowedTables(tableName);
		
		
		String sql = "DELETE from newspapersystemdatabase."+tableName+" WHERE  "+tableName.substring(0, tableName.length() - 1)+"Id = ?";
		
		try(Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, id);
			int rowsAffected = stmt.executeUpdate();
			
			if(rowsAffected==0) {
				System.out.println("No " + tableName + " with id " + id + " exists");
				return false;
			}
			return true;
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean editRow(String tableName, DataProvider entity, int id) {
	      checkForAllowedTables(tableName);

	      LinkedHashMap<String, Object> data = entity.getData();

	      if (data == null || data.isEmpty()) {
	          System.out.println("No data to update");
	          return false;
	      }
	      
	      StringBuilder setClause = new StringBuilder();
	      for (String column : data.keySet()) {
	          if (setClause.length() > 0) {
	              setClause.append(", ");
	          }
	          setClause.append(column).append(" = ?");
	      }
	      
	      String sql = "UPDATE newspapersystemdatabase." + tableName
	              + " SET " + setClause
	              + " WHERE " + tableName.substring(0, tableName.length() - 1) + "Id = ?";

	      try (Connection conn = DatabaseConnection.getConnection();
	           PreparedStatement stmt = conn.prepareStatement(sql)) {

	          int index = 1;
	          for (Map.Entry<String, Object> entry : data.entrySet()) {
	              stmt.setObject(index, entry.getValue());
	              index++;
	          }
	          
	          stmt.setInt(index, id);

	          int rowsAffected = stmt.executeUpdate();

	          if (rowsAffected == 0) {
	              System.out.println("No " + tableName + " with id " + id + " exists");
	              return false;
	          }
	          return true;

	      } catch (SQLException e) {
	          e.printStackTrace();
	          return false;
	      }
	  }
	
	public void checkForAllowedTables(String tableName) {
		if(!ALLOWED_TABLES.contains(tableName)) {
			throw new IllegalArgumentException("Invalid table name: "+tableName);
		}
	}
}
