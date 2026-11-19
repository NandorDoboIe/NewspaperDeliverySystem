package DeliveryDocket;

import java.util.LinkedHashMap;

import Database.DataProvider;

public class DeliveryDocket implements DataProvider{

	private int deliveryDocketId;
	int orderId;
	int customerId;
	String date;
	boolean success;
	
	public DeliveryDocket(LinkedHashMap<String, Object> row) {
		 this(toInt(row.get("deliveryDocketId")),
				 toInt(row.get("orderId")),
				 toInt(row.get("customerId")),
		         (String) row.get("date"),
		         (boolean) row.get("success"));
	}
	
	public DeliveryDocket(int deliveryDocketId, int orderId, int customerId, String date, boolean success) {
		if(verifyId(deliveryDocketId)) {
			this.deliveryDocketId = deliveryDocketId;
		}
		setOrderId(orderId);
		setCustomerId(customerId);
		setDate(date);
		setSuccess(success);
		
	}
	
	
	public int getDeliveryDocketId() {return this.deliveryDocketId;}
	
	public int getCustomerId() {return this.customerId;}
	
	public int getOrderId() {return this.orderId;}
	
	public String getDate() {return this.date;}
	
	public boolean getSuccess() {return this.success;}
	
	
	public void setCustomerId(int customerId) {
		if(verifyId(customerId)) {
			this.customerId = customerId;
		}
		else {
			throw new IllegalArgumentException("Invalid Customer ID");
		}
	}
	
	public void setOrderId(int orderId) {
		if(verifyId(orderId)) {
			this.orderId = orderId;
		}
		else {
			throw new IllegalArgumentException("Invalid Order ID");
		}
	}
	
	public void setDate(String date) {
		if(verifyDate(date)) {
			this.date = date;
		}
		else {
			throw new IllegalArgumentException("Invalid Date");
		}
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	@Override
	public String toString() {
		return "Delivery Docket ID: " + deliveryDocketId + " | Order ID: " + orderId + " | Customer ID: "
				+ customerId + " | Date: " + date + " | Success: " + success;
	}
	
	
	private boolean verifyId(int id) {
		return id >= 0;
	}
	
	private boolean verifyDate(String date) {
		if(date == null) {
			return false;
		}
		return false;
	}
	
	public void updateDeliveryDocket(int orderId, int customerId, String date, boolean Success){
        setOrderId(orderId);
		setCustomerId(customerId);
		setDate(date);
		setSuccess(success);
    }
	
	
	public LinkedHashMap<String,Object> getData(){
		LinkedHashMap<String,Object> data = new LinkedHashMap<>();
		data.put("orderId", this.getOrderId());
		data.put("customerId",this.getCustomerId());
		data.put("date",this.getDate());
		data.put("success",this.getSuccess());
		return data;
	}
	
	private static int toInt(Object value) {
	    if (value instanceof Number) {
	      return ((Number) value).intValue();
	    }
	    try {
	      return Integer.parseInt(String.valueOf(value).trim());
	    } catch (NumberFormatException ex) {
	      throw new IllegalArgumentException("Invalid number: " + value);
	    }
	}

}

