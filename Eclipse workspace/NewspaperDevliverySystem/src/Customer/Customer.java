package Customer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class Customer implements DataProvider{

	private int customerId;
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


	public void setCustomerId(int customerId) {this.customerId = customerId;}

	public void setName(String name) {this.name = name;}

	public void setAddress(String address) {this.address = address;}


	public void setContact(String contact) {this.contact = contact;}

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

	//public Customer readCustomer(){
	//	return this;
	//}

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

	//public boolean deleteCustomer(int customerId){
	//	if(readCustomer(customerId) != null){
	//		customers.remove(customerId);
	//		return true;
	//	}
	//		return false;
	//}
	
	public LinkedHashMap<String,Object> getData(){
		LinkedHashMap<String,Object> data = new LinkedHashMap<>();
		data.put("name",this.getName());
		data.put("address",this.getAddress());
		data.put("contact",this.getContact());
		return data;
	}
}
