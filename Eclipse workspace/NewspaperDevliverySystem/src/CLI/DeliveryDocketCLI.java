package CLI;

import java.util.Scanner;

public class DeliveryDocketCLI {
	private final Scanner scanner;
	 
	public DeliveryDocketCLI(Scanner scanner) {
	     
		this.scanner = scanner;
	 
	}
	    
	public void showMenu() {
	        
		while (true) {
	            
			System.out.println("\n## DELIVERY DOCKET ##");	            
			System.out.println("1. Create Delivery Docket");	            
			System.out.println("2. View All Delivery Dockets");	            
			System.out.println("3. View Delivery Docket by ID");	            
			System.out.println("4. Update Delivery Docket");	            
			System.out.println("5. Delete Delivery Docket");	            
			System.out.println("0. Back to Main Menu");

	            
			switch (readChoice()) {	                
					case 1:
	                    createDelDocket();
	                    break;
	                case 2:
	                	viewAllDelDocket();
	                case 3:
	                    viewDelDocketById();
	                    break;
	                case 4:
	                    updateDelDocket();
	                    break;
	                case 5:
	                    deleteDelDocket();
	                    break;
	                case 0:
	                    return;
	                default:
	                    System.out.println("Invalid choice. Enter a menu number.");
	            }
	        }
	    }

	    private void createDelDocket() {
	        System.out.println("Create Delivery Docket: not connected yet.");
	    }

	    private void viewAllDelDocket() {
	        System.out.println("View All Delivery Dockets: not connected yet.");
	    }
	    
	    private void viewDelDocketById() {
	        System.out.println("View Delivery Docket by ID: not connected yet.");
	    }

	    private void updateDelDocket() {
	        System.out.println("Update Delivery Docket: not connected yet.");
	    }

	    private void deleteDelDocket() {
	        System.out.println("Delete Delivery Docket: not connected yet.");
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
