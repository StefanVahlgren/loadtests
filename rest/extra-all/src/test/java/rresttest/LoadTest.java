package rresttest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
//import org.junit.Test;
//import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.DisabledIf;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
//import org.testng.Assert;
//import org.testng.annotations.Test;

import com.loadcoder.load.scenario.ExecutionBuilder;
import com.loadcoder.load.scenario.LoadBuilder;
import com.loadcoder.load.scenario.LoadScenario;
import com.loadcoder.result.Result;
import com.loadcoder.statics.SummaryUtils;

import dto.Customer;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoadTest {

//	@LocalServerPort
//	private int port;
//
//	RestTemplate restTemplate = new RestTemplate();
//
//	LoadScenario customerScenario = new LoadScenario() {
//
//		@Override
//		public void loadScenario() {
//			Customer c = new Customer("mona@gmail.com", "Mona", 43);
//
//			ResponseEntity<Customer> customer = load("get", ()->restTemplate.getForEntity(createURLWithPort("/customer/get?email={email}"),
//					Customer.class, c.getEmail())).perform();
//			
//			HttpEntity<Customer> req = new HttpEntity<Customer>(c);
//
//			load("create", () ->restTemplate.exchange(createURLWithPort("/customer/create"), HttpMethod.POST, req, Void.class)).perform();
//
//			c.setName("Agnes");
//			
//			load("update", () -> restTemplate.exchange(createURLWithPort("/customer/update"), HttpMethod.POST, req, Void.class))
//			.handleResult(model -> model.setStatus(model.getResponse().getStatusCode().equals(HttpStatus.OK)))
//			.perform();
//			
//			customer = load("get", ()->restTemplate.getForEntity(createURLWithPort("/customer/get?email={email}"),
//					Customer.class, c.getEmail())).perform();
//			
//			System.out.println(customer.getBody().getName());
//		}
//	};
//
//	@Test
//	public void testCustomerLoadScenario() {
//		Result result = new ExecutionBuilder(new LoadBuilder(customerScenario).build()).build().execute().andWait().getReportedResultFromResultFile();
//		Assert.assertEquals(result.getAmountOfTransactions(), 4);
//		Assert.assertEquals(result.getAmountOfFails(), 0);
//	}
//	
//	@Test
//	public void loadCustomerLoadScenario() {
//		
//		Result result = new ExecutionBuilder(new LoadBuilder(customerScenario).build()).build().execute().andWait().getReportedResultFromResultFile();
//	
//		Assert.assertEquals(result.getAmountOfTransactions(), 4);
//		Assert.assertEquals(result.getAmountOfFails(), 0);
//		SummaryUtils.printSimpleSummary(result, "customerLoadScenario");
//	}
//	
//	private String createURLWithPort(String uri) {
//		return "http://localhost:" + port + uri;
//	}
}