package testing;

import com.loadcoder.load.chart.logic.RuntimeChart;
import com.loadcoder.load.scenario.ExecutionBuilder;
import com.loadcoder.load.scenario.Load;
import com.loadcoder.load.scenario.LoadBuilder;
import com.loadcoder.load.scenario.LoadScenario;
import com.loadcoder.load.sut.SUT;

import static com.loadcoder.statics.Time.*;

import org.testng.annotations.Test;

import static com.loadcoder.statics.ThrottleMode.*;
import static com.loadcoder.statics.StopDesisions.*;
import static com.loadcoder.statics.LogbackLogging.*;
import static com.loadcoder.statics.SummaryUtils.*;

public class SUTTests {

	@Test
	public void testCosinus() {
		
		SUT sut = new SUT();
		LoadScenario ls = new LoadScenario() {
			public void loadScenario() {
				load("sinus-curve", ()-> sut.sleepCos(200)).perform();
			}
		};

		Load l = new LoadBuilder(ls).stopDecision(duration(60 * SECOND))
				.amountOfThreads(50)
				.throttle(2, PER_SECOND, PER_THREAD)
				.rampup(20 *SECOND)
				.build();
			
		RuntimeChart chart = new RuntimeChart();
		new ExecutionBuilder(l).runtimeResultUser(chart).build().execute();
		
		chart.waitUntilClosed();
		
	}
}
