package Customer;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import Database.DataProvider;

public class Customer implements DataProvider{

	private int customerId;
	private String firstName;
	private String surname;
	private String address;
	private String phoneNumber;

	public Customer() {
	}

	public Customer(String firstName, String surname, String address, String phoneNumber) {
		this.firstName = firstName;
		this.surname = surname;
		this.address = address;
		this.phoneNumber = phoneNumber;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "Customer ID: " + customerId + "\nFirst Name: " + firstName + "\nSurname: " + surname + "\nAddress: "
				+ address + "\nPhone Number: " + phoneNumber;
	}
	
	public LinkedHashMap<String,Object> getData(){
		LinkedHashMap<String,Object> data = new LinkedHashMap<>();
		data.put("firstName",this.getFirstName());
		data.put("surName",this.getSurname());
		data.put("address",this.getAddress());
		data.put("phoneNumber",this.getPhoneNumber());
		return data;
	}
}
