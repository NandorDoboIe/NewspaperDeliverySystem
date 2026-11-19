package DeliveryDocket;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.LinkedHashMap;

public class DeliveryDocketTest {

    private DeliveryDocket sample() {
        return new DeliveryDocket(1, 2, 3, "2025-03-14", true);
    }

    private LinkedHashMap<String, Object> row() {
        LinkedHashMap<String, Object> row = new LinkedHashMap<>();
        row.put("orderId", 2);
        row.put("customerId", 3);
        row.put("date", "2025-03-14");
        row.put("success", false);
        return row;
    }

    // TC01
    // Test Objective: Verify a valid docket stores all supplied details.
    // Input(s): Id = 1, Order Id = 2, Customer Id = 3, Date = "2025-03-14", Success = true
    // Expected Output(s): Docket created with the supplied details
    @Test
    void createValidDocket_TC01() {
        DeliveryDocket d = sample();
        assertEquals(1, d.getDeliveryDocketId());
        assertEquals(2, d.getOrderId());
        assertEquals(3, d.getCustomerId());
        assertEquals("2025-03-14", d.getDate());
        assertTrue(d.getSuccess());
    }

    // TC02
    // Test Objective: Verify id 0 is accepted before MySQL generates an id.
    // Input(s): Id = 0
    // Expected Output(s): Id 0 accepted
    @Test
    void validId_TC02() {
        assertEquals(0, new DeliveryDocket(0, 2, 3, "2025-03-14", false).getDeliveryDocketId());
    }

    // TC03
    // Test Objective: Verify a negative id is rejected.
    // Input(s): Id = -1
    // Expected Output(s): Error message: Invalid ID
    @Test
    void invalidId_TC03() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryDocket(-1, 2, 3, "2025-03-14", false));
    }

    // TC04
    // Test Objective: Verify the order id minimum boundary of 1 is accepted.
    // Input(s): Order Id = 1
    // Expected Output(s): Order Id 1 accepted
    @Test
    void validOrderId_TC04() {
        assertEquals(1, new DeliveryDocket(1, 1, 3, "2025-03-14", false).getOrderId());
    }

    // TC05
    // Test Objective: Verify an order id of 0 is rejected.
    // Input(s): Order Id = 0
    // Expected Output(s): Error message: Invalid Order ID
    @Test
    void invalidOrderId_TC05() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryDocket(1, 0, 3, "2025-03-14", false));
    }

    // TC06
    // Test Objective: Verify a negative order id is rejected.
    // Input(s): Order Id = -5
    // Expected Output(s): Error message: Invalid Order ID
    @Test
    void invalidOrderId_TC06() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryDocket(1, -5, 3, "2025-03-14", false));
    }

    // TC07
    // Test Objective: Verify the customer id minimum boundary of 1 is accepted.
    // Input(s): Customer Id = 1
    // Expected Output(s): Customer Id 1 accepted
    @Test
    void validCustomerId_TC07() {
        assertEquals(1, new DeliveryDocket(1, 2, 1, "2025-03-14", false).getCustomerId());
    }

    // TC08
    // Test Objective: Verify a customer id of 0 is rejected.
    // Input(s): Customer Id = 0
    // Expected Output(s): Error message: Invalid Customer ID
    @Test
    void invalidCustomerId_TC08() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryDocket(1, 2, 0, "2025-03-14", false));
    }

    // TC09
    // Test Objective: Verify a negative customer id is rejected.
    // Input(s): Customer Id = -5
    // Expected Output(s): Error message: Invalid Customer ID
    @Test
    void invalidCustomerId_TC09() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryDocket(1, 2, -5, "2025-03-14", false));
    }

    // TC10
    // Test Objective: Verify a valid yyyy-MM-dd date is accepted.
    // Input(s): Date = "2025-03-14"
    // Expected Output(s): Date accepted
    @Test
    void validDate_TC10() {
        assertEquals("2025-03-14", new DeliveryDocket(1, 2, 3, "2025-03-14", false).getDate());
    }

    // TC11
    // Test Objective: Verify a missing date is rejected.
    // Input(s): Date = null
    // Expected Output(s): Error message: Invalid Date
    @Test
    void invalidDate_TC11() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryDocket(1, 2, 3, null, false));
    }

    // TC12
    // Test Objective: Verify an empty date is rejected.
    // Input(s): Date = ""
    // Expected Output(s): Error message: Invalid Date
    @Test
    void invalidDate_TC12() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryDocket(1, 2, 3, "", false));
    }

    // TC13
    // Test Objective: Verify a date in dd/MM/yyyy format is rejected.
    // Input(s): Date = "14/03/2025"
    // Expected Output(s): Error message: Invalid Date
    @Test
    void invalidDate_TC13() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryDocket(1, 2, 3, "14/03/2025", false));
    }

    // TC14
    // Test Objective: Verify a date without leading zeros is rejected.
    // Input(s): Date = "2025-3-4"
    // Expected Output(s): Error message: Invalid Date
    @Test
    void invalidDate_TC14() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryDocket(1, 2, 3, "2025-3-4", false));
    }

    // TC15
    // Test Objective: Verify a date written as text is rejected.
    // Input(s): Date = "tomorrow"
    // Expected Output(s): Error message: Invalid Date
    @Test
    void invalidDate_TC15() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryDocket(1, 2, 3, "tomorrow", false));
    }

    // TC16
    // Test Objective: Verify a day that does not exist in the month is rejected.
    // Input(s): Date = "2025-02-30"
    // Expected Output(s): Error message: Invalid Date
    @Test
    void invalidDate_TC16() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryDocket(1, 2, 3, "2025-02-30", false));
    }

    // TC17
    // Test Objective: Verify a month that does not exist is rejected.
    // Input(s): Date = "2025-13-01"
    // Expected Output(s): Error message: Invalid Date
    @Test
    void invalidDate_TC17() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryDocket(1, 2, 3, "2025-13-01", false));
    }

    // TC18
    // Test Objective: Verify 29 February is rejected in a non-leap year.
    // Input(s): Date = "2025-02-29"
    // Expected Output(s): Error message: Invalid Date
    @Test
    void invalidDate_TC18() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryDocket(1, 2, 3, "2025-02-29", false));
    }

    // TC19
    // Test Objective: Verify 29 February is accepted in a leap year.
    // Input(s): Date = "2024-02-29"
    // Expected Output(s): Date accepted
    @Test
    void validLeapDay_TC19() {
        assertEquals("2024-02-29", new DeliveryDocket(1, 2, 3, "2024-02-29", false).getDate());
    }

    // TC20
    // Test Objective: Verify spaces around the date are removed when stored.
    // Input(s): Date = " 2025-03-14 "
    // Expected Output(s): Date "2025-03-14"
    @Test
    void dateTrimmed_TC20() {
        assertEquals("2025-03-14", new DeliveryDocket(1, 2, 3, " 2025-03-14 ", false).getDate());
    }

    // TC21
    // Test Objective: Verify getLocalDate returns the date as a LocalDate.
    // Input(s): Date = "2025-03-14"
    // Expected Output(s): LocalDate 2025-03-14
    @Test
    void getLocalDate_TC21() {
        assertEquals(LocalDate.of(2025, 3, 14), sample().getLocalDate());
    }

    // TC22
    // Test Objective: Verify an unsuccessful delivery is stored.
    // Input(s): Success = false
    // Expected Output(s): getSuccess returns false
    @Test
    void successFalse_TC22() {
        assertFalse(new DeliveryDocket(1, 2, 3, "2025-03-14", false).getSuccess());
    }

    // TC23
    // Test Objective: Verify a row without an id (new docket from the CLI) gets id 0.
    // Input(s): Row = {orderId=2, customerId=3, date="2025-03-14", success=false}
    // Expected Output(s): Docket with id 0
    @Test
    void createFromCliInput_TC23() {
        DeliveryDocket d = new DeliveryDocket(row());
        assertEquals(0, d.getDeliveryDocketId());
        assertEquals(2, d.getOrderId());
        assertFalse(d.getSuccess());
    }

    // TC24
    // Test Objective: Verify a row as returned by MySQL is accepted.
    // Input(s): deliveryDocketId = 7L, date = java.sql.Date 2025-03-14, success = 1
    // Expected Output(s): Docket 7, date "2025-03-14", success true
    @Test
    void createFromDatabaseRow_TC24() {
        LinkedHashMap<String, Object> r = row();
        r.put("deliveryDocketId", 7L);
        r.put("date", java.sql.Date.valueOf("2025-03-14"));
        r.put("success", 1);
        DeliveryDocket d = new DeliveryDocket(r);
        assertEquals(7, d.getDeliveryDocketId());
        assertEquals("2025-03-14", d.getDate());
        assertTrue(d.getSuccess());
    }

    // TC25
    // Test Objective: Verify success typed as "yes" in the CLI is read as true.
    // Input(s): success = "yes"
    // Expected Output(s): true
    @Test
    void successFromText_TC25() {
        LinkedHashMap<String, Object> r = row();
        r.put("success", "yes");
        assertTrue(new DeliveryDocket(r).getSuccess());
    }

    // TC26
    // Test Objective: Verify success typed as "false" in the CLI is read as false.
    // Input(s): success = "false"
    // Expected Output(s): false
    @Test
    void successFromText_TC26() {
        LinkedHashMap<String, Object> r = row();
        r.put("success", "false");
        assertFalse(new DeliveryDocket(r).getSuccess());
    }

    // TC27
    // Test Objective: Verify an unreadable success value is rejected.
    // Input(s): success = "maybe"
    // Expected Output(s): Error message: Invalid success: maybe
    @Test
    void invalidSuccess_TC27() {
        LinkedHashMap<String, Object> r = row();
        r.put("success", "maybe");
        assertThrows(IllegalArgumentException.class, () -> new DeliveryDocket(r));
    }

    // TC28
    // Test Objective: Verify a row with a missing date is rejected.
    // Input(s): date = null
    // Expected Output(s): Error message: Invalid Date
    @Test
    void invalidRowDate_TC28() {
        LinkedHashMap<String, Object> r = row();
        r.put("date", null);
        assertThrows(IllegalArgumentException.class, () -> new DeliveryDocket(r));
    }

    // TC29
    // Test Objective: Verify a row with an order id that is not a number is rejected.
    // Input(s): orderId = "abc"
    // Expected Output(s): Error message: Invalid number: abc
    @Test
    void invalidRowOrderId_TC29() {
        LinkedHashMap<String, Object> r = row();
        r.put("orderId", "abc");
        assertThrows(IllegalArgumentException.class, () -> new DeliveryDocket(r));
    }

    // TC30
    // Test Objective: Verify setOrderId changes the order id.
    // Input(s): Order Id = 9
    // Expected Output(s): Order Id 9
    @Test
    void setOrderId_TC30() {
        DeliveryDocket d = sample();
        d.setOrderId(9);
        assertEquals(9, d.getOrderId());
    }

    // TC31
    // Test Objective: Verify setOrderId rejects an invalid id and keeps the old one.
    // Input(s): Order Id = 0
    // Expected Output(s): Error message: Invalid Order ID; order id stays 2
    @Test
    void setInvalidOrderId_TC31() {
        DeliveryDocket d = sample();
        assertThrows(IllegalArgumentException.class, () -> d.setOrderId(0));
        assertEquals(2, d.getOrderId());
    }

    // TC32
    // Test Objective: Verify setCustomerId changes the customer id.
    // Input(s): Customer Id = 8
    // Expected Output(s): Customer Id 8
    @Test
    void setCustomerId_TC32() {
        DeliveryDocket d = sample();
        d.setCustomerId(8);
        assertEquals(8, d.getCustomerId());
    }

    // TC33
    // Test Objective: Verify setCustomerId rejects an invalid id and keeps the old one.
    // Input(s): Customer Id = 0
    // Expected Output(s): Error message: Invalid Customer ID; customer id stays 3
    @Test
    void setInvalidCustomerId_TC33() {
        DeliveryDocket d = sample();
        assertThrows(IllegalArgumentException.class, () -> d.setCustomerId(0));
        assertEquals(3, d.getCustomerId());
    }

    // TC34
    // Test Objective: Verify setDate changes the date.
    // Input(s): Date = "2025-04-01"
    // Expected Output(s): Date "2025-04-01"
    @Test
    void setDate_TC34() {
        DeliveryDocket d = sample();
        d.setDate("2025-04-01");
        assertEquals("2025-04-01", d.getDate());
    }

    // TC35
    // Test Objective: Verify setDate rejects an invalid date and keeps the old one.
    // Input(s): Date = "bad"
    // Expected Output(s): Error message: Invalid Date; date stays "2025-03-14"
    @Test
    void setInvalidDate_TC35() {
        DeliveryDocket d = sample();
        assertThrows(IllegalArgumentException.class, () -> d.setDate("bad"));
        assertEquals("2025-03-14", d.getDate());
    }

    // TC36
    // Test Objective: Verify setSuccess marks a delivery as failed.
    // Input(s): Success = false
    // Expected Output(s): getSuccess returns false
    @Test
    void setSuccess_TC36() {
        DeliveryDocket d = sample();
        d.setSuccess(false);
        assertFalse(d.getSuccess());
    }

    // TC37
    // Test Objective: Verify updateDeliveryDocket changes every field, including success.
    // Input(s): Order Id = 5, Customer Id = 6, Date = "2025-05-05", Success = false
    // Expected Output(s): Docket updated
    @Test
    void validUpdate_TC37() {
        DeliveryDocket d = sample();
        d.updateDeliveryDocket(5, 6, "2025-05-05", false);
        assertEquals(5, d.getOrderId());
        assertEquals(6, d.getCustomerId());
        assertEquals("2025-05-05", d.getDate());
        assertFalse(d.getSuccess());
    }

    // TC38
    // Test Objective: Verify an update with an invalid date changes nothing.
    // Input(s): Order Id = 5, Customer Id = 6, Date = "bad-date", Success = false
    // Expected Output(s): Error message: Invalid Date; docket unchanged
    @Test
    void invalidDateUpdate_TC38() {
        DeliveryDocket d = sample();
        assertThrows(IllegalArgumentException.class, () -> d.updateDeliveryDocket(5, 6, "bad-date", false));
        assertEquals(2, d.getOrderId());
        assertEquals(3, d.getCustomerId());
        assertEquals("2025-03-14", d.getDate());
        assertTrue(d.getSuccess());
    }

    // TC39
    // Test Objective: Verify an update with an invalid order id changes nothing.
    // Input(s): Order Id = 0
    // Expected Output(s): Error message: Invalid Order ID; docket unchanged
    @Test
    void invalidOrderIdUpdate_TC39() {
        DeliveryDocket d = sample();
        assertThrows(IllegalArgumentException.class, () -> d.updateDeliveryDocket(0, 6, "2025-05-05", false));
        assertEquals(2, d.getOrderId());
        assertEquals(3, d.getCustomerId());
        assertTrue(d.getSuccess());
    }

    // TC40
    // Test Objective: Verify getData returns the four database columns and not the id.
    // Input(s): DeliveryDocket(1, 2, 3, "2025-03-14", true)
    // Expected Output(s): {orderId=2, customerId=3, date=2025-03-14, success=true}
    @Test
    void getData_TC40() {
        LinkedHashMap<String, Object> data = sample().getData();
        assertEquals(4, data.size());
        assertEquals(2, data.get("orderId"));
        assertEquals(3, data.get("customerId"));
        assertEquals("2025-03-14", data.get("date"));
        assertEquals(true, data.get("success"));
    }

    // TC41
    // Test Objective: Verify the docket description contains its details.
    // Input(s): DeliveryDocket(1, 2, 3, "2025-03-14", true)
    // Expected Output(s): Text containing the date and success value
    @Test
    void docketDisplay_TC41() {
        String text = sample().toString();
        assertTrue(text.contains("2025-03-14"));
        assertTrue(text.contains("true"));
    }
}
