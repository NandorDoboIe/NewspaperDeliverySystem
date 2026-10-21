package Database;
import java.sql.Connection;
import Customer.DataProvider;
import Customer.DatabaseConnection;
import Customer.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import Customer.DatabaseConnection;

public class DAO {
	
	private static final Set<String> ALLOWED_TABLES = Set.of("customer");
	
	public boolean addData(String tableName,DataProvider entity) {
		checkForAllowedTables(tableName);
		
		LinkedHashMap<String,Object> data = entity.getData();
		
		if(data == null || data.isEmpty()) {
			System.out.println("No data to insert");
			return false;
		}
		
		String columns = String.join(", ", data.keySet());
		String placeholders = String.join(", ", Collections.nCopies(data.size(),"?"));
		
		String sql = "INSERT INTO newspapersystemdatabase." +tableName+ " (" +columns +") Values ("+placeholders+")";
		
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			int index = 1;
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                stmt.setObject(index, entry.getValue()); 
                index++;
            }
			
			
			int rowsInserted = stmt.executeUpdate();

			return rowsInserted > 0;
			
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
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
		
		
		String sql = "DELETE from newspapersystemdatabase."+tableName+" WHERE  "+tableName+"Id="+id;
		
		try(Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)){
			int rowsAffected = stmt.executeUpdate();
			
			if(rowsAffected==0) {
				System.out.println("No customer with id "+id+" exits");
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
	              + " WHERE " + tableName + "Id = ?";

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
