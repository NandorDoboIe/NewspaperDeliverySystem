package Bill;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import Customer.Customer;
import DeliveryDocket.DeliveryDocket;
import Order.Order;
import Publication.Publication;

public class BillTest {

    private static final YearMonth MARCH = YearMonth.of(2025, 3);

    private final Customer mary = new Customer(1, 1, "Mary Murphy", "12 Church Street", "0871234567");

    // publication 1 costs 3, publication 2 costs 5
    private final List<Publication> publications = List.of(
            new Publication(1, "Daily News", "Newspaper", 3, 10),
            new Publication(2, "Farm Weekly", "Magazine", 5, 10));

    // orders 1 and 2 belong to customer 1, order 3 to customer 2
    private final List<Order> orders = List.of(
            new Order(1, 1, 1, Order.patternFromString("1111111")),
            new Order(2, 1, 2, Order.patternFromString("0000001")),
            new Order(3, 2, 1, Order.patternFromString("1111111")));

    private Bill sample() {
        return new Bill(1, 2, 30, "2025-03-31");
    }

    private LinkedHashMap<String, Object> row() {
        LinkedHashMap<String, Object> row = new LinkedHashMap<>();
        row.put("customerId", 2);
        row.put("amount", 30);
        row.put("date", "2025-03-31");
        return row;
    }

    // customer 1 in March: 2 x Daily News + 1 x Farm Weekly delivered, 1 failed
    private List<DeliveryDocket> dockets() {
        List<DeliveryDocket> d = new ArrayList<>();
        d.add(new DeliveryDocket(1, 1, 1, "2025-03-01", true));  // 3
        d.add(new DeliveryDocket(2, 1, 1, "2025-03-02", true));  // 3
        d.add(new DeliveryDocket(3, 1, 1, "2025-03-03", false)); // failed, not charged
        d.add(new DeliveryDocket(4, 2, 1, "2025-03-02", true));  // 5
        d.add(new DeliveryDocket(5, 1, 1, "2025-02-28", true));  // other month
        d.add(new DeliveryDocket(6, 1, 1, "2025-04-01", true));  // other month
        d.add(new DeliveryDocket(7, 3, 2, "2025-03-01", true));  // other customer
        return d;
    }

    // TC01
    // Test Objective: Verify a valid bill stores all supplied details.
    // Input(s): Id = 1, Customer Id = 2, Amount = 30, Date = "2025-03-31"
    // Expected Output(s): Bill created with the supplied details
    @Test
    void createValidBill_TC01() {
        Bill b = sample();
        assertEquals(1, b.getBillID());
        assertEquals(2, b.getCustomerID());
        assertEquals(30, b.getAmount());
        assertEquals("2025-03-31", b.getDate());
    }

    // TC02
    // Test Objective: Verify id 0 is accepted before MySQL generates an id.
    // Input(s): Id = 0
    // Expected Output(s): Id 0 accepted
    @Test
    void validId_TC02() {
        assertEquals(0, new Bill(0, 2, 30, "2025-03-31").getBillID());
    }

    // TC03
    // Test Objective: Verify a negative id is rejected.
    // Input(s): Id = -1
    // Expected Output(s): Error message: Bill ID cannot be negative.
    @Test
    void invalidId_TC03() {
        assertThrows(IllegalArgumentException.class, () -> new Bill(-1, 2, 30, "2025-03-31"));
    }

    // TC04
    // Test Objective: Verify the customer id minimum boundary of 1 is accepted.
    // Input(s): Customer Id = 1
    // Expected Output(s): Customer Id 1 accepted
    @Test
    void validCustomerId_TC04() {
        assertEquals(1, new Bill(1, 1, 30, "2025-03-31").getCustomerID());
    }

    // TC05
    // Test Objective: Verify a customer id of 0 is rejected.
    // Input(s): Customer Id = 0
    // Expected Output(s): Error message: Customer ID must be greater than 0.
    @Test
    void invalidCustomerId_TC05() {
        assertThrows(IllegalArgumentException.class, () -> new Bill(1, 0, 30, "2025-03-31"));
    }

    // TC06
    // Test Objective: Verify a negative customer id is rejected.
    // Input(s): Customer Id = -2
    // Expected Output(s): Error message: Customer ID must be greater than 0.
    @Test
    void invalidCustomerId_TC06() {
        assertThrows(IllegalArgumentException.class, () -> new Bill(1, -2, 30, "2025-03-31"));
    }

    // TC07
    // Test Objective: Verify an amount of 0 is accepted (nothing delivered that month).
    // Input(s): Amount = 0
    // Expected Output(s): Amount 0 accepted
    @Test
    void validAmount_TC07() {
        assertEquals(0, new Bill(1, 2, 0, "2025-03-31").getAmount());
    }

    // TC08
    // Test Objective: Verify a negative amount is rejected.
    // Input(s): Amount = -1
    // Expected Output(s): Error message: Amount cannot be negative.
    @Test
    void invalidAmount_TC08() {
        assertThrows(IllegalArgumentException.class, () -> new Bill(1, 2, -1, "2025-03-31"));
    }

    // TC09
    // Test Objective: Verify a missing date is rejected.
    // Input(s): Date = null
    // Expected Output(s): Error message: Date must be a real date in yyyy-MM-dd format.
    @Test
    void invalidDate_TC09() {
        assertThrows(IllegalArgumentException.class, () -> new Bill(1, 2, 30, null));
    }

    // TC10
    // Test Objective: Verify an empty date is rejected.
    // Input(s): Date = ""
    // Expected Output(s): Error message: Date must be a real date in yyyy-MM-dd format.
    @Test
    void invalidDate_TC10() {
        assertThrows(IllegalArgumentException.class, () -> new Bill(1, 2, 30, ""));
    }

    // TC11
    // Test Objective: Verify a date in dd/MM/yyyy format is rejected.
    // Input(s): Date = "31/03/2025"
    // Expected Output(s): Error message: Date must be a real date in yyyy-MM-dd format.
    @Test
    void invalidDate_TC11() {
        assertThrows(IllegalArgumentException.class, () -> new Bill(1, 2, 30, "31/03/2025"));
    }

    // TC12
    // Test Objective: Verify a day that does not exist in the month is rejected.
    // Input(s): Date = "2025-02-30"
    // Expected Output(s): Error message: Date must be a real date in yyyy-MM-dd format.
    @Test
    void invalidDate_TC12() {
        assertThrows(IllegalArgumentException.class, () -> new Bill(1, 2, 30, "2025-02-30"));
    }

    // TC13
    // Test Objective: Verify a row without an id (new bill from the CLI) gets id 0, with values typed as text.
    // Input(s): Row = {customerId="2", amount="30", date="2025-03-31"}
    // Expected Output(s): Bill with id 0, customer 2, amount 30
    @Test
    void createFromCliInput_TC13() {
        LinkedHashMap<String, Object> r = row();
        r.put("customerId", "2");
        r.put("amount", "30");
        Bill b = new Bill(r);
        assertEquals(0, b.getBillID());
        assertEquals(2, b.getCustomerID());
        assertEquals(30, b.getAmount());
    }

    // TC14
    // Test Objective: Verify a row as returned by MySQL is accepted.
    // Input(s): billId = 7L, date = java.sql.Date 2025-03-31
    // Expected Output(s): Bill 7 with date "2025-03-31"
    @Test
    void createFromDatabaseRow_TC14() {
        LinkedHashMap<String, Object> r = row();
        r.put("billId", 7L);
        r.put("date", java.sql.Date.valueOf("2025-03-31"));
        Bill b = new Bill(r);
        assertEquals(7, b.getBillID());
        assertEquals("2025-03-31", b.getDate());
    }

    // TC15
    // Test Objective: Verify a row missing the customer id is rejected.
    // Input(s): Row without customerId
    // Expected Output(s): Error message: Missing bill field: customerId
    @Test
    void missingCustomerId_TC15() {
        LinkedHashMap<String, Object> r = row();
        r.remove("customerId");
        assertThrows(IllegalArgumentException.class, () -> new Bill(r));
    }

    // TC16
    // Test Objective: Verify a row missing the amount is rejected.
    // Input(s): Row without amount
    // Expected Output(s): Error message: Missing bill field: amount
    @Test
    void missingAmount_TC16() {
        LinkedHashMap<String, Object> r = row();
        r.remove("amount");
        assertThrows(IllegalArgumentException.class, () -> new Bill(r));
    }

    // TC17
    // Test Objective: Verify a row missing the date is rejected.
    // Input(s): Row without date
    // Expected Output(s): Error message: Missing bill field: date
    @Test
    void missingDate_TC17() {
        LinkedHashMap<String, Object> r = row();
        r.remove("date");
        assertThrows(IllegalArgumentException.class, () -> new Bill(r));
    }

    // TC18
    // Test Objective: Verify a missing row is rejected.
    // Input(s): Row = null
    // Expected Output(s): Error message: Bill data is required.
    @Test
    void nullRow_TC18() {
        assertThrows(IllegalArgumentException.class, () -> new Bill((LinkedHashMap<String, Object>) null));
    }

    // TC19
    // Test Objective: Verify an amount typed as words is rejected.
    // Input(s): amount = "ten"
    // Expected Output(s): Error message: amount must be a whole number: ten
    @Test
    void textAmount_TC19() {
        LinkedHashMap<String, Object> r = row();
        r.put("amount", "ten");
        assertThrows(IllegalArgumentException.class, () -> new Bill(r));
    }

    // TC20
    // Test Objective: Verify a decimal amount is rejected.
    // Input(s): amount = 2.5
    // Expected Output(s): Error message: amount must be a whole number within the integer range.
    @Test
    void decimalAmount_TC20() {
        LinkedHashMap<String, Object> r = row();
        r.put("amount", 2.5);
        assertThrows(IllegalArgumentException.class, () -> new Bill(r));
    }

    // TC21
    // Test Objective: Verify getData returns the three database columns and not the id.
    // Input(s): Bill(1, 2, 30, "2025-03-31")
    // Expected Output(s): {customerId=2, amount=30, date=2025-03-31}
    @Test
    void getData_TC21() {
        LinkedHashMap<String, Object> data = sample().getData();
        assertEquals(3, data.size());
        assertEquals(2, data.get("customerId"));
        assertEquals(30, data.get("amount"));
        assertEquals("2025-03-31", data.get("date"));
    }

    // TC22
    // Test Objective: Verify an empty bill cannot be saved.
    // Input(s): new Bill()
    // Expected Output(s): Error message: Cannot save bill
    @Test
    void emptyBillNotSaved_TC22() {
        assertThrows(IllegalStateException.class, () -> new Bill().getData());
    }

    // TC23
    // Test Objective: Verify successful deliveries are charged at the publication cost.
    // Input(s): 2 successful Daily News (cost 3) deliveries in March
    // Expected Output(s): 6
    @Test
    void chargeSuccessfulDeliveries_TC23() {
        List<DeliveryDocket> twoDelivered = List.of(
                new DeliveryDocket(1, 1, 1, "2025-03-01", true),
                new DeliveryDocket(2, 1, 1, "2025-03-02", true));
        assertEquals(6, Bill.calculateTotal(1, MARCH, twoDelivered, orders, publications));
    }

    // TC24
    // Test Objective: Verify deliveries of several publications are added together and failed ones are not charged.
    // Input(s): Customer 1, March: 2 x cost 3, 1 x cost 5, 1 failed
    // Expected Output(s): 11
    @Test
    void chargeSeveralPublications_TC24() {
        assertEquals(11, Bill.calculateTotal(1, MARCH, dockets(), orders, publications));
    }

    // TC25
    // Test Objective: Verify failed deliveries are not charged.
    // Input(s): 1 failed delivery in March
    // Expected Output(s): 0
    @Test
    void failedNotCharged_TC25() {
        List<DeliveryDocket> failed = List.of(new DeliveryDocket(1, 1, 1, "2025-03-01", false));
        assertEquals(0, Bill.calculateTotal(1, MARCH, failed, orders, publications));
    }

    // TC26
    // Test Objective: Verify other customers' deliveries are not charged to this customer.
    // Input(s): Customer 2, March
    // Expected Output(s): 3 (only customer 2's delivery)
    @Test
    void otherCustomerExcluded_TC26() {
        assertEquals(3, Bill.calculateTotal(2, MARCH, dockets(), orders, publications));
    }

    // TC27
    // Test Objective: Verify deliveries from the previous month are not charged.
    // Input(s): Customer 1, February
    // Expected Output(s): 3 (only the 28 February delivery)
    @Test
    void previousMonthSeparate_TC27() {
        assertEquals(3, Bill.calculateTotal(1, YearMonth.of(2025, 2), dockets(), orders, publications));
    }

    // TC28
    // Test Objective: Verify deliveries from the next month are not charged.
    // Input(s): Customer 1, April
    // Expected Output(s): 3 (only the 1 April delivery)
    @Test
    void nextMonthSeparate_TC28() {
        assertEquals(3, Bill.calculateTotal(1, YearMonth.of(2025, 4), dockets(), orders, publications));
    }

    // TC29
    // Test Objective: Verify a customer with no dockets is charged nothing.
    // Input(s): Dockets = empty list
    // Expected Output(s): 0
    @Test
    void noDockets_TC29() {
        assertEquals(0, Bill.calculateTotal(1, MARCH, new ArrayList<>(), orders, publications));
    }

    // TC30
    // Test Objective: Verify a docket whose order does not exist is reported.
    // Input(s): Docket for order 99
    // Expected Output(s): Error message: Order 99 was not found.
    @Test
    void unknownOrder_TC30() {
        List<DeliveryDocket> unknownOrder = List.of(new DeliveryDocket(1, 99, 1, "2025-03-01", true));
        assertThrows(IllegalArgumentException.class,
                () -> Bill.calculateTotal(1, MARCH, unknownOrder, orders, publications));
    }

    // TC31
    // Test Objective: Verify a docket whose publication does not exist is reported.
    // Input(s): Publications list without publication 2
    // Expected Output(s): Error message: Publication 2 was not found.
    @Test
    void unknownPublication_TC31() {
        List<Publication> noPublication2 = List.of(publications.get(0));
        assertThrows(IllegalArgumentException.class,
                () -> Bill.calculateTotal(1, MARCH, dockets(), orders, noPublication2));
    }

    // TC32
    // Test Objective: Verify the calculation rejects a customer id of 0.
    // Input(s): Customer Id = 0
    // Expected Output(s): Error message: Customer ID must be greater than 0.
    @Test
    void calculateInvalidCustomer_TC32() {
        assertThrows(IllegalArgumentException.class, () -> Bill.calculateTotal(0, MARCH, dockets(), orders, publications));
    }

    // TC33
    // Test Objective: Verify the calculation rejects a missing month.
    // Input(s): Month = null
    // Expected Output(s): Error message: Bill month is required.
    @Test
    void calculateNullMonth_TC33() {
        assertThrows(IllegalArgumentException.class, () -> Bill.calculateTotal(1, null, dockets(), orders, publications));
    }

    // TC34
    // Test Objective: Verify the calculation rejects a missing dockets list.
    // Input(s): Dockets = null
    // Expected Output(s): Error message: Delivery dockets are required.
    @Test
    void calculateNullDockets_TC34() {
        assertThrows(IllegalArgumentException.class, () -> Bill.calculateTotal(1, MARCH, null, orders, publications));
    }

    // TC35
    // Test Objective: Verify the calculation rejects a missing orders list.
    // Input(s): Orders = null
    // Expected Output(s): Error message: Order details are required.
    @Test
    void calculateNullOrders_TC35() {
        assertThrows(IllegalArgumentException.class, () -> Bill.calculateTotal(1, MARCH, dockets(), null, publications));
    }

    // TC36
    // Test Objective: Verify the calculation rejects a missing publications list.
    // Input(s): Publications = null
    // Expected Output(s): Error message: Publication details are required to calculate cost.
    @Test
    void calculateNullPublications_TC36() {
        assertThrows(IllegalArgumentException.class, () -> Bill.calculateTotal(1, MARCH, dockets(), orders, null));
    }

    // TC37
    // Test Objective: Verify the printed bill shows the month, customer name and address.
    // Input(s): Customer Mary Murphy, March 2025
    // Expected Output(s): Text containing 2025-03, Mary Murphy and 12 Church Street
    @Test
    void printableBillCustomer_TC37() {
        String bill = Bill.getPrintableBill(mary, MARCH, dockets(), orders, publications);
        assertTrue(bill.contains("2025-03"));
        assertTrue(bill.contains("Mary Murphy"));
        assertTrue(bill.contains("12 Church Street"));
    }

    // TC38
    // Test Objective: Verify the printed bill lists each publication with count, cost and subtotal.
    // Input(s): Customer Mary Murphy, March 2025
    // Expected Output(s): "Daily News ... 2 x €3 = €6" and "Farm Weekly ... 1 x €5 = €5"
    @Test
    void printableBillLines_TC38() {
        String bill = Bill.getPrintableBill(mary, MARCH, dockets(), orders, publications);
        assertTrue(bill.contains("Daily News") && bill.contains("2 x \u20AC3 = \u20AC6"));
        assertTrue(bill.contains("Farm Weekly") && bill.contains("1 x \u20AC5 = \u20AC5"));
    }

    // TC39
    // Test Objective: Verify the printed bill shows how many deliveries failed.
    // Input(s): Customer Mary Murphy, March 2025 (1 failed)
    // Expected Output(s): "Failed deliveries (not charged): 1"
    @Test
    void printableBillFailed_TC39() {
        String bill = Bill.getPrintableBill(mary, MARCH, dockets(), orders, publications);
        assertTrue(bill.contains("Failed deliveries (not charged): 1"));
    }

    // TC40
    // Test Objective: Verify the printed bill shows the total.
    // Input(s): Customer Mary Murphy, March 2025
    // Expected Output(s): "Total Cost: €11"
    @Test
    void printableBillTotal_TC40() {
        String bill = Bill.getPrintableBill(mary, MARCH, dockets(), orders, publications);
        assertTrue(bill.contains("Total Cost: \u20AC11"));
    }

    // TC41
    // Test Objective: Verify the printed bill says when nothing was delivered.
    // Input(s): Customer Mary Murphy, June 2025
    // Expected Output(s): "No successful deliveries" and "Total Cost: €0"
    @Test
    void printableBillEmpty_TC41() {
        String bill = Bill.getPrintableBill(mary, YearMonth.of(2025, 6), dockets(), orders, publications);
        assertTrue(bill.contains("No successful deliveries"));
        assertTrue(bill.contains("Total Cost: \u20AC0"));
    }

    // TC42
    // Test Objective: Verify the printed bill rejects a missing customer.
    // Input(s): Customer = null
    // Expected Output(s): Error message: Customer details are required.
    @Test
    void printableBillNullCustomer_TC42() {
        assertThrows(IllegalArgumentException.class,
                () -> Bill.getPrintableBill(null, MARCH, dockets(), orders, publications));
    }

    // TC43
    // Test Objective: Verify the bill description contains its details.
    // Input(s): Bill(1, 2, 30, "2025-03-31")
    // Expected Output(s): Text containing Bill 1, Customer ID: 2, 30 and 2025-03-31
    @Test
    void billDisplay_TC43() {
        String s = sample().toString();
        assertTrue(s.contains("Bill 1"));
        assertTrue(s.contains("Customer ID: 2"));
        assertTrue(s.contains("30"));
        assertTrue(s.contains("2025-03-31"));
    }
}
