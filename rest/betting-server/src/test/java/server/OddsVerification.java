package server;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import dto.Customer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BettingApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OddsVerification {

	@LocalServerPort
	private int port;

	RestTemplate restTemplate = new RestTemplate();

	@Test
	public void testRetrieveStudentCourse() {
		Assert.fail();
		HttpEntity<String> entity = new HttpEntity<String>("");

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/odds"),
				HttpMethod.GET, entity, String.class);

		System.out.println(response.getBody());
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}