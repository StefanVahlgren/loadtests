package resttest;

import static com.loadcoder.statics.StopDesisions.duration;
import static com.loadcoder.statics.ThrottleMode.PER_THREAD;
import static com.loadcoder.statics.Time.PER_SECOND;
import static com.loadcoder.statics.Time.SECOND;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.loadcoder.load.chart.logic.RuntimeChart;
import com.loadcoder.load.scenario.ExecutionBuilder;
import com.loadcoder.load.scenario.LoadBuilder;
import com.loadcoder.load.scenario.LoadScenario;
import com.loadcoder.result.Result;
import com.loadcoder.statics.SummaryUtils;

import dto.Customer;
import dto.OddsList;

public class RestTest {

	String uri = "http://localhost:8080/odds";
	
	Customer getTestdata(){
		return new Customer("mail" + System.currentTimeMillis() + "@gmail.com");
	}
	
	@Test
	public void restSystemTest() {
		RestTemplate template = new RestTemplate();
		Customer customer = getTestdata();
		ResponseEntity<OddsList> resp = template.getForEntity(uri+"?customer=" + customer, OddsList.class);
		OddsList body = resp.getBody();
		Assert.assertEquals(body.getOddsList().size(), 3);
	}

	@Test
	public void restloadTest() {

		LoadScenario loadScenario = new LoadScenario() {
		
			RestTemplate template = new RestTemplate();
			
			Customer customer = getTestdata();
			public void loadScenario() {
				
				load("rest-odds", ()->{
					ResponseEntity<OddsList> resp = template.getForEntity(uri+"?customer=" + customer, OddsList.class);
					return resp;})
				.handleResult((a)->{
					a.setStatus(a.getResponse().getBody().getOddsList().size() == 3);
				}).perform();
			}
		};
		
		RuntimeChart chart = new RuntimeChart();
		Result result = new ExecutionBuilder(new LoadBuilder(loadScenario)
				.amountOfThreads(10)
				.stopDecision(duration(60 * SECOND))
				.throttle(5, PER_SECOND, PER_THREAD)
				.rampup(10 * SECOND)
				.build())
				.runtimeResultUser(chart)
				.build().execute().andWait()
				.getReportedResultFromResultFile();

		SummaryUtils.printSimpleSummary(result, "loadTest");
		chart.waitUntilClosed();
	}
	
}
