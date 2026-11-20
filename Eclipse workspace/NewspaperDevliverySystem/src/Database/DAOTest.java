package Database;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import DeliveryArea.DeliveryArea;

// "Database" sheet, DAO part (TC01 - TC39, TC45 - TC47).
// Requires MySQL running on localhost:3306 with the newspapersystemdatabase schema
// (databaseSetup.sql) and root/root.
// The tests use the deliveryAreas table because nothing has to exist before a row can be added.
// Existing data is never wiped: every row a test adds is removed again in tearDown,
// and row counts are compared with the count before the test.
public class DAOTest {

    private static final String TABLE = "deliveryAreas";
    private static final String ID = "deliveryAreaId";

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
            dao.deleteRow(TABLE, id);
        }
        createdIds.clear();
    }

    private DeliveryArea athlone() {
        return new DeliveryArea(0, "DAOTest Athlone");
    }

    private DeliveryArea moate() {
        return new DeliveryArea(0, "DAOTest Moate");
    }

    // inserts a row and remembers it so tearDown removes it
    private int addArea(DeliveryArea area) {
        int id = dao.addData(TABLE, area);
        assertTrue(id > 0, "setup insert should succeed");
        createdIds.add(id);
        return id;
    }

    private LinkedHashMap<String, Object> findRow(int id) {
        for (LinkedHashMap<String, Object> row : dao.readTable(TABLE)) {
            if (((Number) row.get(ID)).intValue() == id) {
                return row;
            }
        }
        return null;
    }

    private int nonExistentId() {
        int max = 0;
        for (LinkedHashMap<String, Object> row : dao.readTable(TABLE)) {
            max = Math.max(max, ((Number) row.get(ID)).intValue());
        }
        return max + 100000;
    }

    private static DataProvider providerOf(LinkedHashMap<String, Object> data) {
        return () -> data;
    }

    // ====================== CREATE (DAO.addData) ======================

    // TC01
    // Test Objective: Verify an allowed table name is accepted.
    // Input(s): tableName = "deliveryAreas"
    // Expected Output(s): No exception thrown
    @Test
    void allowedTableNameAccepted_TC01() {
        assertDoesNotThrow(() -> dao.checkForAllowedTables(TABLE));
    }

    // TC02
    // Test Objective: Verify a table name that is not allowed is rejected.
    // Input(s): tableName = "holidays"
    // Expected Output(s): IllegalArgumentException: Invalid table name: holidays
    @Test
    void invalidTableNameRejected_TC02() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> dao.addData("holidays", athlone()));
        assertEquals("Invalid table name: holidays", ex.getMessage());
    }

    // TC03
    // Test Objective: Verify an empty table name is rejected.
    // Input(s): tableName = ""
    // Expected Output(s): IllegalArgumentException: Invalid table name: 
    @Test
    void emptyTableNameRejected_TC03() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> dao.addData("", athlone()));
        assertEquals("Invalid table name: ", ex.getMessage());
    }

    // TC04
    // Test Objective: Verify table names are case sensitive.
    // Input(s): tableName = "DeliveryAreas"
    // Expected Output(s): IllegalArgumentException: Invalid table name: DeliveryAreas
    @Test
    void tableNamesAreCaseSensitive_TC04() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> dao.addData("DeliveryAreas", athlone()));
        assertEquals("Invalid table name: DeliveryAreas", ex.getMessage());
    }

    // TC05
    // Test Objective: Verify an entity with empty data is not inserted.
    // Input(s): tableName = "deliveryAreas", entity.getData() = {}
    // Expected Output(s): Returns -1; "No data to insert" printed; row count unchanged
    @Test
    void emptyDataNotInserted_TC05() {
        int before = dao.readTable(TABLE).size();
        assertEquals(-1, dao.addData(TABLE, providerOf(new LinkedHashMap<>())));
        assertTrue(console.toString().contains("No data to insert"));
        assertEquals(before, dao.readTable(TABLE).size());
    }

    // TC06
    // Test Objective: Verify an entity with null data is not inserted.
    // Input(s): tableName = "deliveryAreas", entity.getData() = null
    // Expected Output(s): Returns -1; "No data to insert" printed
    @Test
    void nullDataNotInserted_TC06() {
        assertEquals(-1, dao.addData(TABLE, providerOf(null)));
        assertTrue(console.toString().contains("No data to insert"));
    }

    // TC07
    // Test Objective: Verify a valid delivery area is inserted.
    // Input(s): tableName = "deliveryAreas", DeliveryArea: location = "DAOTest Athlone"
    // Expected Output(s): Returns the new deliveryAreaId (greater than 0)
    @Test
    void validRowInserted_TC07() {
        int id = dao.addData(TABLE, athlone());
        createdIds.add(id);
        assertTrue(id > 0);
    }

    // TC08
    // Test Objective: Verify the row count increases by one after insert.
    // Input(s): Insert DeliveryArea: location = "DAOTest Athlone"
    // Expected Output(s): readTable returns the previous row count + 1
    @Test
    void rowCountIncreasesByOne_TC08() {
        int before = dao.readTable(TABLE).size();
        addArea(athlone());
        assertEquals(before + 1, dao.readTable(TABLE).size());
    }

    // TC09
    // Test Objective: Verify the inserted values match the entity.
    // Input(s): Insert DeliveryArea: location = "DAOTest Athlone", then readTable
    // Expected Output(s): New row: location = "DAOTest Athlone"
    @Test
    void insertedValuesMatchEntity_TC09() {
        int id = addArea(athlone());
        LinkedHashMap<String, Object> row = findRow(id);
        assertNotNull(row);
        assertEquals("DAOTest Athlone", row.get("location"));
    }

    // TC10
    // Test Objective: Verify a new deliveryAreaId is generated for each inserted row.
    // Input(s): Insert the same delivery area twice
    // Expected Output(s): The two ids are different
    @Test
    void newIdGeneratedForEachRow_TC10() {
        int id1 = addArea(athlone());
        int id2 = addArea(athlone());
        assertNotEquals(id1, id2);
    }

    // TC11
    // Test Objective: Verify insert returns -1 when the database cannot be reached.
    // Input(s): Valid delivery area, MySQL server stopped (manual test)
    // Expected Output(s): Returns -1; stack trace printed
    @Test
    @Disabled("TC11 - manual: stop the MySQL server, then run. Expect -1 and a stack trace.")
    void insertFailsWhenDatabaseUnreachable_TC11() {
        assertEquals(-1, dao.addData(TABLE, athlone()));
    }

    // TC12
    // Test Objective: Verify insert returns -1 when a value breaks a column constraint.
    // Input(s): location = 300 characters (column is VARCHAR(150))
    // Expected Output(s): Returns -1; stack trace printed; row count unchanged
    @Test
    void insertFailsOnColumnConstraint_TC12() {
        int before = dao.readTable(TABLE).size();
        int id = dao.addData(TABLE, new DeliveryArea(0, "x".repeat(300)));
        if (id > 0) createdIds.add(id);
        assertEquals(-1, id);
        assertEquals(before, dao.readTable(TABLE).size());
    }

    // ====================== READ (DAO.readTable) ======================

    // TC13
    // Test Objective: Verify an allowed table name is accepted.
    // Input(s): tableName = "deliveryAreas"
    // Expected Output(s): No exception thrown
    @Test
    void readAllowedTableNameAccepted_TC13() {
        assertDoesNotThrow(() -> dao.readTable(TABLE));
    }

    // TC14
    // Test Objective: Verify a table name that is not allowed is rejected.
    // Input(s): tableName = "holidays"
    // Expected Output(s): IllegalArgumentException: Invalid table name: holidays
    @Test
    void readInvalidTableNameRejected_TC14() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> dao.readTable("holidays"));
        assertEquals("Invalid table name: holidays", ex.getMessage());
    }

    // TC15
    // Test Objective: Verify all rows in the table are returned.
    // Input(s): Insert 3 delivery areas
    // Expected Output(s): readTable returns the previous row count + 3
    @Test
    void allRowsReturned_TC15() {
        int before = dao.readTable(TABLE).size();
        addArea(athlone());
        addArea(athlone());
        addArea(athlone());
        assertEquals(before + 3, dao.readTable(TABLE).size());
    }

    // TC16
    // Test Objective: Verify each row contains every column.
    // Input(s): deliveryAreas table has rows
    // Expected Output(s): Each row has keys deliveryAreaId and location
    @Test
    void eachRowContainsEveryColumn_TC16() {
        addArea(athlone());
        for (LinkedHashMap<String, Object> row : dao.readTable(TABLE)) {
            assertEquals(2, row.size());
            assertTrue(row.containsKey(ID));
            assertTrue(row.containsKey("location"));
        }
    }

    // TC17
    // Test Objective: Verify the row values are correct.
    // Input(s): Insert DeliveryArea: location = "DAOTest Athlone"
    // Expected Output(s): row.get("deliveryAreaId") = new id, row.get("location") = "DAOTest Athlone"
    @Test
    void rowValuesAreCorrect_TC17() {
        int id = addArea(athlone());
        LinkedHashMap<String, Object> row = findRow(id);
        assertEquals(id, ((Number) row.get(ID)).intValue());
        assertEquals("DAOTest Athlone", row.get("location"));
    }

    // TC18
    // Test Objective: Verify the columns of each row are returned in table column order.
    // Input(s): deliveryAreas table has rows
    // Expected Output(s): Keys of each row are [deliveryAreaId, location]
    @Test
    void columnsReturnedInTableOrder_TC18() {
        addArea(athlone());
        for (LinkedHashMap<String, Object> row : dao.readTable(TABLE)) {
            assertEquals(List.of(ID, "location"), new ArrayList<>(row.keySet()));
        }
    }

    // TC19
    // Test Objective: Verify an empty table returns an empty list.
    // Input(s): deliveryAreas table has 0 rows (skipped if the table has data)
    // Expected Output(s): Empty list returned (size 0)
    @Test
    void emptyTableReturnsEmptyList_TC19() {
        assumeTrue(dao.readTable(TABLE).isEmpty(), "deliveryAreas table is not empty - skipped");
        assertEquals(0, dao.readTable(TABLE).size());
    }

    // TC20
    // Test Objective: Verify read returns an empty list when the database cannot be reached.
    // Input(s): MySQL server stopped (manual test)
    // Expected Output(s): Empty list returned; stack trace printed
    @Test
    @Disabled("TC20 - manual: stop the MySQL server, then run. Expect an empty list and a stack trace.")
    void readReturnsEmptyListWhenDatabaseUnreachable_TC20() {
        assertTrue(dao.readTable(TABLE).isEmpty());
    }

    // ====================== EDIT (DAO.editRow) ======================

    // TC21
    // Test Objective: Verify an allowed table name is accepted.
    // Input(s): tableName = "deliveryAreas"
    // Expected Output(s): No exception thrown
    @Test
    void editAllowedTableNameAccepted_TC21() {
        assertDoesNotThrow(() -> dao.checkForAllowedTables(TABLE));
    }

    // TC22
    // Test Objective: Verify a table name that is not allowed is rejected.
    // Input(s): tableName = "holidays", id = 1
    // Expected Output(s): IllegalArgumentException: Invalid table name: holidays
    @Test
    void editInvalidTableNameRejected_TC22() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> dao.editRow("holidays", athlone(), 1));
        assertEquals("Invalid table name: holidays", ex.getMessage());
    }

    // TC23
    // Test Objective: Verify an entity with empty data is not used to update.
    // Input(s): tableName = "deliveryAreas", entity.getData() = {}, id = 1
    // Expected Output(s): Returns false; "No data to update" printed
    @Test
    void emptyDataNotUsedToUpdate_TC23() {
        assertFalse(dao.editRow(TABLE, providerOf(new LinkedHashMap<>()), 1));
        assertTrue(console.toString().contains("No data to update"));
    }

    // TC24
    // Test Objective: Verify an entity with null data is not used to update.
    // Input(s): tableName = "deliveryAreas", entity.getData() = null, id = 1
    // Expected Output(s): Returns false; "No data to update" printed
    @Test
    void nullDataNotUsedToUpdate_TC24() {
        assertFalse(dao.editRow(TABLE, providerOf(null), 1));
        assertTrue(console.toString().contains("No data to update"));
    }

    // TC25
    // Test Objective: Verify an existing delivery area is updated.
    // Input(s): Existing row, DeliveryArea: location = "DAOTest Moate"
    // Expected Output(s): Returns true
    @Test
    void existingRowUpdated_TC25() {
        int id = addArea(athlone());
        assertTrue(dao.editRow(TABLE, moate(), id));
    }

    // TC26
    // Test Objective: Verify the row holds the new values after update.
    // Input(s): Update the row as above, then readTable
    // Expected Output(s): Row: location = "DAOTest Moate"
    @Test
    void rowHoldsNewValuesAfterUpdate_TC26() {
        int id = addArea(athlone());
        dao.editRow(TABLE, moate(), id);
        assertEquals("DAOTest Moate", findRow(id).get("location"));
    }

    // TC27
    // Test Objective: Verify other rows are not changed by an update.
    // Input(s): Insert 2 rows, update the second, then readTable
    // Expected Output(s): First row still has location = "DAOTest Athlone"
    @Test
    void otherRowsNotChangedByUpdate_TC27() {
        int id1 = addArea(athlone());
        int id2 = addArea(athlone());
        dao.editRow(TABLE, moate(), id2);
        assertEquals("DAOTest Athlone", findRow(id1).get("location"));
    }

    // TC28
    // Test Objective: Verify the row count is unchanged by an update.
    // Input(s): Update an existing row
    // Expected Output(s): readTable returns the same row count
    @Test
    void rowCountUnchangedByUpdate_TC28() {
        int id = addArea(athlone());
        int before = dao.readTable(TABLE).size();
        dao.editRow(TABLE, moate(), id);
        assertEquals(before, dao.readTable(TABLE).size());
    }

    // TC29
    // Test Objective: Verify updating an id that does not exist returns false.
    // Input(s): id not in the table, valid delivery area
    // Expected Output(s): Returns false; "No deliveryAreas with id <id> exists" printed
    @Test
    void updatingMissingIdReturnsFalse_TC29() {
        int missing = nonExistentId();
        assertFalse(dao.editRow(TABLE, moate(), missing));
        assertTrue(console.toString().contains("No " + TABLE + " with id " + missing + " exists"));
    }

    // TC30
    // Test Objective: Verify update returns false when the database cannot be reached.
    // Input(s): Valid delivery area, MySQL server stopped (manual test)
    // Expected Output(s): Returns false; stack trace printed
    @Test
    @Disabled("TC30 - manual: stop the MySQL server, then run. Expect false and a stack trace.")
    void updateFailsWhenDatabaseUnreachable_TC30() {
        assertFalse(dao.editRow(TABLE, moate(), 1));
    }

    // ====================== DELETE (DAO.deleteRow) ======================

    // TC31
    // Test Objective: Verify an allowed table name is accepted.
    // Input(s): tableName = "deliveryAreas"
    // Expected Output(s): No exception thrown
    @Test
    void deleteAllowedTableNameAccepted_TC31() {
        assertDoesNotThrow(() -> dao.checkForAllowedTables(TABLE));
    }

    // TC32
    // Test Objective: Verify a table name that is not allowed is rejected.
    // Input(s): tableName = "holidays", id = 1
    // Expected Output(s): IllegalArgumentException: Invalid table name: holidays
    @Test
    void deleteInvalidTableNameRejected_TC32() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> dao.deleteRow("holidays", 1));
        assertEquals("Invalid table name: holidays", ex.getMessage());
    }

    // TC33
    // Test Objective: Verify an existing delivery area is deleted.
    // Input(s): tableName = "deliveryAreas", id of an inserted row
    // Expected Output(s): Returns true
    @Test
    void existingRowDeleted_TC33() {
        int id = addArea(athlone());
        assertTrue(dao.deleteRow(TABLE, id));
    }

    // TC34
    // Test Objective: Verify the deleted row is no longer in the table.
    // Input(s): Delete an inserted row, then readTable
    // Expected Output(s): No row with that deliveryAreaId returned
    @Test
    void deletedRowNoLongerInTable_TC34() {
        int id = addArea(athlone());
        dao.deleteRow(TABLE, id);
        assertNull(findRow(id));
    }

    // TC35
    // Test Objective: Verify the row count decreases by one after delete.
    // Input(s): Delete an inserted row
    // Expected Output(s): readTable returns the previous row count - 1
    @Test
    void rowCountDecreasesByOne_TC35() {
        int id = addArea(athlone());
        int before = dao.readTable(TABLE).size();
        dao.deleteRow(TABLE, id);
        assertEquals(before - 1, dao.readTable(TABLE).size());
    }

    // TC36
    // Test Objective: Verify other rows are not deleted.
    // Input(s): Insert 2 rows, delete the first, then readTable
    // Expected Output(s): Second row still returned with location = "DAOTest Moate"
    @Test
    void otherRowsNotDeleted_TC36() {
        int id1 = addArea(athlone());
        int id2 = addArea(moate());
        dao.deleteRow(TABLE, id1);
        LinkedHashMap<String, Object> remaining = findRow(id2);
        assertNotNull(remaining);
        assertEquals("DAOTest Moate", remaining.get("location"));
    }

    // TC37
    // Test Objective: Verify deleting an id that does not exist returns false.
    // Input(s): id not in the table
    // Expected Output(s): Returns false; "No deliveryAreas with id <id> exists" printed
    @Test
    void deletingMissingIdReturnsFalse_TC37() {
        int missing = nonExistentId();
        assertFalse(dao.deleteRow(TABLE, missing));
        assertTrue(console.toString().contains("No " + TABLE + " with id " + missing + " exists"));
    }

    // TC38
    // Test Objective: Verify deleting the same id twice returns false the second time.
    // Input(s): Delete the same id twice
    // Expected Output(s): First call true, second call false
    @Test
    void deletingSameIdTwiceFailsSecondTime_TC38() {
        int id = addArea(athlone());
        assertTrue(dao.deleteRow(TABLE, id));
        assertFalse(dao.deleteRow(TABLE, id));
    }

    // TC39
    // Test Objective: Verify delete returns false when the database cannot be reached.
    // Input(s): id = 1, MySQL server stopped (manual test)
    // Expected Output(s): Returns false; stack trace printed
    @Test
    @Disabled("TC39 - manual: stop the MySQL server, then run. Expect false and a stack trace.")
    void deleteFailsWhenDatabaseUnreachable_TC39() {
        assertFalse(dao.deleteRow(TABLE, 1));
    }

    // ====================== Integration / End to End ======================

    // TC45
    // Test Objective: Verify a delivery area can be created, read, edited and deleted in order.
    // Input(s): addData("DAOTest Athlone") -> readTable -> editRow(id, "DAOTest Moate") -> readTable -> deleteRow(id) -> readTable
    // Expected Output(s): Each call succeeds; row appears after add, shows the new location after edit, and is gone after delete
    @Test
    void createReadEditDeleteInOrder_TC45() {
        int id = dao.addData(TABLE, athlone());
        assertTrue(id > 0);
        createdIds.add(id);
        assertNotNull(findRow(id));

        assertTrue(dao.editRow(TABLE, moate(), id));
        assertEquals("DAOTest Moate", findRow(id).get("location"));

        assertTrue(dao.deleteRow(TABLE, id));
        assertNull(findRow(id));
    }

    // TC46
    // Test Objective: Verify a DeliveryArea object saved with addData is read back unchanged.
    // Input(s): DeliveryArea: location = "DAOTest Athlone"
    // Expected Output(s): DeliveryArea built from the row has the new id and the same location
    @Test
    void savedRowReadBackUnchanged_TC46() {
        DeliveryArea area = athlone();
        int id = addArea(area);
        DeliveryArea readBack = new DeliveryArea(findRow(id));
        assertEquals(id, readBack.getDeliveryAreaId());
        assertEquals(area.getLocation(), readBack.getLocation());
    }

    // TC47
    // Test Objective: Verify the DAO handles several delivery areas correctly.
    // Input(s): Add 3 delivery areas, edit the second, delete the first
    // Expected Output(s): First row gone, second row edited, third row unchanged
    @Test
    void daoHandlesSeveralRows_TC47() {
        int id1 = addArea(new DeliveryArea(0, "DAOTest One"));
        int id2 = addArea(new DeliveryArea(0, "DAOTest Two"));
        int id3 = addArea(new DeliveryArea(0, "DAOTest Three"));

        assertTrue(dao.editRow(TABLE, new DeliveryArea(0, "DAOTest Two Edited"), id2));
        assertTrue(dao.deleteRow(TABLE, id1));

        assertNull(findRow(id1));
        assertEquals("DAOTest Two Edited", findRow(id2).get("location"));
        assertEquals("DAOTest Three", findRow(id3).get("location"));
    }
}
