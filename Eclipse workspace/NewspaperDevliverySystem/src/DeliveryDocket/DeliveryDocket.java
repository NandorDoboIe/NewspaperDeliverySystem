package DeliveryDocket;

import java.util.ArrayList;

public class DeliveryDocket {
	private int id;
	private int order_id;
	private int customer_id;
	
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
	
	
	
}
