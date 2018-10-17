package testing;

import static com.loadcoder.statics.StopDesisions.duration;
import static com.loadcoder.statics.StopDesisions.iterations;
import static com.loadcoder.statics.ThrottleMode.PER_THREAD;
import static com.loadcoder.statics.Time.PER_SECOND;
import static com.loadcoder.statics.Time.SECOND;

import java.lang.reflect.Method;

import org.testng.annotations.Test;

import com.loadcoder.load.chart.logic.ResultChart;
import com.loadcoder.load.chart.logic.RuntimeChart;
import com.loadcoder.load.scenario.ExecutionBuilder;
import com.loadcoder.load.scenario.FinishedExecution;
import com.loadcoder.load.scenario.Load;
import com.loadcoder.load.scenario.LoadBuilder;
import com.loadcoder.load.scenario.LoadScenario;
import com.loadcoder.load.sut.SUT;
import com.loadcoder.result.Result;
import com.loadcoder.statics.SummaryUtils;

public class LoadcoderExamples {

	@Test
	public void helloWorldLoadTest() {

		// Implement the LoadScenario to define what the load should consist of
		LoadScenario loadScenario = new LoadScenario() {

			public void loadScenario() {

				// Use load method to define a transaction
				load("helloWorld", () -> {
					System.out.println("hello World");
				})
				.perform();
			}
		};

		Load load = new LoadBuilder(loadScenario)
				
				// run 10 iterations of the LoadScenario
				.stopDecision(iterations(10)).build();
			
		// Start the test and wait until it finishes
		new ExecutionBuilder(load).build().execute().andWait();
	}

	@Test
	public void extendedTest(Method method) {

		SUT sut = new SUT();
		LoadScenario ls = new LoadScenario() {

			public void loadScenario() {
				 load("t1", 
						() -> {sut.methodWhereResponseTimeFollowSomeKindOfPattern(100, 250); return "";})
						.handleResult((resultmodel) -> {
						
					if (resultmodel.getResponseTime() > 170) {
						resultmodel.setStatus(false);
					}
					
				}).perform();

				// Use the respone from the first transaction in the second
				load("t2", () -> sut.methodThatTakesBetweenTheseResponseTimes(50, 120)).perform();
			}
		};

		Load load = new LoadBuilder(ls)
				// 10 threads
				.amountOfThreads(10)
				// each thread will be throttled at a limit of 20 transactions/second
				.throttle(2, PER_SECOND, PER_THREAD)
				// start the 10 threads under 30 seconds
				.rampup(10 * SECOND)
				// execute the test under 30 seconds
				.stopDecision(duration(30 * SECOND))
				.build();

		RuntimeChart chart = new RuntimeChart();
		FinishedExecution finished = new ExecutionBuilder(load).runtimeResultUser(chart).build().execute().andWait();
		Result result = finished.getReportedResultFromResultFile();
		SummaryUtils.printSimpleSummary(result, method.getName());
		new ResultChart(result);
		// wait here until the chart is closed
		chart.waitUntilClosed();
	}

}
