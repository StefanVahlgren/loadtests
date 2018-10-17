package server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import dto.Customer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CustomerApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerVerification {

	@LocalServerPort
	private int port;

	RestTemplate restTemplate = new RestTemplate();


	@Test
	public void testRetrieveStudentCourse() {
		HttpEntity<String> entity = new HttpEntity<String>("");


		HttpEntity<Customer> req = new HttpEntity<Customer>(new Customer("my@email.com", 35, "Greta"));

		restTemplate.exchange(createURLWithPort("/customer/create"), HttpMethod.POST, req, Void.class);
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}