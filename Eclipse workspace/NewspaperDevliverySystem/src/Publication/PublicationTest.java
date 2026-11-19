package Publication;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.LinkedHashMap;

public class PublicationTest {

    private Publication sample() {
        return new Publication(101, "Daily News", "Newspaper", 3, 10);
    }

    // TC01
    // Test Objective: Verify a valid publication stores all supplied details.
    // Input(s): Id = 101, Name = "Daily News", Type = "Newspaper", Cost = 3, Stock = 10
    // Expected Output(s): Publication created with the supplied details
    @Test
    void createValidPublication_TC01() {
        Publication p = sample();
        assertEquals(101, p.getPublicationID());
        assertEquals("Daily News", p.getName());
        assertEquals("Newspaper", p.getType());
        assertEquals(3, p.getCost());
        assertEquals(10, p.getStock());
    }

    // TC02
    // Test Objective: Verify id 0 is accepted before MySQL generates an id.
    // Input(s): Id = 0
    // Expected Output(s): Id 0 accepted
    @Test
    void newPublicationIdZero_TC02() {
        assertEquals(0, new Publication(0, "A", "N", 1, 0).getPublicationID());
    }

    // TC03
    // Test Objective: Verify a negative id is rejected.
    // Input(s): Id = -1
    // Expected Output(s): Error message: Invalid publication details
    @Test
    void negativePublicationId_TC03() {
        assertThrows(IllegalArgumentException.class, () -> new Publication(-1, "A", "N", 1, 0));
    }

    // TC04
    // Test Objective: Verify the name is accepted at the minimum length of 1 character.
    // Input(s): Name = "A"
    // Expected Output(s): true
    @Test
    void nameOneCharacter_TC04() {
        assertTrue(sample().verifyName("A"));
    }

    // TC05
    // Test Objective: Verify a normal name is accepted.
    // Input(s): Name = "Daily News"
    // Expected Output(s): true
    @Test
    void nameMultipleCharacters_TC05() {
        assertTrue(sample().verifyName("Daily News"));
    }

    // TC06
    // Test Objective: Verify an empty name is rejected.
    // Input(s): Name = ""
    // Expected Output(s): false
    @Test
    void nameEmpty_TC06() {
        assertFalse(sample().verifyName(""));
    }

    // TC07
    // Test Objective: Verify a name of only spaces is rejected.
    // Input(s): Name = "   "
    // Expected Output(s): false
    @Test
    void nameSpaces_TC07() {
        assertFalse(sample().verifyName("   "));
    }

    // TC08
    // Test Objective: Verify a missing name is rejected.
    // Input(s): Name = null
    // Expected Output(s): false
    @Test
    void nameNull_TC08() {
        assertFalse(sample().verifyName(null));
    }

    // TC09
    // Test Objective: Verify a name of 150 characters is accepted (SQL VARCHAR(150)).
    // Input(s): Name = 150 x "A"
    // Expected Output(s): true
    @Test
    void nameLength150_TC09() {
        assertTrue(sample().verifyName("A".repeat(150)));
    }

    // TC10
    // Test Objective: Verify a name of 151 characters is rejected.
    // Input(s): Name = 151 x "A"
    // Expected Output(s): false
    @Test
    void nameLength151_TC10() {
        assertFalse(sample().verifyName("A".repeat(151)));
    }

    // TC11
    // Test Objective: Verify spaces around the name are removed when stored.
    // Input(s): Name = " Paper "
    // Expected Output(s): Name "Paper"
    @Test
    void nameIsTrimmed_TC11() {
        assertEquals("Paper", new Publication(0, " Paper ", "News", 1, 0).getName());
    }

    // TC12
    // Test Objective: Verify the type is accepted at the minimum length of 1 character.
    // Input(s): Type = "N"
    // Expected Output(s): true
    @Test
    void typeOneCharacter_TC12() {
        assertTrue(sample().verifyType("N"));
    }

    // TC13
    // Test Objective: Verify a normal type is accepted.
    // Input(s): Type = "Magazine"
    // Expected Output(s): true
    @Test
    void typeMultipleCharacters_TC13() {
        assertTrue(sample().verifyType("Magazine"));
    }

    // TC14
    // Test Objective: Verify an empty type is rejected.
    // Input(s): Type = ""
    // Expected Output(s): false
    @Test
    void typeEmpty_TC14() {
        assertFalse(sample().verifyType(""));
    }

    // TC15
    // Test Objective: Verify a type of only spaces is rejected.
    // Input(s): Type = "   "
    // Expected Output(s): false
    @Test
    void typeSpaces_TC15() {
        assertFalse(sample().verifyType("   "));
    }

    // TC16
    // Test Objective: Verify a missing type is rejected.
    // Input(s): Type = null
    // Expected Output(s): false
    @Test
    void typeNull_TC16() {
        assertFalse(sample().verifyType(null));
    }

    // TC17
    // Test Objective: Verify a type of 150 characters is accepted.
    // Input(s): Type = 150 x "T"
    // Expected Output(s): true
    @Test
    void typeLength150_TC17() {
        assertTrue(sample().verifyType("T".repeat(150)));
    }

    // TC18
    // Test Objective: Verify a type of 151 characters is rejected.
    // Input(s): Type = 151 x "T"
    // Expected Output(s): false
    @Test
    void typeLength151_TC18() {
        assertFalse(sample().verifyType("T".repeat(151)));
    }

    // TC19
    // Test Objective: Verify spaces around the type are removed when stored.
    // Input(s): Type = " News "
    // Expected Output(s): Type "News"
    @Test
    void typeIsTrimmed_TC19() {
        assertEquals("News", new Publication(0, "Paper", " News ", 1, 0).getType());
    }

    // TC20
    // Test Objective: Verify the minimum valid cost of 1 is accepted.
    // Input(s): Cost = 1
    // Expected Output(s): true
    @Test
    void costOne_TC20() {
        assertTrue(sample().verifyCost(1));
    }

    // TC21
    // Test Objective: Verify a normal positive cost is accepted.
    // Input(s): Cost = 99
    // Expected Output(s): true
    @Test
    void costPositive_TC21() {
        assertTrue(sample().verifyCost(99));
    }

    // TC22
    // Test Objective: Verify a cost of zero is rejected.
    // Input(s): Cost = 0
    // Expected Output(s): false
    @Test
    void costZero_TC22() {
        assertFalse(sample().verifyCost(0));
    }

    // TC23
    // Test Objective: Verify a negative cost is rejected.
    // Input(s): Cost = -1
    // Expected Output(s): false
    @Test
    void costNegative_TC23() {
        assertFalse(sample().verifyCost(-1));
    }

    // TC24
    // Test Objective: Verify a missing cost is rejected.
    // Input(s): Cost = null
    // Expected Output(s): false
    @Test
    void costNull_TC24() {
        assertFalse(sample().verifyCost(null));
    }

    // TC25
    // Test Objective: Verify a stock of zero is accepted.
    // Input(s): Stock = 0
    // Expected Output(s): true
    @Test
    void stockZero_TC25() {
        assertTrue(sample().verifyStock(0));
    }

    // TC26
    // Test Objective: Verify a positive stock is accepted.
    // Input(s): Stock = 1
    // Expected Output(s): true
    @Test
    void stockPositive_TC26() {
        assertTrue(sample().verifyStock(1));
    }

    // TC27
    // Test Objective: Verify a negative stock is rejected.
    // Input(s): Stock = -1
    // Expected Output(s): false
    @Test
    void stockNegative_TC27() {
        assertFalse(sample().verifyStock(-1));
    }

    // TC28
    // Test Objective: Verify a missing stock is rejected.
    // Input(s): Stock = null
    // Expected Output(s): false
    @Test
    void stockNull_TC28() {
        assertFalse(sample().verifyStock(null));
    }

    // TC29
    // Test Objective: Verify the constructor rejects an invalid name.
    // Input(s): Name = ""
    // Expected Output(s): Error message: Invalid publication details
    @Test
    void constructorInvalidName_TC29() {
        assertThrows(IllegalArgumentException.class, () -> new Publication(0, "", "News", 1, 0));
    }

    // TC30
    // Test Objective: Verify the constructor rejects an invalid type.
    // Input(s): Type = ""
    // Expected Output(s): Error message: Invalid publication details
    @Test
    void constructorInvalidType_TC30() {
        assertThrows(IllegalArgumentException.class, () -> new Publication(0, "Paper", "", 1, 0));
    }

    // TC31
    // Test Objective: Verify the constructor rejects an invalid cost.
    // Input(s): Cost = 0
    // Expected Output(s): Error message: Invalid publication details
    @Test
    void constructorInvalidCost_TC31() {
        assertThrows(IllegalArgumentException.class, () -> new Publication(0, "Paper", "News", 0, 0));
    }

    // TC32
    // Test Objective: Verify the constructor rejects an invalid stock.
    // Input(s): Stock = -1
    // Expected Output(s): Error message: Invalid publication details
    @Test
    void constructorInvalidStock_TC32() {
        assertThrows(IllegalArgumentException.class, () -> new Publication(0, "Paper", "News", 1, -1));
    }

    // TC33
    // Test Objective: Verify all valid details pass combined validation.
    // Input(s): Name = "Paper", Type = "Magazine", Cost = 1, Stock = 0
    // Expected Output(s): true
    @Test
    void verifyAllDetails_TC33() {
        assertTrue(sample().verifyDetails("Paper", "Magazine", 1, 0));
    }

    // TC34
    // Test Objective: Verify one invalid detail fails combined validation.
    // Input(s): Name = "Paper", Type = "Magazine", Cost = 0, Stock = 0
    // Expected Output(s): false
    @Test
    void verifyInvalidDetails_TC34() {
        assertFalse(sample().verifyDetails("Paper", "Magazine", 0, 0));
    }

    // TC35
    // Test Objective: Verify a valid update replaces the editable fields but not the id.
    // Input(s): Name = "Weekly", Type = "Magazine", Cost = 5, Stock = 4
    // Expected Output(s): Publication updated, id stays 101
    @Test
    void updateValidDetails_TC35() {
        Publication p = sample();
        p.updateDetails("Weekly", "Magazine", 5, 4);
        assertEquals(101, p.getPublicationID());
        assertEquals("Weekly", p.getName());
        assertEquals("Magazine", p.getType());
        assertEquals(5, p.getCost());
        assertEquals(4, p.getStock());
    }

    // TC36
    // Test Objective: Verify an update with an invalid name is rejected and keeps the old data.
    // Input(s): Name = ""
    // Expected Output(s): Error message: Invalid publication details; name stays "Daily News"
    @Test
    void updateInvalidName_TC36() {
        Publication p = sample();
        assertThrows(IllegalArgumentException.class, () -> p.updateDetails("", "Magazine", 5, 4));
        assertEquals("Daily News", p.getName());
        assertEquals(10, p.getStock());
    }

    // TC37
    // Test Objective: Verify an update with an invalid type is rejected.
    // Input(s): Type = ""
    // Expected Output(s): Error message: Invalid publication details; type stays "Newspaper"
    @Test
    void updateInvalidType_TC37() {
        Publication p = sample();
        assertThrows(IllegalArgumentException.class, () -> p.updateDetails("Weekly", "", 5, 4));
        assertEquals("Newspaper", p.getType());
    }

    // TC38
    // Test Objective: Verify an update with an invalid cost is rejected.
    // Input(s): Cost = 0
    // Expected Output(s): Error message: Invalid publication details; cost stays 3
    @Test
    void updateInvalidCost_TC38() {
        Publication p = sample();
        assertThrows(IllegalArgumentException.class, () -> p.updateDetails("Weekly", "Magazine", 0, 4));
        assertEquals(3, p.getCost());
    }

    // TC39
    // Test Objective: Verify an update with an invalid stock is rejected.
    // Input(s): Stock = -1
    // Expected Output(s): Error message: Invalid publication details; stock stays 10
    @Test
    void updateInvalidStock_TC39() {
        Publication p = sample();
        assertThrows(IllegalArgumentException.class, () -> p.updateDetails("Weekly", "Magazine", 5, -1));
        assertEquals(10, p.getStock());
    }

    // TC40
    // Test Objective: Verify a stock of 0 triggers the low stock warning.
    // Input(s): Stock = 0
    // Expected Output(s): true
    @Test
    void lowStockZero_TC40() {
        assertTrue(new Publication(1, "Paper", "News", 1, 0).isLowStock());
    }

    // TC41
    // Test Objective: Verify a stock of 4 triggers the low stock warning.
    // Input(s): Stock = 4
    // Expected Output(s): true
    @Test
    void lowStockFour_TC41() {
        assertTrue(new Publication(1, "Paper", "News", 1, 4).isLowStock());
    }

    // TC42
    // Test Objective: Verify a stock of 5 does not trigger the low stock warning.
    // Input(s): Stock = 5
    // Expected Output(s): false
    @Test
    void lowStockFive_TC42() {
        assertFalse(new Publication(1, "Paper", "News", 1, 5).isLowStock());
    }

    // TC43
    // Test Objective: Verify a positive restock amount increases the stock.
    // Input(s): Stock = 10, Restock = 3
    // Expected Output(s): Stock 13
    @Test
    void restockValid_TC43() {
        Publication p = sample();
        p.addStock(3);
        assertEquals(13, p.getStock());
    }

    // TC44
    // Test Objective: Verify a restock amount of zero is rejected.
    // Input(s): Restock = 0
    // Expected Output(s): Error message: Restock quantity must be positive without overflow; stock stays 10
    @Test
    void restockZero_TC44() {
        Publication p = sample();
        assertThrows(IllegalArgumentException.class, () -> p.addStock(0));
        assertEquals(10, p.getStock());
    }

    // TC45
    // Test Objective: Verify a negative restock amount is rejected.
    // Input(s): Restock = -1
    // Expected Output(s): Error message: Restock quantity must be positive without overflow; stock stays 10
    @Test
    void restockNegative_TC45() {
        Publication p = sample();
        assertThrows(IllegalArgumentException.class, () -> p.addStock(-1));
        assertEquals(10, p.getStock());
    }

    // TC46
    // Test Objective: Verify restocking cannot go over Java's maximum integer.
    // Input(s): Stock = Integer.MAX_VALUE, Restock = 1
    // Expected Output(s): Error message: Restock quantity must be positive without overflow
    @Test
    void restockOverflow_TC46() {
        Publication p = new Publication(1, "Paper", "News", 1, Integer.MAX_VALUE);
        assertThrows(IllegalArgumentException.class, () -> p.addStock(1));
        assertEquals(Integer.MAX_VALUE, p.getStock());
    }

    // TC47
    // Test Objective: Verify a restock can clear the low stock warning.
    // Input(s): Stock = 4, Restock = 1
    // Expected Output(s): Low stock true before, false after
    @Test
    void restockClearsLowStock_TC47() {
        Publication p = new Publication(1, "Paper", "News", 1, 4);
        assertTrue(p.isLowStock());
        p.addStock(1);
        assertFalse(p.isLowStock());
    }

    // TC48
    // Test Objective: Verify the publication description contains its main details.
    // Input(s): Publication(101, "Daily News", "Newspaper", 3, 10)
    // Expected Output(s): Text containing Daily News, 101 and 10
    @Test
    void publicationDisplay_TC48() {
        String description = sample().toString();
        assertTrue(description.contains("Daily News"));
        assertTrue(description.contains("101"));
        assertTrue(description.contains("10"));
    }

    // TC49
    // Test Objective: Verify a publication is created from a database row.
    // Input(s): Row = {publicationId=1, name="Irish Independent", type="Newspaper", cost=3, stock=20}
    // Expected Output(s): Publication with the row's details
    @Test
    void createPublicationFromMap_TC49() {
        LinkedHashMap<String, Object> row = new LinkedHashMap<>();
        row.put("publicationId", 1);
        row.put("name", "Irish Independent");
        row.put("type", "Newspaper");
        row.put("cost", 3);
        row.put("stock", 20);

        Publication publication = new Publication(row);

        assertEquals(1, publication.getPublicationID());
        assertEquals("Irish Independent", publication.getName());
        assertEquals("Newspaper", publication.getType());
        assertEquals(3, publication.getCost());
        assertEquals(20, publication.getStock());
    }
}
