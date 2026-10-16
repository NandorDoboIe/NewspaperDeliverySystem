package Customer;
import Database.DAO;

public class testConnection {
    public static void main(String[] args) {

        Customer customer = new Customer("Jane", "Doe", "12 High Street", "0871234567");

        DAO dao = new DAO();
        boolean success = dao.addData("CustomerTable", customer);

        if (success) {
            System.out.println("Customer inserted successfully.");
        } else {
            System.out.println("Insert failed.");
        }
    }
}