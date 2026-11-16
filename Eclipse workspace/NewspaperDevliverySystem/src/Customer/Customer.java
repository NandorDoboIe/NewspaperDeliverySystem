package Customer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class Customer implements DataProvider{

	private final int customerId;
	private String name;
	private String address;
	private String contact;

	public Customer(int customerId, String name, String address, String contact) {
		if(verifyId(customerId)
		        && verifyName(name)
		        && verifyAddress(address)
		        && verifyContact(contact)){
			this.customerId = customerId;
			this.name = name;
			this.address = address;
			this.contact = contact;
		}
		else {
			throw new IllegalArgumentException("Invalid customer details");
		}
	}

	public int getCustomerId() {return customerId;}
	
	public String getName() {return name;}
	
	public String getAddress() {return address;}
	
	public String getContact() {return contact;}


	public boolean setName(String name) {
		if(verifyName(name)) {
			this.name = name;
			return true;
		}
		return false;
	}
			

	public boolean setAddress(String address) {
		if(verifyAddress(address)) {
			this.address = address;
			return true;
		}
		return false;
	}

	public boolean setContact(String contact) {
		if(verifyContact(contact)) {
			this.contact = contact;
			return true;
		}
		return false;
		}

	@Override
	public String toString() {
		return "Customer ID: " + customerId + "\nName: " + name + "\nAddress: "
				+ address + "\nContact: " + contact;
	}
	
	private boolean verifyId(int id) {
		return id > 0;
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

	public boolean updateCustomer(String name, String address, String contact){
		if(this.verifyName(name)
        && this.verifyAddress(address)
        && this.verifyContact(contact)){
            setName(name);
			setAddress(address);
			setContact(contact);
            return true;
        }
        return false;
    }
	
	public LinkedHashMap<String,Object> getData(){
		LinkedHashMap<String,Object> data = new LinkedHashMap<>();
		data.put("name",this.getName());
		data.put("address",this.getAddress());
		data.put("contact",this.getContact());
		return data;
	}
}
