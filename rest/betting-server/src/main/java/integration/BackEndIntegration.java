package integration;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import dto.Customer;
import server.util.RestUtils;

public class BackEndIntegration {
	RestTemplate restTemplate = new RestTemplate();
	
	public Customer fetchUserData(){
		String url = "http://localhost:8090/customer/get?email=admin@betting.com";
		ResponseEntity<Customer> response = callUserData(restTemplate, url);
		Customer info = response.getBody();
		return info;
	}
	
	public static ResponseEntity<Customer> callUserData(RestTemplate restTemplate, String url){
//		RestUtils.createHeaders("user", "password");
//		HttpEntity h = new HttpEntity<>();
//		ResponseEntity<Customer> resp = restTemplate.exchange
//				 (url, HttpMethod.GET, Customer.class);
		
		ResponseEntity<Customer> customer = restTemplate.getForEntity(url, Customer.class);
		 
		return customer;
	}
}
