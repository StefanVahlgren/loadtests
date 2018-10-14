package resttest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
//import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
//import org.testng.Assert;
//import org.testng.annotations.Test;

import com.loadcoder.load.scenario.ExecutionBuilder;
import com.loadcoder.load.scenario.LoadBuilder;
import com.loadcoder.result.Result;

import resttest.LoadScenarios;
import server.CustomerApp;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = CustomerApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoadScenarioTest {

	@LocalServerPort
	private int port;

	RestTemplate restTemplate = new RestTemplate();

	@Test
	public void testCustomerLoadScenario() {
		Result result = new ExecutionBuilder(
				new LoadBuilder(LoadScenarios.customerScenario(createURLWithPort()))
				.build()).build().execute().andWait().getReportedResultFromResultFile();
		
		Assert.assertEquals(4, result.getAmountOfTransactions());
		Assert.assertEquals(0, result.getAmountOfFails());
	}
	
	private String createURLWithPort() {
		return "http://localhost:" + port;
	}
}