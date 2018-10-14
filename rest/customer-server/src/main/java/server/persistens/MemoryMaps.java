package server.persistens;

import java.util.HashMap;
import java.util.Map;

import dto.Customer;

public class MemoryMaps {

	Map<String, Customer> customers = new HashMap<String, Customer>();

	public Map<String, Customer> getCustomers() {
		return customers;
	}
}
