package Publication;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import Database.DataProvider;

public class Publication implements DataProvider {

    private final int publicationsId; 
    private String name;
    private String type;
    private int cost;
    private int stock;

    public Publication(int publicationsId, String name, String type, int cost, int stock) {
        List<String> errors = getValidationErrors(name, type, cost, stock);
        if (publicationsId < 0) {
            errors.add(0, "Publication ID cannot be negative.");
        }
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Invalid publication details: " + String.join(" ", errors));
        }

        this.publicationsId = publicationsId;
        this.name = name.trim();
        this.type = type.trim();
        this.cost = cost;
        this.stock = stock;
    }

    // Works with DAOID . may be absent before insertion
    public Publication(LinkedHashMap<String, Object> row) {
        this(
            optionalId(row),
            toText(requiredField(row, "name"), "name"),
            toText(requiredField(row, "type"), "type"),
            toInt(requiredField(row, "cost"), "cost"),
            toInt(requiredField(row, "stock"), "stock")
        );
    }

    public Publication() {
        this.publicationsId = 0;
    }

    public int getPublicationID() { return publicationsId; }
    public String getName() { return name; }
    public String getType() { return type; }
    public int getCost() { return cost; }
    public int getStock() { return stock; }

    private boolean verifyName(String value) {
        return value != null && !value.trim().isEmpty() && value.trim().length() <= 150;
    }

    private boolean verifyType(String value) {
        return value != null && !value.trim().isEmpty() && value.trim().length() <= 150;
    }

    private boolean verifyCost(Integer value) {
        return value != null && value > 0;
    }

    private boolean verifyStock(Integer value) {
        return value != null && value >= 0;
    }

    private boolean verifyDetails(String name, String type, Integer cost, Integer stock) {
        return getValidationErrors(name, type, cost, stock).isEmpty();
    }

    // Checks all fields
    private List<String> getValidationErrors(String name, String type, Integer cost, Integer stock) {
        List<String> errors = new ArrayList<>();

        if (name == null || name.trim().isEmpty()) {
            errors.add("Name is required.");
        } else if (name.trim().length() > 150) {
            errors.add("Name must be 150 characters or fewer.");
        }

        if (type == null || type.trim().isEmpty()) {
            errors.add("Type is required.");
        } else if (type.trim().length() > 150) {
            errors.add("Type must be 150 characters or fewer.");
        }

        if (cost == null) {
            errors.add("Cost is required.");
        } else if (cost <= 0) {
            errors.add("Cost must be greater than 0.");
        }

        if (stock == null) {
            errors.add("Stock is required.");
        } else if (stock < 0) {
            errors.add("Stock cannot be negative.");
        }

        return errors;
    }

    private List<String> getValidationErrors() {
        return getValidationErrors(name, type, cost, stock);
    }

    // Returns text for the CLI.
    private String getValidationMessage(String name, String type, Integer cost, Integer stock) {
        List<String> errors = getValidationErrors(name, type, cost, stock);
        return errors.isEmpty() ? "Publication details are valid." : String.join(" ", errors);
    }

    private String getValidationMessage() {
        return getValidationMessage(name, type, cost, stock);
    }

    public boolean isLowStock() {
        return stock < 5;
    }

    private boolean verifyRestock(int quantity) {
        return quantity > 0 && (long) stock + quantity <= Integer.MAX_VALUE;
    }

    public void updateDetails(String name, String type, int cost, int stock) {
        if (!verifyDetails(name, type, cost, stock)) {
            throw new IllegalArgumentException(
                "Invalid publication details: " + getValidationMessage(name, type, cost, stock));
        }
        this.name = name.trim();
        this.type = type.trim();
        this.cost = cost;
        this.stock = stock;
    }

    public void addStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Restock quantity must be greater than 0.");
        }
        if (!verifyRestock(quantity)) {
            throw new IllegalArgumentException("Restock quantity would exceed the maximum stock value.");
        }
        stock += quantity;
    }

    @Override
    public String toString() {
        return "Publication " + publicationsId + ": " + name + " (" + type + ")"
                + " | Cost: " + cost + " | Stock: " + stock;
    }

    @Override
    public LinkedHashMap<String, Object> getData() {
        if (!verifyDetails(name, type, cost, stock)) {
            throw new IllegalStateException("Cannot save publication: " + getValidationMessage());
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("name", name);
        data.put("type", type);
        data.put("cost", cost);
        data.put("stock", stock);
        return data;
    }

    private static Object requiredField(LinkedHashMap<String, Object> row, String field) {
        if (row == null) {
            throw new IllegalArgumentException("Publication data is required.");
        }
        if (!row.containsKey(field)) {
            throw new IllegalArgumentException("Missing publication field: " + field);
        }
        return row.get(field);
    }

    private static String toText(Object value, String field) {
        if (value == null) {
            return null; // The normal validation will report that the field is required.
        }
        if (!(value instanceof String)) {
            throw new IllegalArgumentException(field + " must be text.");
        }
        return (String) value;
    }

    private static int optionalId(LinkedHashMap<String, Object> row) {
        if (row == null) {
            throw new IllegalArgumentException("Publication data is required.");
        }
        return row.containsKey("publicationsId")
                ? toInt(row.get("publicationsId"), "publicationsId") : 0;
    }

    private static int toInt(Object value, String field) {
        if (value == null) {
            throw new IllegalArgumentException(field + " must be a whole number.");
        }
        if (value instanceof Number) {
            double number = ((Number) value).doubleValue();
            if (!Double.isFinite(number) || number != Math.rint(number)
                    || number < Integer.MIN_VALUE || number > Integer.MAX_VALUE) {
                throw new IllegalArgumentException(field + " must be a whole number within the integer range.");
            }
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value).trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(field + " must be a whole number: " + value, ex);
        }
    }
}
