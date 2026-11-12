package Customer;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import Database.DataProvider;

public class Customer implements DataProvider{

	private int customerId;
	private String firstName;
	private String surname;
	private String address;
	private String contact;

	public Customer() {
	}

	public Customer(String firstName, String surname, String address, String contact) {
		this.firstName = firstName;
		this.surname = surname;
		this.address = address;
		this.contact = contact;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getcontact() {
		return contact;
	}

	public void setcontact(String contact) {
		this.contact = contact;
	}

	@Override
	public String toString() {
		return "Customer ID: " + customerId + "\nFirst Name: " + firstName + "\nSurname: " + surname + "\nAddress: "
				+ address + "\nPhone Number: " + contact;
	}
	
	public LinkedHashMap<String,Object> getData(){
		LinkedHashMap<String,Object> data = new LinkedHashMap<>();
		data.put("firstName",this.getFirstName());
		data.put("surName",this.getSurname());
		data.put("address",this.getAddress());
		data.put("contact",this.getcontact());
		return data;
	}
}
