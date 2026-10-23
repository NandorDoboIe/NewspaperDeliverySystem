package DelArea;


import java.util.ArrayList;

public class DelArea {

    private int id;
    private String location;

    // Store all delivery areas
    private static ArrayList<DelArea> areas = new ArrayList<>();

    public DelArea() {
    }

    public DelArea(int id, String location) {
        this.id = id;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Verify delivery area ID
    public boolean verifyId(int id) {
        return id > 0;
    }

    // Verify delivery area location
    public boolean verifyLocation(String location) {
        return location != null && !location.trim().isEmpty();
    }

    // Verify delivery area details
    public boolean verifyDelArea() {
        return verifyId(id) && verifyLocation(location);
    }

    // CREATE: Add a delivery area
    public boolean createDelArea(int id, String location) {

        DelArea newArea = new DelArea(id, location);

        if (!newArea.verifyDelArea()) {
            return false;
        }

        // Prevent duplicate IDs
        if (readDelArea(id) != null) {
            return false;
        }

        areas.add(newArea);
        return true;
    }

    // READ: Find a delivery area by ID
    public DelArea readDelArea(int id) {

        for (DelArea area : areas) {
            if (area.getId() == id) {
                return area;
            }
        }

        return null;
    }

    // UPDATE: Update a delivery area's location by id
    public boolean updateDelArea(int id, String location) {

        DelArea area = readDelArea(id);

        if (area == null) {
            return false;
        }

        if (!verifyLocation(location)) {
            return false;
        }

        area.setLocation(location);
        return true;
    }

    // DELETE: Remove a delivery area by ID
    public boolean deleteDelArea(int id) {

        DelArea area = readDelArea(id);

        if (area == null) {
            return false;
        }

        areas.remove(area);
        return true;
    }

    // LIST: Display all delivery areas
    public void listDelAreas() {

        if (areas.isEmpty()) {
            System.out.println("No delivery areas found.");
            return;
        }

        for (DelArea area : areas) {
            System.out.println(
                "ID: " + area.getId()
                + ", Location: " + area.getLocation()
            );
        }
    }

    // Reset the list between tests
    static void resetAreas() {
        areas.clear();
    }
}
