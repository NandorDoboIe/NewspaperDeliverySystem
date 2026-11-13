package CLI;

import java.util.Scanner;

public class DelAreaCLI {
	private final Scanner scanner;

	 
	public DelAreaCLI(Scanner scanner) {
	     
		this.scanner = scanner;
	 
	}
	    
	public void showMenu() {
	        
		while (true) {
	            
			System.out.println("\n## DELIVERY AREAS ##");	            
			System.out.println("1. Create Delivery Area");	            
			System.out.println("2. View All Delivery Area");	            
			System.out.println("3. View Delivery Area by ID");	            
			System.out.println("4. Update Delivery Area");	            
			System.out.println("5. Delete Delivery Area");	            
			System.out.println("0. Back to Main Menu");

	            
			switch (readChoice()) {	                
					case 1:
	                    createDelArea();
	                    break;
	                case 2:
	                	viewAllDelArea();
	                case 3:
	                    viewDelAreaById();
	                    break;
	                case 4:
	                    updateDelArea();
	                    break;
	                case 5:
	                    deleteDelArea();
	                    break;
	                case 0:
	                    return;
	                default:
	                    System.out.println("Invalid choice. Enter a menu number.");
	            }
	        }
	    }

	    private void createDelArea() {
	        System.out.println("Create Delivery Area: not connected yet.");
	    }

	    private void viewAllDelArea() {
	        System.out.println("View All Delivery Areas: not connected yet.");
	    }
	    
	    private void viewDelAreaById() {
	        System.out.println("View Delivery Area by ID: not connected yet.");
	    }

	    private void updateDelArea() {
	        System.out.println("Update Delivery Area: not connected yet.");
	    }

	    private void deleteDelArea() {
	        System.out.println("Delete Delivery Area: not connected yet.");
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

