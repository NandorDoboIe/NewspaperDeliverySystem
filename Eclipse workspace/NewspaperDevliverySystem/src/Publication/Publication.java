package Publication;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Database.DataProvider;

public class Publication implements DataProvider {

    private final Map<Integer, Publication> publications = new LinkedHashMap<>();
    private final List<String> errorLog = new ArrayList<>();

    private int publicationID;
    private String name;
    private String type;
    private Integer cost;
    private Integer stock;

    public Publication() {
    }

    public Publication(int publicationID, String name, String type,
                       Integer cost, Integer stock) {
        this.publicationID = publicationID;
        this.name = name;
        this.type = type;
        this.cost = cost;
        this.stock = stock;
    }

    public int getPublicationID() { return publicationID; }
    public String getName() { return name; }
    public String getType() { return type; }
    public Integer getCost() { return cost; }
    public Integer getStock() { return stock; }

    /** Validates that the publication name has at least one character. */
    public boolean verifyName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    /** Validates that the publication type is not empty. */
    public boolean verifyType(String type) {
        return type != null && !type.trim().isEmpty();
    }

    /** Validates a supplied publication cost greater than zero. */
    public boolean verifyCost(Integer cost) {
        return cost != null && cost > 0;
    }

    /** Validates a supplied stock count of zero or more. */
    public boolean verifyStock(Integer stock) {
        return stock != null && stock >= 0;
    }

    /** Verifies the success flag supplied by the data-storage operation. */
    public boolean verifySave(boolean saveSuccessful) {
        return saveSuccessful;
    }

    /** Validates all required publication details. */
    public boolean verifyDetails(String name, String type,
                                 Integer cost, Integer stock) {
        return verifyName(name)
                && verifyType(type)
                && verifyCost(cost)
                && verifyStock(stock);
    }

    /** Creates a publication in the temporary in-memory store. */
    public boolean createPublication(int publicationID, String name, String type,
                                     Integer cost, Integer stock,
                                     boolean uniqueID, boolean saveSuccessful) {
        if (publicationID <= 0 || !uniqueID || publications.containsKey(publicationID)
                || !verifyDetails(name, type, cost, stock)
                || !verifySave(saveSuccessful)) {
            return false;
        }
        publications.put(publicationID,
                new Publication(publicationID, name, type, cost, stock));
        return true;
    }

    /** Reads one publication; returns null for an invalid or missing record. */
    public Publication readPublication(int publicationID) {
        Publication found = publications.get(publicationID);
        if (found == null || !verifyDetails(found.name, found.type,
                                           found.cost, found.stock)) {
            return null;
        }
        return found;
    }

    /** Updates all editable publication fields if valid and saved. */
    public boolean updatePublication(int publicationID, String name, String type,
                                     Integer cost, Integer stock,
                                     boolean updateSuccessful) {
        Publication found = readPublication(publicationID);
        if (found == null || !verifyDetails(name, type, cost, stock)
                || !verifySave(updateSuccessful)) {
            return false;
        }
        found.name = name;
        found.type = type;
        found.cost = cost;
        found.stock = stock;
        return true;
    }

    /** Deletes a publication if it exists and removal succeeds. */
    public boolean deletePublication(int publicationID, boolean deleteSuccessful) {
        if (readPublication(publicationID) == null || !deleteSuccessful) {
            return false;
        }
        publications.remove(publicationID);
        return true;
    }

    /** Checks the low-stock boundary: fewer than five items. */
    public boolean isLowStock(int publicationID) {
        Publication found = readPublication(publicationID);
        return found != null && found.stock < 5;
    }

    /** Validates an attempted low-stock notification and logs false alarms. */
    public boolean sendLowStockWarning(int publicationID, boolean warningSent) {
        if (!isLowStock(publicationID)) {
            if (warningSent) {
                errorLog.add("Incorrect low-stock warning for publication " + publicationID);
            }
            return false;
        }
        return warningSent;
    }

    /** Exposes a snapshot of low-stock notification errors for testing. */
    public List<String> getErrorLog() {
        return new ArrayList<>(errorLog);
    }

    /** Indicates whether the news agent can open the restocking screen. */
    public boolean canAccessRestockUI(boolean isNewsAgent, boolean uiAvailable) {
        return isNewsAgent && uiAvailable;
    }

    /** Applies a positive stock increase to the selected publication. */
    public boolean restockPublication(int publicationID, int quantity,
                                      boolean isNewsAgent, boolean uiAvailable,
                                      boolean updateSuccessful) {
        Publication found = readPublication(publicationID);
        if (found == null || quantity <= 0
                || !canAccessRestockUI(isNewsAgent, uiAvailable)
                || !verifySave(updateSuccessful)
                || (long) found.stock + quantity > Integer.MAX_VALUE) {
            return false;
        }
        found.stock += quantity;
        return true;
    }

    /** Supplies publication table fields to the team's Database.DataProvider. */
    @Override
    public LinkedHashMap<String, Object> getData() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("name", name);
        data.put("type", type);
        data.put("cost", cost);
        data.put("stock", stock);
        return data;
    }

    @Override
    public String toString() {
        return "Publication ID: " + publicationID
                + "\nName: " + name
                + "\nType: " + type
                + "\nCost: " + cost
                + "\nStock: " + stock;
    }
}
