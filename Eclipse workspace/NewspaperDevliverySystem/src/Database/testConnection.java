package Database;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import Customer.Customer;

public class testConnection {
    public static void main(String[] args) {

        Customer customer = new Customer("Jane", "Doe", "12 High Street", "0871234567");
        Customer updateCustomer = new Customer("wfawv","adwad","asdwad","0871234567");

        DAO dao = new DAO();
        boolean success = dao.addData("customer", customer);
        ArrayList<LinkedHashMap<String,Object>> table = dao.readTable("customer");
        boolean deleteSuccess = dao.deleteRow("customer", 1);
        boolean updateSuccess = dao.editRow("customer",updateCustomer,3);
        

        if (success) {
            System.out.println("Customer inserted successfully.");
            for(LinkedHashMap<String,Object> row: table) {
            	System.out.println(row.get("customerId")+"-"+row.get("firstName")+"....");
            }
        } else {
            System.out.println("Insert failed.");
        }
        
        if (deleteSuccess) {
            System.out.println("Customer deleted successfully.");
        } else {
            System.out.println("Delete failed.");
        }
    }
}