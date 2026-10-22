package Customer;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Customer implements DataProvider{

	private int customerId;
	private String name;
	private String address;
	private String phoneNumber;

	public Customer() {
	}

	public Customer(String name, String address, String phoneNumber) {
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "Customer ID: " + customerId + "\nName: " + name + "\nAddress: "
				+ address + "\nPhone Number: " + phoneNumber;
	}
	
	public LinkedHashMap<String,Object> getData(){
		LinkedHashMap<String,Object> data = new LinkedHashMap<>();
		data.put("name",this.getName());
		data.put("address",this.getAddress());
		data.put("phoneNumber",this.getPhoneNumber());
		return data;
	}
}
