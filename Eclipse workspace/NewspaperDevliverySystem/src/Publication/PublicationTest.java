package Publication;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PublicationTest {

    // TC01
    // Test Objective: Verify a one-character publication name is accepted
    // Input(s): Name = "A"
    // Expected Output(s): Name accepted
    @Test
    void validNameOneCharacter_TC01() {
        Publication publication = new Publication();
        assertTrue(publication.verifyName("A"));
    }

    // TC02
    // Test Objective: Verify a longer publication name is accepted
    // Input(s): Name = "Daily News"
    // Expected Output(s): Name accepted
    @Test
    void validNameMultipleCharacters_TC02() {
        Publication publication = new Publication();
        assertTrue(publication.verifyName("Daily News"));
    }

    // TC03
    // Test Objective: Verify a blank publication name is rejected
    // Input(s): Name = ""
    // Expected Output(s): Invalid name
    @Test
    void invalidNameEmpty_TC03() {
        Publication publication = new Publication();
        assertFalse(publication.verifyName(""));
    }

    // TC04
    // Test Objective: Verify an omitted publication name is rejected
    // Input(s): Name = null
    // Expected Output(s): Invalid name
    @Test
    void invalidNameNull_TC04() {
        Publication publication = new Publication();
        assertFalse(publication.verifyName(null));
    }

    // TC05
    // Test Objective: Verify a publication type is accepted
    // Input(s): Type = "Newspaper"
    // Expected Output(s): Type accepted
    @Test
    void validType_TC05() {
        Publication publication = new Publication();
        assertTrue(publication.verifyType("Newspaper"));
    }

    // TC06
    // Test Objective: Verify an empty publication type is rejected
    // Input(s): Type = ""
    // Expected Output(s): Invalid type
    @Test
    void invalidTypeEmpty_TC06() {
        Publication publication = new Publication();
        assertFalse(publication.verifyType(""));
    }

    // TC07
    // Test Objective: Verify a whitespace-only type is rejected
    // Input(s): Type = "   "
    // Expected Output(s): Invalid type
    @Test
    void invalidTypeSpaces_TC07() {
        Publication publication = new Publication();
        assertFalse(publication.verifyType("   "));
    }

    // TC08
    // Test Objective: Verify a minimum positive cost is accepted
    // Input(s): Cost = 1
    // Expected Output(s): Cost accepted
    @Test
    void validCostOne_TC08() {
        Publication publication = new Publication();
        assertTrue(publication.verifyCost(1));
    }

    // TC09
    // Test Objective: Verify a larger positive cost is accepted
    // Input(s): Cost = 150
    // Expected Output(s): Cost accepted
    @Test
    void validCostMultiple_TC09() {
        Publication publication = new Publication();
        assertTrue(publication.verifyCost(150));
    }

    // TC10
    // Test Objective: Verify an omitted cost is rejected
    // Input(s): Cost = null
    // Expected Output(s): Invalid cost
    @Test
    void invalidCostNull_TC10() {
        Publication publication = new Publication();
        assertFalse(publication.verifyCost(null));
    }

    // TC11
    // Test Objective: Verify a zero cost is rejected
    // Input(s): Cost = 0
    // Expected Output(s): Invalid cost
    @Test
    void invalidCostZero_TC11() {
        Publication publication = new Publication();
        assertFalse(publication.verifyCost(0));
    }

    // TC12
    // Test Objective: Verify a negative cost is rejected
    // Input(s): Cost = -1
    // Expected Output(s): Invalid cost
    @Test
    void invalidCostNegative_TC12() {
        Publication publication = new Publication();
        assertFalse(publication.verifyCost(-1));
    }

    // TC13
    // Test Objective: Verify zero stock is accepted
    // Input(s): Stock = 0
    // Expected Output(s): Stock accepted
    @Test
    void validStockZero_TC13() {
        Publication publication = new Publication();
        assertTrue(publication.verifyStock(0));
    }

    // TC14
    // Test Objective: Verify a positive stock count is accepted
    // Input(s): Stock = 20
    // Expected Output(s): Stock accepted
    @Test
    void validStockPositive_TC14() {
        Publication publication = new Publication();
        assertTrue(publication.verifyStock(20));
    }

    // TC15
    // Test Objective: Verify an omitted stock count is rejected
    // Input(s): Stock = null
    // Expected Output(s): Invalid stock
    @Test
    void invalidStockNull_TC15() {
        Publication publication = new Publication();
        assertFalse(publication.verifyStock(null));
    }

    // TC16
    // Test Objective: Verify negative stock is rejected
    // Input(s): Stock = -1
    // Expected Output(s): Invalid stock
    @Test
    void invalidStockNegative_TC16() {
        Publication publication = new Publication();
        assertFalse(publication.verifyStock(-1));
    }

    // TC17
    // Test Objective: Verify successful save is accepted
    // Input(s): Save successful = true
    // Expected Output(s): Save accepted
    @Test
    void validSave_TC17() {
        Publication publication = new Publication();
        assertTrue(publication.verifySave(true));
    }

    // TC18
    // Test Objective: Verify failed save is rejected
    // Input(s): Save successful = false
    // Expected Output(s): Save rejected
    @Test
    void invalidSave_TC18() {
        Publication publication = new Publication();
        assertFalse(publication.verifySave(false));
    }

    // TC19
    // Test Objective: Verify a publication is created with valid details
    // Input(s): ID 201; Daily News; Newspaper; Cost 3; Stock 10
    // Expected Output(s): Publication created
    @Test
    void createValidPublication_TC19() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 10, true, true));
    }

    // TC20
    // Test Objective: Verify creation fails with blank name
    // Input(s): Name = ""
    // Expected Output(s): Publication not created
    @Test
    void createInvalidName_TC20() {
        Publication publication = new Publication();
        assertFalse(publication.createPublication(201, "", "Newspaper", 3, 10, true, true));
    }

    // TC21
    // Test Objective: Verify creation fails with blank type
    // Input(s): Type = ""
    // Expected Output(s): Publication not created
    @Test
    void createInvalidType_TC21() {
        Publication publication = new Publication();
        assertFalse(publication.createPublication(201, "Daily News", "", 3, 10, true, true));
    }

    // TC22
    // Test Objective: Verify creation fails with invalid cost
    // Input(s): Cost = 0
    // Expected Output(s): Publication not created
    @Test
    void createInvalidCost_TC22() {
        Publication publication = new Publication();
        assertFalse(publication.createPublication(201, "Daily News", "Newspaper", 0, 10, true, true));
    }

    // TC23
    // Test Objective: Verify creation fails with missing stock
    // Input(s): Stock = null
    // Expected Output(s): Publication not created
    @Test
    void createMissingStock_TC23() {
        Publication publication = new Publication();
        assertFalse(publication.createPublication(201, "Daily News", "Newspaper", 3, null, true, true));
    }

    // TC24
    // Test Objective: Verify creation fails with negative stock
    // Input(s): Stock = -1
    // Expected Output(s): Publication not created
    @Test
    void createNegativeStock_TC24() {
        Publication publication = new Publication();
        assertFalse(publication.createPublication(201, "Daily News", "Newspaper", 3, -1, true, true));
    }

    // TC25
    // Test Objective: Verify creation fails when storage reports failure
    // Input(s): Save successful = false
    // Expected Output(s): Publication not created
    @Test
    void createSaveFailure_TC25() {
        Publication publication = new Publication();
        assertFalse(publication.createPublication(201, "Daily News", "Newspaper", 3, 10, true, false));
    }

    // TC26
    // Test Objective: Verify duplicate publication ID is rejected
    // Input(s): ID 201 already exists
    // Expected Output(s): Second publication not created
    @Test
    void createDuplicateID_TC26() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 10, true, true));
        assertFalse(publication.createPublication(201, "Weekend", "Magazine", 5, 8, true, true));
    }

    // TC27
    // Test Objective: Verify missing cost prevents creation
    // Input(s): Cost = null
    // Expected Output(s): Publication not created
    @Test
    void createMissingCost_TC27() {
        Publication publication = new Publication();
        assertFalse(publication.createPublication(201, "Daily News", "Newspaper", null, 10, true, true));
    }

    // TC28
    // Test Objective: Verify existing publication can be retrieved
    // Input(s): Publication ID = 201
    // Expected Output(s): Publication returned
    @Test
    void readExistingPublication_TC28() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 10, true, true));
        assertNotNull(publication.readPublication(201));
    }

    // TC29
    // Test Objective: Verify all publication fields are retrieved correctly
    // Input(s): ID 201; Daily News; Newspaper; 3; 10
    // Expected Output(s): Matching ID, name, type, cost and stock
    @Test
    void readAllFields_TC29() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 10, true, true));
        Publication result = publication.readPublication(201);
        assertEquals(201, result.getPublicationID());
        assertEquals("Daily News", result.getName());
        assertEquals("Newspaper", result.getType());
        assertEquals(3, result.getCost());
        assertEquals(10, result.getStock());
    }

    // TC30
    // Test Objective: Verify a non-existing publication cannot be retrieved
    // Input(s): Publication ID = 999
    // Expected Output(s): No publication returned
    @Test
    void readMissingPublication_TC30() {
        Publication publication = new Publication();
        assertNull(publication.readPublication(999));
    }

    // TC31
    // Test Objective: Verify publication details reject missing name
    // Input(s): Name = null
    // Expected Output(s): Invalid details
    @Test
    void readRejectMissingName_TC31() {
        Publication publication = new Publication();
        assertFalse(publication.verifyDetails(null, "Newspaper", 3, 10));
    }

    // TC32
    // Test Objective: Verify publication details reject missing type
    // Input(s): Type = null
    // Expected Output(s): Invalid details
    @Test
    void readRejectMissingType_TC32() {
        Publication publication = new Publication();
        assertFalse(publication.verifyDetails("Daily News", null, 3, 10));
    }

    // TC33
    // Test Objective: Verify publication details reject missing cost or stock
    // Input(s): Cost = null / Stock = null
    // Expected Output(s): Invalid details
    @Test
    void readRejectMissingNumbers_TC33() {
        Publication publication = new Publication();
        assertFalse(publication.verifyDetails("Daily News", "Newspaper", null, 10));
        assertFalse(publication.verifyDetails("Daily News", "Newspaper", 3, null));
    }

    // TC34
    // Test Objective: Verify an existing publication can be updated
    // Input(s): ID 201; new name = Daily Post
    // Expected Output(s): Publication updated
    @Test
    void updateExistingPublication_TC34() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 10, true, true));
        assertTrue(publication.updatePublication(201, "Daily Post", "Newspaper", 3, 10, true));
    }

    // TC35
    // Test Objective: Verify a missing publication cannot be updated
    // Input(s): ID = 999
    // Expected Output(s): Publication not updated
    @Test
    void updateMissingPublication_TC35() {
        Publication publication = new Publication();
        assertFalse(publication.updatePublication(999, "Daily Post", "Newspaper", 3, 10, true));
    }

    // TC36
    // Test Objective: Verify update rejects empty name
    // Input(s): New name = ""
    // Expected Output(s): Publication not updated
    @Test
    void updateInvalidName_TC36() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 10, true, true));
        assertFalse(publication.updatePublication(201, "", "Newspaper", 3, 10, true));
    }

    // TC37
    // Test Objective: Verify update rejects missing type
    // Input(s): New type = null
    // Expected Output(s): Publication not updated
    @Test
    void updateMissingType_TC37() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 10, true, true));
        assertFalse(publication.updatePublication(201, "Daily News", null, 3, 10, true));
    }

    // TC38
    // Test Objective: Verify update rejects missing cost
    // Input(s): New cost = null
    // Expected Output(s): Publication not updated
    @Test
    void updateMissingCost_TC38() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 10, true, true));
        assertFalse(publication.updatePublication(201, "Daily News", "Newspaper", null, 10, true));
    }

    // TC39
    // Test Objective: Verify update rejects zero or negative cost
    // Input(s): New cost = 0 / -2
    // Expected Output(s): Publication not updated
    @Test
    void updateInvalidCost_TC39() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 10, true, true));
        assertFalse(publication.updatePublication(201, "Daily News", "Newspaper", 0, 10, true));
        assertFalse(publication.updatePublication(201, "Daily News", "Newspaper", -2, 10, true));
    }

    // TC40
    // Test Objective: Verify update accepts zero stock
    // Input(s): New stock = 0
    // Expected Output(s): Publication updated
    @Test
    void updateZeroStock_TC40() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 10, true, true));
        assertTrue(publication.updatePublication(201, "Daily News", "Newspaper", 3, 0, true));
        assertEquals(0, publication.readPublication(201).getStock());
    }

    // TC41
    // Test Objective: Verify update rejects missing stock
    // Input(s): New stock = null
    // Expected Output(s): Publication not updated
    @Test
    void updateMissingStock_TC41() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 10, true, true));
        assertFalse(publication.updatePublication(201, "Daily News", "Newspaper", 3, null, true));
    }

    // TC42
    // Test Objective: Verify update rejects negative stock
    // Input(s): New stock = -1
    // Expected Output(s): Publication not updated
    @Test
    void updateNegativeStock_TC42() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 10, true, true));
        assertFalse(publication.updatePublication(201, "Daily News", "Newspaper", 3, -1, true));
    }

    // TC43
    // Test Objective: Verify unsuccessful save leaves publication unchanged
    // Input(s): Update successful = false
    // Expected Output(s): Update rejected; original data retained
    @Test
    void updateSaveFailure_TC43() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 10, true, true));
        assertFalse(publication.updatePublication(201, "Daily Post", "Magazine", 5, 0, false));
        assertEquals("Daily News", publication.readPublication(201).getName());
        assertEquals(10, publication.readPublication(201).getStock());
    }

    // TC44
    // Test Objective: Verify all new fields are stored on a valid update
    // Input(s): New name/type/cost/stock
    // Expected Output(s): All new fields retrieved
    @Test
    void updateAllFields_TC44() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 10, true, true));
        assertTrue(publication.updatePublication(201, "Weekly Review", "Magazine", 7, 4, true));
        Publication result = publication.readPublication(201);
        assertEquals("Weekly Review", result.getName());
        assertEquals("Magazine", result.getType());
        assertEquals(7, result.getCost());
        assertEquals(4, result.getStock());
    }

    // TC45
    // Test Objective: Verify an existing publication can be deleted
    // Input(s): Publication ID 201; delete success = true
    // Expected Output(s): Publication deleted
    @Test
    void deleteExistingPublication_TC45() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 10, true, true));
        assertTrue(publication.deletePublication(201, true));
        assertNull(publication.readPublication(201));
    }

    // TC46
    // Test Objective: Verify invalid publication ID cannot be deleted
    // Input(s): Publication ID = 999
    // Expected Output(s): Publication not deleted
    @Test
    void deleteMissingPublication_TC46() {
        Publication publication = new Publication();
        assertFalse(publication.deletePublication(999, true));
    }

    // TC47
    // Test Objective: Verify failed deletion does not remove publication
    // Input(s): Delete successful = false
    // Expected Output(s): Delete rejected; publication retained
    @Test
    void deleteFailedOperation_TC47() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 10, true, true));
        assertFalse(publication.deletePublication(201, false));
        assertNotNull(publication.readPublication(201));
    }

    // TC48
    // Test Objective: Verify deleting one publication leaves another available
    // Input(s): IDs 201 and 202
    // Expected Output(s): Publication 202 remains
    @Test
    void deleteLeavesOtherPublication_TC48() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 10, true, true));
        assertTrue(publication.createPublication(202, "Weekend", "Magazine", 5, 8, true, true));
        assertTrue(publication.deletePublication(201, true));
        assertNotNull(publication.readPublication(202));
    }

    // TC49
    // Test Objective: Verify zero stock triggers a warning condition
    // Input(s): Stock = 0
    // Expected Output(s): Low stock = true
    @Test
    void lowStockZero_TC49() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 0, true, true));
        assertTrue(publication.isLowStock(201));
    }

    // TC50
    // Test Objective: Verify stock of four triggers a warning condition
    // Input(s): Stock = 4
    // Expected Output(s): Low stock = true
    @Test
    void lowStockFour_TC50() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 4, true, true));
        assertTrue(publication.isLowStock(201));
    }

    // TC51
    // Test Objective: Verify stock of five does not trigger low stock
    // Input(s): Stock = 5
    // Expected Output(s): Low stock = false
    @Test
    void lowStockFive_TC51() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 5, true, true));
        assertFalse(publication.isLowStock(201));
    }

    // TC52
    // Test Objective: Verify higher stock does not trigger low stock
    // Input(s): Stock = 10
    // Expected Output(s): Low stock = false
    @Test
    void lowStockTen_TC52() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 10, true, true));
        assertFalse(publication.isLowStock(201));
    }

    // TC53
    // Test Objective: Verify valid low-stock notification is acknowledged
    // Input(s): Stock = 4; warning sent = true
    // Expected Output(s): Warning accepted
    @Test
    void warningSentSuccessfully_TC53() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 4, true, true));
        assertTrue(publication.sendLowStockWarning(201, true));
    }

    // TC54
    // Test Objective: Verify notification send failure is reported
    // Input(s): Stock = 4; warning sent = false
    // Expected Output(s): Warning rejected
    @Test
    void warningSendFailed_TC54() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 4, true, true));
        assertFalse(publication.sendLowStockWarning(201, false));
    }

    // TC55
    // Test Objective: Verify false warning is rejected and logged
    // Input(s): Stock = 10; warning sent = true
    // Expected Output(s): Warning rejected; error logged
    @Test
    void warningFalseAlarmLogged_TC55() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 10, true, true));
        assertFalse(publication.sendLowStockWarning(201, true));
        assertEquals(1, publication.getErrorLog().size());
    }

    // TC56
    // Test Objective: Verify news agent can access available restock UI
    // Input(s): News agent = true; UI available = true
    // Expected Output(s): UI access permitted
    @Test
    void restockUIAvailable_TC56() {
        Publication publication = new Publication();
        assertTrue(publication.canAccessRestockUI(true, true));
    }

    // TC57
    // Test Objective: Verify unavailable restock UI denies access
    // Input(s): News agent = true; UI available = false
    // Expected Output(s): UI access rejected
    @Test
    void restockUIUnavailable_TC57() {
        Publication publication = new Publication();
        assertFalse(publication.canAccessRestockUI(true, false));
    }

    // TC58
    // Test Objective: Verify unauthorised user cannot access restock UI
    // Input(s): News agent = false
    // Expected Output(s): UI access rejected
    @Test
    void restockUIUnauthorized_TC58() {
        Publication publication = new Publication();
        assertFalse(publication.canAccessRestockUI(false, true));
    }

    // TC59
    // Test Objective: Verify the correct publication can be restocked
    // Input(s): ID 201; add 6 items
    // Expected Output(s): Stock increased on ID 201
    @Test
    void restockCorrectPublication_TC59() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 4, true, true));
        assertTrue(publication.createPublication(202, "Weekly", "Magazine", 5, 8, true, true));
        assertTrue(publication.restockPublication(201, 6, true, true, true));
        assertEquals(10, publication.readPublication(201).getStock());
        assertEquals(8, publication.readPublication(202).getStock());
    }

    // TC60
    // Test Objective: Verify zero or negative restock quantity is rejected
    // Input(s): Add 0 or -1 items
    // Expected Output(s): Stock not changed
    @Test
    void restockInvalidQuantity_TC60() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 4, true, true));
        assertFalse(publication.restockPublication(201, 0, true, true, true));
        assertFalse(publication.restockPublication(201, -1, true, true, true));
        assertEquals(4, publication.readPublication(201).getStock());
    }

    // TC61
    // Test Objective: Verify unauthorised restock is rejected
    // Input(s): News agent = false
    // Expected Output(s): Stock not changed
    @Test
    void restockUnauthorized_TC61() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 4, true, true));
        assertFalse(publication.restockPublication(201, 5, false, true, true));
        assertEquals(4, publication.readPublication(201).getStock());
    }

    // TC62
    // Test Objective: Verify restock fails if saving fails
    // Input(s): Save successful = false
    // Expected Output(s): Stock not changed
    @Test
    void restockSaveFailure_TC62() {
        Publication publication = new Publication();
        assertTrue(publication.createPublication(201, "Daily News", "Newspaper", 3, 4, true, true));
        assertFalse(publication.restockPublication(201, 5, true, true, false));
        assertEquals(4, publication.readPublication(201).getStock());
    }

    // TC63
    // Test Objective: Verify restock of missing publication is rejected
    // Input(s): ID = 999
    // Expected Output(s): Restock rejected
    @Test
    void restockMissingPublication_TC63() {
        Publication publication = new Publication();
        assertFalse(publication.restockPublication(999, 5, true, true, true));
    }

    // TC64
    // Test Objective: Verify data matches database publication columns
    // Input(s): name,type,cost,stock
    // Expected Output(s): All four column keys returned
    @Test
    void dataProviderFields_TC64() {
        Publication publication = new Publication(201, "Daily News", "Newspaper", 3, 4);
        assertEquals("Daily News", publication.getData().get("name"));
        assertEquals("Newspaper", publication.getData().get("type"));
        assertEquals(3, publication.getData().get("cost"));
        assertEquals(4, publication.getData().get("stock"));
    }
}
