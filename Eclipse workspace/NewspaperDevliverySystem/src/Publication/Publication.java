package Publication;

import java.util.LinkedHashMap;

import Database.DataProvider;



public class Publication implements DataProvider {
	private final int publicationsId; // 0 means this record has not been inserted yet
	private String name;
	private String type;
	private int cost; // database stores an int cost
	private int stock;

	public Publication(int publicationsId, String name, String type, int cost, int stock) {
		if (publicationsId < 0 || !verifyDetails(name, type, cost, stock)) {
			throw new IllegalArgumentException("Invalid publication details");
		}
		this.publicationsId = publicationsId;
		this.name = name.trim();
		this.type = type.trim();
		this.cost = cost;
		this.stock = stock;
	}

	public Publication(LinkedHashMap<String, Object> row) {
	    this(
	        ((Number) row.get("publicationsId")).intValue(),
	        (String) row.get("name"),
	        (String) row.get("type"),
	        ((Number) row.get("cost")).intValue(),
	        ((Number) row.get("stock")).intValue()
	    );
	}
	public Publication() {
	    this.publicationsId = 0;
	}
	
	public int getPublicationID() {
		return publicationsId;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getCost() {
		return cost;
	}

	public int getStock() {
		return stock;
	}

	public boolean verifyName(String value) {
		return value != null && !value.trim().isEmpty() && value.trim().length() <= 150;
	}

	public boolean verifyType(String value) {
		return value != null && !value.trim().isEmpty() && value.trim().length() <= 150;
	}

	public boolean verifyCost(Integer value) {
		return value != null && value > 0;
	}

	public boolean verifyStock(Integer value) {
		return value != null && value >= 0;
	}

	public boolean verifyDetails(String name, String type, Integer cost, Integer stock) {
		return verifyName(name) && verifyType(type) && verifyCost(cost) && verifyStock(stock);
	}

	public boolean isLowStock() {
		return stock < 5; // 0-4 warning; 5 no warning
	}

	public boolean verifyRestock(int quantity) {
		return quantity > 0 && (long) stock + quantity <= Integer.MAX_VALUE;
	}

	public void updateDetails(String name, String type, int cost, int stock) {
		if (!verifyDetails(name, type, cost, stock)) {
			throw new IllegalArgumentException("Invalid publication details");
		}
		this.name = name.trim();
		this.type = type.trim();
		this.cost = cost;
		this.stock = stock;
	}

	public void addStock(int quantity) {
		if (!verifyRestock(quantity)) {
			throw new IllegalArgumentException("Restock quantity must be positive without overflow");
		}
		stock += quantity;
	}

	@Override
	public String toString() {
		return "Publication " + publicationsId + ": " + name + " (" + type + ")" + " | Cost: " + cost + " | Stock: "
				+ stock;
	}
}
