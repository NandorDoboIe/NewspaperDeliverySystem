package CLI;

import java.util.Scanner;

public class UserCLI {
	
	private final Scanner scanner;
	 
	public UserCLI(Scanner scanner) {
	     
		this.scanner = scanner;
	 
	}
	    
	public void showMenu() {
	        
		while (true) {
	            
			System.out.println("\n## USERS ##");	            
			System.out.println("1. Create User");	            
			System.out.println("2. View All Users");	            
			System.out.println("3. View User by ID");	            
			System.out.println("4. Update User");	            
			System.out.println("5. Delete User");	            
			System.out.println("0. Back to Main Menu");

	            
			switch (readChoice()) {	                
					case 1:
	                    createUser();
	                    break;
	                case 2:
	                	viewAllUsers();
	                case 3:
	                    viewUserById();
	                    break;
	                case 4:
	                    updateUser();
	                    break;
	                case 5:
	                    deleteUser();
	                    break;
	                case 0:
	                    return;
	                default:
	                    System.out.println("Invalid choice. Enter a menu number.");
	            }
	        }
	    }

	    private void createUser() {
	        System.out.println("Create Order: not connected yet.");
	    }

	    private void viewAllUsers() {
	        System.out.println("View All Users: not connected yet.");
	    }
	    
	    private void viewUserById() {
	        System.out.println("View Users by ID: not connected yet.");
	    }

	    private void updateUser() {
	        System.out.println("Update User: not connected yet.");
	    }

	    private void deleteUser() {
	        System.out.println("Delete User: not connected yet.");
	    }

	    private int readChoice() {
	        System.out.print("Enter choice: ");
	        if (!scanner.hasNextLine()) {
	            return 0;
	        }
	        try {
	            return Integer.parseInt(scanner.nextLine().trim());
	        } catch (NumberFormatException ex) {
	            return -1;
	        }
	    }
    
}
