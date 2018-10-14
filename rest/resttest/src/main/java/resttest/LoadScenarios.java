package resttest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.loadcoder.load.scenario.LoadScenario;

import dto.Customer;

public class LoadScenarios {

	public static LoadScenario customerScenario(String baseUrl) {
		
	LoadScenario customerScenario = new LoadScenario() {

		RestTemplate restTemplate = new RestTemplate();
		@Override
		public void loadScenario() {
			Customer c = new Customer(generateTestEmail(),  43, "Mona");

			ResponseEntity<Customer> customer = load("get", ()->restTemplate.getForEntity(baseUrl +"/customer/get?email={email}",
					Customer.class, c.getEmail())).perform();
			
			HttpEntity<Customer> req = new HttpEntity<Customer>(c);

			load("create", () ->restTemplate.exchange(baseUrl + "/customer/create", HttpMethod.POST, req, Void.class)).perform();

			c.setName("Agnes");
			
			load("update", () -> restTemplate.exchange(baseUrl + "/customer/update", HttpMethod.POST, req, Void.class))
			.handleResult(model -> model.setStatus(model.getResponse().getStatusCode().equals(HttpStatus.OK)))
			.perform();
			
			customer = load("get", ()->restTemplate.getForEntity(baseUrl + "/customer/get?email={email}",
					Customer.class, c.getEmail())).perform();
			
		}
	
		public String generateTestEmail() {
			return "mail_" + System.currentTimeMillis() + "@gmail.com";
		}
	
	};
	return customerScenario;
	}
}
