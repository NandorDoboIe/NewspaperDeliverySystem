package Customer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

import Database.DataProvider;

public class Customer implements DataProvider{

	private final int customerId;
	private String name;
	private String address;
	private String contact;
	
	public Customer(LinkedHashMap<String, Object> row) {
		 this(toInt(row.get("customerId")),
		         (String) row.get("name"),
		         (String) row.get("address"),
		         (String) row.get("contact"));
	}
	
	public Customer(int customerId, String name, String address, String contact) {
		if(verifyId(customerId)) {
			this.customerId = customerId;
		}
		else {
			throw new IllegalArgumentException("Invalid ID");
		}
		setName(name);
		setAddress(address);
		setContact(contact);
	}
	

	public int getCustomerId() {return customerId;}
	
	public String getName() {return name;}
	
	public String getAddress() {return address;}
	
	public String getContact() {return contact;}


	public void setName(String name) {
		if(verifyName(name)) {
			this.name = name;
		}
		else {
			throw new IllegalArgumentException("Invalid Name");
		}
	}
			

	public void setAddress(String address) {
		if(verifyAddress(address)) {
			this.address = address;
		}
		else {
			throw new IllegalArgumentException("Invalid Address");
		}
	}

	public void setContact(String contact) {
		if(verifyContact(contact)) {
			this.contact = contact;
		}
		else {
			throw new IllegalArgumentException("Invalid Contact");
		}
	}

	@Override
	public String toString() {
		return "Customer ID: " + customerId + " | Name: " + name + " | Address: "
				+ address + " | Contact: " + contact;
	}
	
	
	
	private boolean verifyId(int id) {
		return id >= 0;
	}
	
	private boolean verifyName(String name){
		if(name == null) {
			return false;
		}
        return !name.equals("");
    }

	private boolean verifyAddress(String address){
		if(address == null) {
			return false;
		}
        return !address.equals("");
    }

	private boolean verifyContact(String contact){
		if(contact == null) {
			return false;
		}
        if(contact.length() == 10){
            return StringUtils.isNumeric(contact);
        }
        return false;
    }

	public void updateCustomer(String name, String address, String contact){
        setName(name);
		setAddress(address);
		setContact(contact);
       }
 
	
	public LinkedHashMap<String,Object> getData(){
		LinkedHashMap<String,Object> data = new LinkedHashMap<>();
		data.put("name",this.getName());
		data.put("address",this.getAddress());
		data.put("contact",this.getContact());
		return data;
	}
	
	private static int toInt(Object value) {
		System.out.println(value);
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
