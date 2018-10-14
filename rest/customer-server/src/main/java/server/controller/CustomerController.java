package server.controller;


import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dto.Customer;
import server.persistens.MemoryMaps;
import server.util.RestUtils;

@RestController
@RequestMapping(value = "/customer")
public class CustomerController {

	private final MemoryMaps m = new MemoryMaps();
	
	public CustomerController() {
		Customer admin = new Customer("admin@betting.com");
		m.getCustomers().put(admin.getEmail(), admin);
	}
    
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Customer getCustomer(@RequestParam String email) {
    	RestUtils.sleep(48);
    	Customer c = m.getCustomers().get(email);
    	return c;
    }
    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Customer createCustomer(@RequestBody Customer c) {
    	RestUtils.sleep(82);
    	Customer alreadyExistingCustomer = m.getCustomers().get(c.getEmail());
    	if(alreadyExistingCustomer != null) {
    		throw new RuntimeException("Customer with this email already exists");
    	}
    	
    	updateCustomer(c);

    	return c;
    }
    
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public void updateCustomer(@RequestBody Customer customer) {
    	RestUtils.sleep(73);
    	m.getCustomers().put(customer.getEmail(), customer);
    }
    
}