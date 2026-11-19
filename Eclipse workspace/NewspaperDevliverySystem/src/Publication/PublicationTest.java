package Publication;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.LinkedHashMap;

/**
 * JUnit 5 unit tests for the refactored Publication model (no database
 * required).
 */
public class PublicationTest {

	private Publication sample() {
		return new Publication(101, "Daily News", "Newspaper", 3, 10);
	}

	// TC01 - A valid publication stores all supplied details.
	@Test
	void createValidPublication_TC01() {
		Publication p = sample();
		assertEquals(101, p.getPublicationID());
		assertEquals("Daily News", p.getName());
		assertEquals("Newspaper", p.getType());
		assertEquals(3, p.getCost());
		assertEquals(10, p.getStock());
	}

	// TC02 - ID 0 is allowed before MySQL generates an ID.
	@Test
	void newPublicationIdZero_TC02() {
		assertEquals(0, new Publication(0, "A", "N", 1, 0).getPublicationID());
	}

	// TC03 - Negative IDs are invalid.
	@Test
	void negativePublicationId_TC03() {
		assertThrows(IllegalArgumentException.class, () -> new Publication(-1, "A", "N", 1, 0));
	}

	// TC04 - Name: minimum boundary is one character.
	@Test
	void nameOneCharacter_TC04() {
		assertTrue(sample().verifyName("A"));
	}

	// TC05 - Name: normal valid value.
	@Test
	void nameMultipleCharacters_TC05() {
		assertTrue(sample().verifyName("Daily News"));
	}

	// TC06 - Name: empty string is invalid.
	@Test
	void nameEmpty_TC06() {
		assertFalse(sample().verifyName(""));
	}

	// TC07 - Name: spaces only are invalid.
	@Test
	void nameSpaces_TC07() {
		assertFalse(sample().verifyName("   "));
	}

	// TC08 - Name: null is invalid.
	@Test
	void nameNull_TC08() {
		assertFalse(sample().verifyName(null));
	}

	// TC09 - Name: 150 characters are accepted (SQL VARCHAR(150)).
	@Test
	void nameLength150_TC09() {
		assertTrue(sample().verifyName("A".repeat(150)));
	}

	// TC10 - Name: 151 characters are rejected.
	@Test
	void nameLength151_TC10() {
		assertFalse(sample().verifyName("A".repeat(151)));
	}

	// TC11 - Name: leading and trailing spaces are trimmed when stored.
	@Test
	void nameIsTrimmed_TC11() {
		assertEquals("Paper", new Publication(0, " Paper ", "News", 1, 0).getName());
	}

	// TC12 - Type: minimum boundary is one character.
	@Test
	void typeOneCharacter_TC12() {
		assertTrue(sample().verifyType("N"));
	}

	// TC13 - Type: normal valid value.
	@Test
	void typeMultipleCharacters_TC13() {
		assertTrue(sample().verifyType("Magazine"));
	}

	// TC14 - Type: empty string is invalid.
	@Test
	void typeEmpty_TC14() {
		assertFalse(sample().verifyType(""));
	}

	// TC15 - Type: spaces only are invalid.
	@Test
	void typeSpaces_TC15() {
		assertFalse(sample().verifyType("   "));
	}

	// TC16 - Type: null is invalid.
	@Test
	void typeNull_TC16() {
		assertFalse(sample().verifyType(null));
	}

	// TC17 - Type: 150 characters are valid.
	@Test
	void typeLength150_TC17() {
		assertTrue(sample().verifyType("T".repeat(150)));
	}

	// TC18 - Type: 151 characters are invalid.
	@Test
	void typeLength151_TC18() {
		assertFalse(sample().verifyType("T".repeat(151)));
	}

	// TC19 - Type: whitespace is trimmed when stored.
	@Test
	void typeIsTrimmed_TC19() {
		assertEquals("News", new Publication(0, "Paper", " News ", 1, 0).getType());
	}

	// TC20 - Cost: minimum valid integer is 1.
	@Test
	void costOne_TC20() {
		assertTrue(sample().verifyCost(1));
	}

	// TC21 - Cost: normal positive value is valid.
	@Test
	void costPositive_TC21() {
		assertTrue(sample().verifyCost(99));
	}

	// TC22 - Cost: zero is invalid.
	@Test
	void costZero_TC22() {
		assertFalse(sample().verifyCost(0));
	}

	// TC23 - Cost: negative values are invalid.
	@Test
	void costNegative_TC23() {
		assertFalse(sample().verifyCost(-1));
	}

	// TC24 - Cost: null represents a missing input and is invalid.
	@Test
	void costNull_TC24() {
		assertFalse(sample().verifyCost(null));
	}

	// TC25 - Stock: zero is a valid stock count.
	@Test
	void stockZero_TC25() {
		assertTrue(sample().verifyStock(0));
	}

	// TC26 - Stock: a positive count is valid.
	@Test
	void stockPositive_TC26() {
		assertTrue(sample().verifyStock(1));
	}

	// TC27 - Stock: negative values are invalid.
	@Test
	void stockNegative_TC27() {
		assertFalse(sample().verifyStock(-1));
	}

	// TC28 - Stock: null represents a missing input and is invalid.
	@Test
	void stockNull_TC28() {
		assertFalse(sample().verifyStock(null));
	}

	// TC29 - Constructor rejects an invalid name.
	@Test
	void constructorInvalidName_TC29() {
		assertThrows(IllegalArgumentException.class, () -> new Publication(0, "", "News", 1, 0));
	}

	// TC30 - Constructor rejects an invalid type.
	@Test
	void constructorInvalidType_TC30() {
		assertThrows(IllegalArgumentException.class, () -> new Publication(0, "Paper", "", 1, 0));
	}

	// TC31 - Constructor rejects an invalid cost.
	@Test
	void constructorInvalidCost_TC31() {
		assertThrows(IllegalArgumentException.class, () -> new Publication(0, "Paper", "News", 0, 0));
	}

	// TC32 - Constructor rejects an invalid stock count.
	@Test
	void constructorInvalidStock_TC32() {
		assertThrows(IllegalArgumentException.class, () -> new Publication(0, "Paper", "News", 1, -1));
	}

	// TC33 - All required details can pass validation.
	@Test
	void verifyAllDetails_TC33() {
		assertTrue(sample().verifyDetails("Paper", "Magazine", 1, 0));
	}

	// TC34 - An invalid detail causes combined validation to fail.
	@Test
	void verifyInvalidDetails_TC34() {
		assertFalse(sample().verifyDetails("Paper", "Magazine", 0, 0));
	}

	// TC35 - A valid update replaces the editable fields, not the ID.
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

	// TC36 - Invalid name updates are rejected and preserve prior data.
	@Test
	void updateInvalidName_TC36() {
		Publication p = sample();
		assertThrows(IllegalArgumentException.class, () -> p.updateDetails("", "Magazine", 5, 4));
		assertEquals("Daily News", p.getName());
		assertEquals(10, p.getStock());
	}

	// TC37 - Invalid type updates are rejected.
	@Test
	void updateInvalidType_TC37() {
		Publication p = sample();
		assertThrows(IllegalArgumentException.class, () -> p.updateDetails("Weekly", "", 5, 4));
		assertEquals("Newspaper", p.getType());
	}

	// TC38 - Invalid cost updates are rejected.
	@Test
	void updateInvalidCost_TC38() {
		Publication p = sample();
		assertThrows(IllegalArgumentException.class, () -> p.updateDetails("Weekly", "Magazine", 0, 4));
		assertEquals(3, p.getCost());
	}

	// TC39 - Invalid stock updates are rejected.
	@Test
	void updateInvalidStock_TC39() {
		Publication p = sample();
		assertThrows(IllegalArgumentException.class, () -> p.updateDetails("Weekly", "Magazine", 5, -1));
		assertEquals(10, p.getStock());
	}

	// TC40 - Stock 0 triggers the low-stock warning.
	@Test
	void lowStockZero_TC40() {
		assertTrue(new Publication(1, "Paper", "News", 1, 0).isLowStock());
	}

	// TC41 - Stock 4 triggers the low-stock warning.
	@Test
	void lowStockFour_TC41() {
		assertTrue(new Publication(1, "Paper", "News", 1, 4).isLowStock());
	}

	// TC42 - Stock 5 is not low stock.
	@Test
	void lowStockFive_TC42() {
		assertFalse(new Publication(1, "Paper", "News", 1, 5).isLowStock());
	}

	// TC43 - A positive restock amount increases stock correctly.
	@Test
	void restockValid_TC43() {
		Publication p = sample();
		p.addStock(3);
		assertEquals(13, p.getStock());
	}

	// TC44 - A zero restock amount is rejected.
	@Test
	void restockZero_TC44() {
		Publication p = sample();
		assertThrows(IllegalArgumentException.class, () -> p.addStock(0));
		assertEquals(10, p.getStock());
	}

	// TC45 - A negative restock amount is rejected.
	@Test
	void restockNegative_TC45() {
		Publication p = sample();
		assertThrows(IllegalArgumentException.class, () -> p.addStock(-1));
		assertEquals(10, p.getStock());
	}

	// TC46 - Restocking cannot overflow Java's maximum integer.
	@Test
	void restockOverflow_TC46() {
		Publication p = new Publication(1, "Paper", "News", 1, Integer.MAX_VALUE);
		assertThrows(IllegalArgumentException.class, () -> p.addStock(1));
		assertEquals(Integer.MAX_VALUE, p.getStock());
	}

	// TC47 - A valid restock can clear a low-stock alert.
	@Test
	void restockClearsLowStock_TC47() {
		Publication p = new Publication(1, "Paper", "News", 1, 4);
		assertTrue(p.isLowStock());
		p.addStock(1);
		assertFalse(p.isLowStock());
	}

	// TC48 - A publication's display string contains its main details.
	@Test
	void publicationDisplay_TC48() {
		String description = sample().toString();
		assertTrue(description.contains("Daily News"));
		assertTrue(description.contains("101"));
		assertTrue(description.contains("10"));
	}

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
