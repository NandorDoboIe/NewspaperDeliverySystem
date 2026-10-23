package Customer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Customer implements DataProvider{

	private final Map<Integer, Customer> customers = new LinkedHashMap<>();

	private int customerId;
	private String name;
	private String address;
	private String contact;

	public Customer() {
	}

	public Customer(int customerId, String name, String address, String contact) {
		this.customerId = customerId;
		this.name = name;
		this.address = address;
		this.contact = contact;
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

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	@Override
	public String toString() {
		return "Customer ID: " + customerId + "\nName: " + name + "\nAddress: "
				+ address + "\nPhone Number: " + contact;
	}

	public boolean verifyName(String name){
        return !name.equals("");
    }

	public boolean verifyAddress(String address){
        return !address.equals("");
    }

	public boolean verifyContact(String contact){
        if(contact.length() == 10){
            return StringUtils.isNumeric(contact);
        }
        return false;
    }

	public boolean verifySave(boolean saveSuccessful) {
        return saveSuccessful;
    }

	public boolean createCustomer(String name, String address, String contact) {
        if(this.verifyName(name)
        && this.verifyAddress(address)
        && this.verifyContact(contact)){
            customers.put(customerId, new Customer(customerId, name, address, contact));
            return true;
        }
        return false;
    }

	public List<Customer> readCustomers(){
		List<Customer> results = new ArrayList<>();
        for (Customer customer : customers.values()) {
            results.add(customer);
        }
        return results;
	}

	public Customer readCustomer(int customerId){
		Customer customer = customers.get(customerId);

		return customer;
	}

	public boolean updateCustomer(int customerId, String name, String address, String contact){
		Customer customer = readCustomer(customerId);
		if(this.verifyName(name)
        && this.verifyAddress(address)
        && this.verifyContact(contact)){
            customer.setName(name);
			customer.setAddress(address);
			customer.setContact(contact);
            return true;
        }
        return false;
    }

	public boolean deleteCustomer(int customerId){
		if(readCustomer(customerId) != null){
			customers.remove(customerId);
			return true;
		}
		return false;

}
	
	public LinkedHashMap<String,Object> getData(){
		LinkedHashMap<String,Object> data = new LinkedHashMap<>();
		data.put("name",this.getName());
		data.put("address",this.getAddress());
		data.put("phoneNumber",this.getContact());
		return data;
	}
}
