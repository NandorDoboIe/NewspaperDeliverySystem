package DeliveryArea;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.LinkedHashMap;

public class DeliveryAreaTest {
    // TC01
    // Test Objective: Verify the location is accepted when 1 character long.
    // Input(s): Location = "A"
    // Expected Output(s): Location accepted
    @Test
    void validLocation_TC01(){
        DeliveryArea area = new DeliveryArea(1, "A");
        assertEquals("A", area.getLocation());
    }

    // TC02
    // Test Objective: Verify the location is accepted when more than 1 character long.
    // Input(s): Location = "Athlone Town Centre"
    // Expected Output(s): Location accepted
    @Test
    void validLocation_TC02(){
        DeliveryArea area = new DeliveryArea(1, "Athlone Town Centre");
        assertEquals("Athlone Town Centre", area.getLocation());
    }

    // TC03
    // Test Objective: Verify the delivery area is rejected when the location is left empty.
    // Input(s): Location = ""
    // Expected Output(s): Error message: Invalid delivery area details
    @Test
    void invalidLocation_TC03(){
        assertThrows(IllegalArgumentException.class, () -> new DeliveryArea(1, ""));
    }

    // TC04
    // Test Objective: Verify the delivery area is rejected when the location is only spaces.
    // Input(s): Location = "   "
    // Expected Output(s): Error message: Invalid delivery area details
    @Test
    void invalidLocation_TC04(){
        assertThrows(IllegalArgumentException.class, () -> new DeliveryArea(1, "   "));
    }

    // TC05
    // Test Objective: Verify the delivery area is rejected when the location is missing.
    // Input(s): Location = null
    // Expected Output(s): Error message: Invalid delivery area details
    @Test
    void invalidLocation_TC05(){
        assertThrows(IllegalArgumentException.class, () -> new DeliveryArea(1, null));
    }

    // TC06
    // Test Objective: Verify spaces around the location are removed when stored.
    // Input(s): Location = " Moate "
    // Expected Output(s): Location "Moate"
    @Test
    void locationTrimmed_TC06(){
        DeliveryArea area = new DeliveryArea(1, " Moate ");
        assertEquals("Moate", area.getLocation());
    }

    // TC07
    // Test Objective: Verify id 0 is accepted for a delivery area not yet saved to the database
    // Input(s): Id = 0
    // Expected Output(s): Id Accepted
    @Test
    void validId_TC07(){
        DeliveryArea area = new DeliveryArea(0, "Moate");
        assertEquals(0, area.getDeliveryAreaId());
    }

    // TC08
    // Test Objective: Verify id is accepted when it is larger than 0
    // Input(s): Id = 1
    // Expected Output(s): Id Accepted
    @Test
    void validId_TC08(){
        DeliveryArea area = new DeliveryArea(1, "Moate");
        assertEquals(1, area.getDeliveryAreaId());
    }

    // TC09
    // Test Objective: Verify id is rejected if it is negative
    // Input(s): Id = -1
    // Expected Output(s): Error message: Invalid delivery area details
    @Test
    void invalidId_TC09(){
        assertThrows(IllegalArgumentException.class, () -> new DeliveryArea(-1, "Moate"));
    }

    // TC10
    // Test Objective: Verify the correct Id is retrieved
    // Input(s): Id = 3
    // Expected Output(s): Id 3 is returned
    @Test
    void getId_TC10(){
        DeliveryArea area = new DeliveryArea(3, "Moate");
        assertEquals(3, area.getDeliveryAreaId());
    }

    // TC11
    // Test Objective: Verify the correct Location is retrieved
    // Input(s): Location = "Moate"
    // Expected Output(s): Location Moate is returned
    @Test
    void getLocation_TC11(){
        DeliveryArea area = new DeliveryArea(3, "Moate");
        assertEquals("Moate", area.getLocation());
    }

    // TC12
    // Test Objective: Verify a delivery area is created from a database row
    // Input(s): Row = {deliveryAreaId=4, location="Moate"}
    // Expected Output(s): Delivery area 4 with location Moate
    @Test
    void createFromRow_TC12(){
        LinkedHashMap<String, Object> row = new LinkedHashMap<>();
        row.put("deliveryAreaId", 4);
        row.put("location", "Moate");
        DeliveryArea area = new DeliveryArea(row);
        assertEquals(4, area.getDeliveryAreaId());
        assertEquals("Moate", area.getLocation());
    }

    // TC13
    // Test Objective: Verify a delivery area is created from CLI input where the id is typed as text
    // Input(s): Row = {deliveryAreaId="0", location="Moate"}
    // Expected Output(s): Delivery area with id 0
    @Test
    void createFromCliInput_TC13(){
        LinkedHashMap<String, Object> row = new LinkedHashMap<>();
        row.put("deliveryAreaId", "0");
        row.put("location", "Moate");
        assertEquals(0, new DeliveryArea(row).getDeliveryAreaId());
    }

    // TC14
    // Test Objective: Verify a row with an id that is not a number is rejected
    // Input(s): deliveryAreaId = "abc"
    // Expected Output(s): Error message: Invalid number: abc
    @Test
    void invalidRow_TC14(){
        LinkedHashMap<String, Object> row = new LinkedHashMap<>();
        row.put("deliveryAreaId", "abc");
        row.put("location", "Moate");
        assertThrows(IllegalArgumentException.class, () -> new DeliveryArea(row));
    }

    // TC15
    // Test Objective: Verify getData returns only the location column and not the id
    // Input(s): DeliveryArea(1, "Moate")
    // Expected Output(s): {location=Moate}
    @Test
    void getData_TC15(){
        LinkedHashMap<String, Object> data = new DeliveryArea(1, "Moate").getData();
        assertEquals(1, data.size());
        assertEquals("Moate", data.get("location"));
    }

    // TC16
    // Test Objective: Verify the delivery area description contains its details
    // Input(s): DeliveryArea(7, "Moate")
    // Expected Output(s): Text containing 7 and Moate
    @Test
    void deliveryAreaDisplay_TC16(){
        String text = new DeliveryArea(7, "Moate").toString();
        assertTrue(text.contains("7"));
        assertTrue(text.contains("Moate"));
    }
}
