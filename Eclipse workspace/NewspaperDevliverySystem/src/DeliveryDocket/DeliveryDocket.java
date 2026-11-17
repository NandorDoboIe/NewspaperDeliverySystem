package DeliveryDocket;

import java.util.ArrayList;

public class DeliveryDocket {
	private int id;
	private int order_id;
	private int customer_id;
	
	private static ArrayList<DeliveryDocket> dels = new ArrayList<>();
	
	public DeliveryDocket() {}
	
	public DeliveryDocket(int id, int order_id, int customer_id) {
		super();
		this.id = id;
		this.order_id = order_id;
		this.customer_id = customer_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public int getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}
	
	public boolean verifyId(int id) {
		return id>0;
	}
	
	public boolean verifyCustomerId(int customer_id) {
		return customer_id>0;
	}
	
	public boolean verifyOrderId(int order_id) {
		return order_id>0;
	}
	
	public boolean verifyDeliveryDocket() {
        return verifyId(id) && verifyOrderId(order_id) && verifyCustomerId(customer_id);
    }
	
	public boolean createDeliveryDocket(int id, int order_id, int customer_id) {

		DeliveryDocket newDel = new DeliveryDocket(id,order_id,customer_id);

        if (!newDel.verifyDeliveryDocket()) {
            return false;
        }

        // Prevent duplicate IDs
        if (readDeliveryDocket(id) != null) {
            return false;
        }

        dels.add(newDel);
        return true;
    }

    // READ: Find a delivery area by ID
    public DeliveryDocket readDeliveryDocket(int id) {

        for (DeliveryDocket del : dels) {
            if (del.getId() == id) {
                return del;
            }
        }

        return null;
    }
    
    public boolean updateDeliveryDocket(int id, int order_id, int customer_id) {

    	DeliveryDocket del = readDeliveryDocket(id);

        if (del == null) {
            return false;
        }

        if (!verifyOrderId(order_id)) {
            return false;
        }
        if (!verifyCustomerId(customer_id)) {
            return false;
        }

        del.setOrder_id(order_id);
        del.setCustomer_id(customer_id);
        return true;
    }

    // DELETE: Remove a delivery area by ID
    public boolean deleteDeliveryDocket(int id) {

        DeliveryDocket del = readDeliveryDocket(id);

        if (del == null) {
            return false;
        }

        dels.remove(del);
        return true;
    }

    // LIST: Display all delivery areas
    public void listDelAreas() {

        if (dels.isEmpty()) {
            System.out.println("No delivery areas found.");
            return;
        }

        for (DeliveryDocket del : dels) {
            System.out.println(
                "ID: " + del.getId()
                + ", Order ID: " + del.getOrder_id()
                + ", Customer ID: " + del.getCustomer_id()
            );
        }
    }

    // Reset the list between tests
    static void resetAreas() {
    	dels.clear();
    }
	
	
	
}
