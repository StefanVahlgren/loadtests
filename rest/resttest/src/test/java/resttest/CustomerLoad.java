package resttest;

import static com.loadcoder.statics.StopDesisions.duration;
import static com.loadcoder.statics.Time.SECOND;

import org.junit.Assert;
import org.junit.Test;

import com.loadcoder.load.chart.logic.RuntimeChart;
import com.loadcoder.load.scenario.ExecutionBuilder;
import com.loadcoder.load.scenario.Load;
import com.loadcoder.load.scenario.LoadBuilder;
import com.loadcoder.result.Result;
import com.loadcoder.statics.SummaryUtils;

/**
 * This test will NOT run automatically at mvn test since the class doesnt end with Test
 */
public class CustomerLoad {

	@Test
	public void loadCustomerLoadScenario() {
		Assert.fail();
		Load load = new LoadBuilder(LoadScenarios.customerScenario("http://localhost:8090"))
				.amountOfThreads(10)
				.rampup(20 * SECOND)
				.stopDecision(duration(60 * SECOND))
				.build();
		
		Result result = new ExecutionBuilder(load).runtimeResultUser(new RuntimeChart()).build().execute().andWait().getReportedResultFromResultFile();

		SummaryUtils.printSimpleSummary(result, "customerLoadScenario");
	}
}
