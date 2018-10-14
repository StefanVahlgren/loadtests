package resttest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.loadcoder.load.chart.logic.Chart;
import com.loadcoder.load.chart.logic.ResultChart;
import com.loadcoder.load.chart.logic.RuntimeChart;
import com.loadcoder.load.scenario.ExecutionBuilder;
import com.loadcoder.load.scenario.LoadBuilder;
import com.loadcoder.load.scenario.LoadScenario;
import com.loadcoder.result.Result;
import com.loadcoder.statics.SummaryUtils;

import integration.BackEndIntegration;
import resttest.security.SecurityUtils;
import server.util.RestUtils;

import static com.loadcoder.statics.StopDesisions.*;
import static com.loadcoder.statics.Time.*;
import static com.loadcoder.statics.ThrottleMode.*;

public class SecureTest {

	String url = "http://localhost:8080/api/secure/userdata";

	@Test
	public void secureSystemTest() {
		RestTemplate template = new RestTemplate();
		template.setErrorHandler(RestUtils.getErrorHandler());

		ResponseEntity<String> resp = template.exchange(url, HttpMethod.GET,
				new HttpEntity<>(SecurityUtils.createHeaders("user", "wrongpassword")), String.class);

		Assert.assertEquals(resp.getStatusCode(), HttpStatus.UNAUTHORIZED);

		resp = template.exchange(url, HttpMethod.GET, new HttpEntity<>(SecurityUtils.createHeaders("user", "password")),
				String.class);

		Assert.assertEquals(resp.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void secureEndpointLoadTest() {

		RestTemplate template = new RestTemplate();

		LoadScenario loadScenario = new LoadScenario() {
			RestTemplate template = new RestTemplate();

			@Override
			public void loadScenario() {
				ResponseEntity<String> resp = load("restCall",
						() -> template.exchange(url, HttpMethod.GET,
								new HttpEntity<>(SecurityUtils.createHeaders("user", "password")), String.class))
										.handleResult(result -> {
											result.setStatus(result.getResponse().getStatusCode() == HttpStatus.OK);
										}).perform();
			}
		};

		RuntimeChart chart = new RuntimeChart();
		Result result = new ExecutionBuilder(new LoadBuilder(loadScenario).stopDecision(duration(10 * SECOND)).build())
				.runtimeResultUser(chart).build().execute().andWait().getReportedResultFromResultFile();

		ResultChart resultChart = new ResultChart(result);
		resultChart.waitUntilClosed();
		SummaryUtils.printSimpleSummary(result, "loadTest");
	}

}
