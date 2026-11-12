package Database;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import Customer.Customer;

/**
 * JUnit tests for the "Database" sheet (TC01 - TC47), DAO part.
 *
 * Requires MySQL running on localhost:3306 with the newspapersystemdatabase
 * schema (databaseSetup.sql) and root/root credentials.
 *
 * The tests never wipe the customer table: every row a test inserts is
 * tracked and removed again in @AfterEach, and row counts are compared
 * relative to the count before the test, so existing data is left alone.
 */
class DAOTest {

	private DAO dao;
	private final List<Integer> createdIds = new ArrayList<>();
	private final ByteArrayOutputStream console = new ByteArrayOutputStream();
	private PrintStream originalOut;

	@BeforeEach
	void setUp() {
		dao = new DAO();
		originalOut = System.out;
		System.setOut(new PrintStream(console));
	}

	@AfterEach
	void tearDown() {
		System.setOut(originalOut);
		for (int id : createdIds) {
			dao.deleteRow("customer", id);
		}
		createdIds.clear();
	}

	// ---------- helpers ----------

	private Customer jane() {
		return new Customer("Jane", "Doe", "12 High Street", "0871234567");
	}

	private Set<Integer> currentIds() {
		Set<Integer> ids = new HashSet<>();
		for (LinkedHashMap<String, Object> row : dao.readTable("customer")) {
			ids.add(((Number) get(row, "customerId")).intValue());
		}
		return ids;
	}

	/** Inserts a customer and returns its generated id (tracked for clean-up). */
	private int addCustomer(Customer c) {
		Set<Integer> before = currentIds();
		assertTrue(dao.addData("customer", c), "setup insert should succeed");
		Set<Integer> after = currentIds();
		after.removeAll(before);
		assertEquals(1, after.size(), "exactly one new row expected");
		int id = after.iterator().next();
		createdIds.add(id);
		return id;
	}

	/** Column lookup ignoring case (table columns are "firstname"/"surname"). */
	private static Object get(LinkedHashMap<String, Object> row, String key) {
		for (String k : row.keySet()) {
			if (k.equalsIgnoreCase(key)) {
				return row.get(k);
			}
		}
		return null;
	}

	private LinkedHashMap<String, Object> findRow(int id) {
		for (LinkedHashMap<String, Object> row : dao.readTable("customer")) {
			if (((Number) get(row, "customerId")).intValue() == id) {
				return row;
			}
		}
		return null;
	}

	private String output() {
		return console.toString();
	}

	private static DataProvider providerOf(LinkedHashMap<String, Object> data) {
		return () -> data;
	}

	// ====================== CREATE (DAO.addData) ======================

	@Test // TC01
	void tc01_allowedTableNameAccepted() {
		assertDoesNotThrow(() -> dao.checkForAllowedTables("customer"));
	}

	@Test // TC02
	void tc02_invalidTableNameRejected() {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> dao.addData("users", jane()));
		assertEquals("Invalid table name: users", ex.getMessage());
	}

	@Test // TC03
	void tc03_emptyTableNameRejected() {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> dao.addData("", jane()));
		assertEquals("Invalid table name: ", ex.getMessage());
	}

	@Test // TC04
	void tc04_tableNamesAreCaseSensitive() {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> dao.addData("Customer", jane()));
		assertEquals("Invalid table name: Customer", ex.getMessage());
	}

	@Test // TC05
	void tc05_emptyDataNotInserted() {
		int before = dao.readTable("customer").size();
		assertFalse(dao.addData("customer", providerOf(new LinkedHashMap<>())));
		assertTrue(output().contains("No data to insert"));
		assertEquals(before, dao.readTable("customer").size());
	}

	@Test // TC06
	void tc06_nullDataNotInserted() {
		assertFalse(dao.addData("customer", providerOf(null)));
		assertTrue(output().contains("No data to insert"));
	}

	@Test // TC07
	void tc07_validCustomerInserted() {
		Set<Integer> before = currentIds();
		assertTrue(dao.addData("customer", jane()));
		Set<Integer> after = currentIds();
		after.removeAll(before);
		createdIds.addAll(after);
	}

	@Test // TC08
	void tc08_rowCountIncreasesByOne() {
		int before = dao.readTable("customer").size();
		addCustomer(jane());
		assertEquals(before + 1, dao.readTable("customer").size());
	}

	@Test // TC09
	void tc09_insertedValuesMatchEntity() {
		int id = addCustomer(jane());
		LinkedHashMap<String, Object> row = findRow(id);
		assertNotNull(row);
		assertEquals("Jane", get(row, "firstName"));
		assertEquals("Doe", get(row, "surName"));
		assertEquals("12 High Street", get(row, "address"));
		assertEquals("0871234567", get(row, "contact"));
	}

	@Test // TC10
	void tc10_newIdGeneratedForEachRow() {
		int id1 = addCustomer(jane());
		int id2 = addCustomer(jane());
		assertNotEquals(id1, id2);
	}

	@Test
	@Disabled("TC11 - manual: stop the MySQL server, then run. Expect false and a stack trace.")
	void tc11_insertFailsWhenDatabaseUnreachable() {
		assertFalse(dao.addData("customer", jane()));
	}

	@Test // TC12
	void tc12_insertFailsOnColumnConstraint() {
		String tooLong = "x".repeat(300); // columns are VARCHAR(150)
		Customer bad = new Customer(tooLong, "Doe", "12 High Street", "0871234567");
		int before = dao.readTable("customer").size();
		assertFalse(dao.addData("customer", bad));
		assertEquals(before, dao.readTable("customer").size());
	}

	// ====================== READ (DAO.readTable) ======================

	@Test // TC13
	void tc13_readAllowedTableNameAccepted() {
		assertDoesNotThrow(() -> dao.readTable("customer"));
	}

	@Test // TC14
	void tc14_readInvalidTableNameRejected() {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> dao.readTable("users"));
		assertEquals("Invalid table name: users", ex.getMessage());
	}

	@Test // TC15
	void tc15_allRowsReturned() {
		int before = dao.readTable("customer").size();
		addCustomer(jane());
		addCustomer(jane());
		addCustomer(jane());
		assertEquals(before + 3, dao.readTable("customer").size());
	}

	@Test // TC16
	void tc16_eachRowContainsEveryColumn() {
		addCustomer(jane());
		List<LinkedHashMap<String, Object>> rows = dao.readTable("customer");
		assertFalse(rows.isEmpty());
		for (LinkedHashMap<String, Object> row : rows) {
			assertEquals(5, row.size());
			for (String col : new String[] { "customerId", "firstName", "surName", "address", "contact" }) {
				boolean found = row.keySet().stream().anyMatch(k -> k.equalsIgnoreCase(col));
				assertTrue(found, "missing column " + col);
			}
		}
	}

	@Test // TC17
	void tc17_rowValuesAreCorrect() {
		int id = addCustomer(jane());
		LinkedHashMap<String, Object> row = findRow(id);
		assertEquals(id, ((Number) get(row, "customerId")).intValue());
		assertEquals("Jane", get(row, "firstName"));
		assertEquals("Doe", get(row, "surName"));
		assertEquals("12 High Street", get(row, "address"));
		assertEquals("0871234567", get(row, "contact"));
	}

	@Test // TC18
	void tc18_columnsReturnedInTableOrder() {
		addCustomer(jane());
		List<String> expected = List.of("customerid", "firstname", "surname", "address", "contact");
		for (LinkedHashMap<String, Object> row : dao.readTable("customer")) {
			List<String> keys = new ArrayList<>();
			for (String k : row.keySet()) {
				keys.add(k.toLowerCase());
			}
			assertEquals(expected, keys);
		}
	}

	@Test // TC19
	void tc19_emptyTableReturnsEmptyList() {
		// Only meaningful on an empty table; we never wipe real data.
		assumeTrue(dao.readTable("customer").isEmpty(), "customer table is not empty - skipped");
		assertEquals(0, dao.readTable("customer").size());
	}

	@Test
	@Disabled("TC20 - manual: stop the MySQL server, then run. Expect empty list and a stack trace.")
	void tc20_readReturnsEmptyListWhenDatabaseUnreachable() {
		assertTrue(dao.readTable("customer").isEmpty());
	}

	// ====================== EDIT (DAO.editRow) ======================

	@Test // TC21
	void tc21_editAllowedTableNameAccepted() {
		assertDoesNotThrow(() -> dao.checkForAllowedTables("customer"));
	}

	@Test // TC22
	void tc22_editInvalidTableNameRejected() {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> dao.editRow("users", jane(), 1));
		assertEquals("Invalid table name: users", ex.getMessage());
	}

	@Test // TC23
	void tc23_emptyDataNotUsedToUpdate() {
		assertFalse(dao.editRow("customer", providerOf(new LinkedHashMap<>()), 1));
		assertTrue(output().contains("No data to update"));
	}

	@Test // TC24
	void tc24_nullDataNotUsedToUpdate() {
		assertFalse(dao.editRow("customer", providerOf(null), 1));
		assertTrue(output().contains("No data to update"));
	}

	private Customer sam() {
		return new Customer("Sam", "Ryan", "Athlone", "0871234567");
	}

	@Test // TC25
	void tc25_existingCustomerUpdated() {
		int id = addCustomer(jane());
		assertTrue(dao.editRow("customer", sam(), id));
	}

	@Test // TC26
	void tc26_rowHoldsNewValuesAfterUpdate() {
		int id = addCustomer(jane());
		dao.editRow("customer", sam(), id);
		LinkedHashMap<String, Object> row = findRow(id);
		assertEquals("Sam", get(row, "firstName"));
		assertEquals("Ryan", get(row, "surName"));
		assertEquals("Athlone", get(row, "address"));
		assertEquals("0871234567", get(row, "contact"));
	}

	@Test // TC27
	void tc27_otherRowsNotChangedByUpdate() {
		int id1 = addCustomer(jane());
		int id2 = addCustomer(jane());
		dao.editRow("customer", sam(), id2);
		LinkedHashMap<String, Object> other = findRow(id1);
		assertEquals("Jane", get(other, "firstName"));
		assertEquals("Doe", get(other, "surName"));
		assertEquals("12 High Street", get(other, "address"));
	}

	@Test // TC28
	void tc28_rowCountUnchangedByUpdate() {
		int id = addCustomer(jane());
		int before = dao.readTable("customer").size();
		dao.editRow("customer", sam(), id);
		assertEquals(before, dao.readTable("customer").size());
	}

	@Test // TC29
	void tc29_updatingMissingIdReturnsFalse() {
		int missing = nonExistentId();
		assertFalse(dao.editRow("customer", sam(), missing));
		assertTrue(output().contains("No customer with id " + missing + " exists"));
	}

	@Test
	@Disabled("TC30 - manual: stop the MySQL server, then run. Expect false and a stack trace.")
	void tc30_updateFailsWhenDatabaseUnreachable() {
		assertFalse(dao.editRow("customer", sam(), 3));
	}

	// ====================== DELETE (DAO.deleteRow) ======================

	@Test // TC31
	void tc31_deleteAllowedTableNameAccepted() {
		assertDoesNotThrow(() -> dao.checkForAllowedTables("customer"));
	}

	@Test // TC32
	void tc32_deleteInvalidTableNameRejected() {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> dao.deleteRow("users", 1));
		assertEquals("Invalid table name: users", ex.getMessage());
	}

	@Test // TC33
	void tc33_existingCustomerDeleted() {
		int id = addCustomer(jane());
		assertTrue(dao.deleteRow("customer", id));
	}

	@Test // TC34
	void tc34_deletedRowNoLongerInTable() {
		int id = addCustomer(jane());
		dao.deleteRow("customer", id);
		assertNull(findRow(id));
	}

	@Test // TC35
	void tc35_rowCountDecreasesByOne() {
		int id = addCustomer(jane());
		int before = dao.readTable("customer").size();
		dao.deleteRow("customer", id);
		assertEquals(before - 1, dao.readTable("customer").size());
	}

	@Test // TC36
	void tc36_otherRowsNotDeleted() {
		int id1 = addCustomer(jane());
		int id2 = addCustomer(sam());
		dao.deleteRow("customer", id1);
		LinkedHashMap<String, Object> remaining = findRow(id2);
		assertNotNull(remaining);
		assertEquals("Sam", get(remaining, "firstName"));
		assertEquals("Ryan", get(remaining, "surName"));
	}

	@Test // TC37
	void tc37_deletingMissingIdReturnsFalse() {
		int missing = nonExistentId();
		assertFalse(dao.deleteRow("customer", missing));
		// message in DAO.deleteRow is spelled "exits"
		assertTrue(output().contains("No customer with id " + missing + " exits"));
	}

	@Test // TC38
	void tc38_deletingSameIdTwiceFailsSecondTime() {
		int id = addCustomer(jane());
		assertTrue(dao.deleteRow("customer", id));
		assertFalse(dao.deleteRow("customer", id));
	}

	@Test
	@Disabled("TC39 - manual: stop the MySQL server, then run. Expect false and a stack trace.")
	void tc39_deleteFailsWhenDatabaseUnreachable() {
		assertFalse(dao.deleteRow("customer", 1));
	}

	// ====================== Integration / End to End ======================

	@Test // TC45
	void tc45_createReadEditDeleteInOrder() {
		Set<Integer> before = currentIds();

		assertTrue(dao.addData("customer", jane()));
		Set<Integer> afterAdd = currentIds();
		afterAdd.removeAll(before);
		assertEquals(1, afterAdd.size());
		int id = afterAdd.iterator().next();
		createdIds.add(id);
		assertNotNull(findRow(id));

		assertTrue(dao.editRow("customer", sam(), id));
		assertEquals("Sam", get(findRow(id), "firstName"));

		assertTrue(dao.deleteRow("customer", id));
		assertNull(findRow(id));
	}

	@Test // TC46
	void tc46_savedCustomerReadBackUnchanged() {
		Customer c = jane();
		int id = addCustomer(c);
		LinkedHashMap<String, Object> row = findRow(id);
		assertEquals(c.getFirstName(), get(row, "firstName"));
		assertEquals(c.getSurname(), get(row, "surName"));
		assertEquals(c.getAddress(), get(row, "address"));
		assertEquals(c.getcontact(), get(row, "contact"));
	}

	@Test // TC47
	void tc47_daoHandlesSeveralCustomers() {
		int id1 = addCustomer(new Customer("One", "A", "Addr1", "0871111111"));
		int id2 = addCustomer(new Customer("Two", "B", "Addr2", "0872222222"));
		int id3 = addCustomer(new Customer("Three", "C", "Addr3", "0873333333"));

		assertTrue(dao.editRow("customer", new Customer("Two-Edited", "B", "Addr2", "0872222222"), id2));
		assertTrue(dao.deleteRow("customer", id1));

		Set<Integer> ids = currentIds();
		assertFalse(ids.contains(id1));
		assertTrue(ids.contains(id2));
		assertTrue(ids.contains(id3));
		assertEquals("Two-Edited", get(findRow(id2), "firstName"));
		assertEquals("Three", get(findRow(id3), "firstName"));
	}

	// ---------- helper ----------

	private int nonExistentId() {
		int max = 0;
		for (int id : currentIds()) {
			max = Math.max(max, id);
		}
		return max + 100000;
	}
}
