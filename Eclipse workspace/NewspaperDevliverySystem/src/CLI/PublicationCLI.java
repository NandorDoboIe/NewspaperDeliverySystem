package CLI;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import Database.DAO;
import Publication.Publication;

public class PublicationCLI {

    private final Scanner scanner;

    public PublicationCLI(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showMenu(ArrayList<Publication> publications) {
        while (true) {
            System.out.println("\n## PUBLICATIONS ##");
            System.out.println("1. Create Publication");
            System.out.println("2. View Publications");
            System.out.println("3. Update Publication");
            System.out.println("4. Delete Publication");
            System.out.println("5. Restock Publication");
            System.out.println("0. Back to Main Menu");
            showLowStockAlerts(publications);
            

            switch (readChoice()) {
                case 1:
                    createPublication(publications);
                    break;
                case 2:
                    viewPublications(publications);
                    break;
                case 3:
                    updatePublication(publications);
                    break;
                case 4:
                    deletePublication(publications);
                    break;
                case 5:
                    restockPublication(publications);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Enter a menu number.");
            }
        }
    }

    private void createPublication(ArrayList<Publication> publications) {
        
        ArrayList<String> variables = new ArrayList<String>(List.of("name","type","cost","stock"));
        LinkedHashMap<String, Object> userInput = new LinkedHashMap<>();
        userInput = takeUserInput(variables);
        
        try {
        	Publication newPublication = new Publication(userInput);
            System.out.println(newPublication.toString());
           
            DAO dao = new DAO();
            int newId = dao.addData("publications", newPublication);
            if(newId<=0) {
            	System.out.println("Insert failed");
            	return;
            }
            userInput.put("publicationsId",newId);
            Publication saved = new Publication(userInput);
            publications.add(saved);
            System.out.println("Publication created and uploaded to the database successfully");
            System.out.println(saved.toString());
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void viewPublications(ArrayList<Publication> publications) {
        if(publications.isEmpty()) {
        	System.out.println("Publications is empty");
        	return;
        }
        publications.forEach(publication -> System.out.println(publication.toString()));
    }

    private void updatePublication(ArrayList<Publication> publications) {
    	DAO dao = new DAO();
        viewPublications(publications);
        System.out.print("Id of the publication to update: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid id.");
            return;
        }
        Publication existing = findPublication(publications, id);
        if (existing == null) {
            System.out.println("No publication with id " + id);
            return;
        }

        LinkedHashMap<String, Object> input = takeUserInput(new ArrayList<>(List.of("name", "type", "cost", "stock")));
        input.put("publicationsId", id);
        try {
            Publication updated = new Publication(input);     // validates
            if (dao.editRow("publications", updated, id)) {
                publications.set(publications.indexOf(existing), updated);
                System.out.println("Updated: " + updated);
            } else {
                System.out.println("Update failed");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void deletePublication(ArrayList<Publication> publications) {
        viewPublications(publications);
        DAO dao = new DAO();
        System.out.print("Id of the publication to delete: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid id.");
            return;
        }
        Publication existing = findPublication(publications, id);
        if (existing == null) {
            System.out.println("No publication with id " + id);
            return;
        }
        if (dao.deleteRow("publications", id)) {
            publications.remove(existing);
            System.out.println("Deleted.");
        } else {
            System.out.println("Delete failed");
        }
    }
    
    private void restockPublication(ArrayList<Publication> publications) {
    	viewPublications(publications);
        DAO dao = new DAO();
        Publication restockingPublication = findByIdHandler(publications);

        LinkedHashMap<String, Object> input = takeUserInput(new ArrayList<>(List.of("stock")));
        
        restockingPublication.updateDetails(restockingPublication.getName(),restockingPublication.getType(),restockingPublication.getCost(),toInt(input.get("stock")));
    }

    public void showLowStockAlerts(ArrayList<Publication> publications) {
    	if(publications.isEmpty()) {
        	System.out.println("Publications is empty");
        	return;
        }
        publications.forEach(publication -> {
        	if(publication.getStock()<5) {
        		System.out.println("Stock of "+ publication.getName()+" (id: "+ publication.getPublicationID() +") is lock please restock!");
        	}
        });
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
    private LinkedHashMap<String, Object> takeUserInput(ArrayList<String> variables) {
		LinkedHashMap<String, Object> userInput = new LinkedHashMap<>();
		userInput.put("publicationsId", 0);
		variables.forEach(variable -> {
			System.out.print("Please enter "+variable+": ");
			String input = scanner.nextLine().trim();
			userInput.put(variable, input);
		});
		
		return userInput;
	}
    
    private Publication findPublication(ArrayList<Publication> publications, int targetId) {
    	for(Publication p: publications) {
    		if(p.getPublicationID() == targetId) {
    			return p;
    		}
    	}
    	return null;
    }
    private int toInt(Object value) {
	    if (value instanceof Number) {
	      return ((Number) value).intValue();
	    }
	    try {
	      return Integer.parseInt(String.valueOf(value).trim());
	    } catch (NumberFormatException ex) {
	      throw new IllegalArgumentException("Invalid number: " + value);
	    }
	}
    
    private Publication findByIdHandler(ArrayList<Publication> publications) {
    	System.out.print("Id of the publication to restock: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid id.");
            return null;
        }
        Publication existing = findPublication(publications, id);
        if (existing == null) {
            System.out.println("No publication with id " + id);
            return null;
        }
        return existing;
    }
}
