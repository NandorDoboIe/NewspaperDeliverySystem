package DeliveryArea;

import java.util.LinkedHashMap;
import Database.DataProvider;

public class DeliveryArea implements DataProvider {

	private final int deliveryAreaId;
	private String location;

	public DeliveryArea(LinkedHashMap<String, Object> row) {
		this(toInt(row.get("deliveryAreaId")),
		     (String) row.get("location"));
	}

	public DeliveryArea(int deliveryAreaId, String location) {
		if (deliveryAreaId < 0 || location == null || location.trim().isEmpty()) {
			throw new IllegalArgumentException("Invalid delivery area details");
		}
		this.deliveryAreaId = deliveryAreaId;
		this.location = location.trim();
	}

	public int getDeliveryAreaId() {return deliveryAreaId;}

	public String getLocation() {return location;}

	@Override
	public String toString() {
		return "Delivery Area ID: " + deliveryAreaId + " | Location: " + location;
	}

	@Override
	public LinkedHashMap<String, Object> getData() {
		LinkedHashMap<String, Object> data = new LinkedHashMap<>();
		data.put("location", location);
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