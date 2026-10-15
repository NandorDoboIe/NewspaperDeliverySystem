package Database;
import java.sql.Connection;
import Customer.DataProvider;
import Customer.DatabaseConnection;
import Customer.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import Customer.DatabaseConnection;

public class DAO {
	
	public boolean addData(String tableName,DataProvider entity) {
		LinkedHashMap<String,Object> data = entity.getData();
		
		if(data == null || data.isEmpty()) {
			System.out.println("No data to insert");
			return false;
		}
		
		String columns = String.join(", ", data.keySet());
		String placeholders = String.join(", ", Collections.nCopies(data.size(),"?"));
		
		String sql = "INSERT INTO " +tableName+ " (" +columns +") Values ("+placeholders+")";
		
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
}
