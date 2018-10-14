package testing;

import static com.loadcoder.statics.Time.*;
import static com.loadcoder.statics.ThrottleMode.*;
import static com.loadcoder.statics.StopDesisions.*;
import static com.loadcoder.statics.LogbackLogging.*;
import static com.loadcoder.statics.SummaryUtils.*;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.testng.annotations.Test;

import com.loadcoder.load.LoadUtility;
import com.loadcoder.load.chart.logic.ResultChart;
import com.loadcoder.load.scenario.ExecutionBuilder;
import com.loadcoder.load.scenario.FinishedExecution;
import com.loadcoder.load.scenario.Load;
import com.loadcoder.load.scenario.LoadBuilder;
import com.loadcoder.load.scenario.LoadScenario;
import com.loadcoder.load.sut.SUT;
import com.loadcoder.result.clients.GrafanaClient;
import com.loadcoder.result.clients.InfluxDBClient;
import com.loadcoder.result.clients.InfluxDBClient.InfluxDBTestExecution;
import com.loadcoder.statics.ThrottleMode;
import com.loadcoder.statics.TimeUnit;

import testing.utils.LocalCPUUsageProvider;

public class GrafanaReportedTests {

	static String db = "stefansDB";
	static InfluxDBClient client = new InfluxDBClient("localhost", 8086, false, db);
	static GrafanaClient grafana = new GrafanaClient("localhost", 3000, false, "Basic YWRtaW46YWRtaW4=");
	
	
	@Test(groups = "manual")
	public void simpleGrafanaReportedTest(Method method) {
	
		String executionId=  method.getName()+System.currentTimeMillis();
		InfluxDBTestExecution influxDBTestExecution = client.createTestExecution(executionId);
		grafana.createNewDashboard( method.getName(), executionId, Arrays.asList("CPU", "getInfo", "updateInfo", "deleteInfo"));
		Thread t = new LocalCPUUsageProvider().getInfluxDBUpdatingThread(influxDBTestExecution);
		t.start();
		
		SUT sut = new SUT();
		LoadScenario ls = new LoadScenario() {

			@Override
			public void loadScenario() {
				load("getInfo", () -> {
					sut.methodWhereResponseTimeFollowSomeKindOfPattern(490, 730);
				}).perform();
				load("updateInfo", () -> {
					sut.methodWhereResponseTimeFollowSomeKindOfPattern(sut, 220, 520);
				}).perform();
				load("deleteInfo", () -> {
					sut.methodWhereResponseTimeFollowSomeKindOfPattern(client, 50, 240);
				}).perform();
				
			}
		};
		
		Load l = new LoadBuilder(ls)
				.amountOfThreads(10)
				.stopDecision(duration(60 * SECOND))
				.rampup(20 * SECOND)
				.throttle(2, PER_SECOND, PER_THREAD).build();
		FinishedExecution finished = new ExecutionBuilder(l)
				.runtimeResultUser((result) -> influxDBTestExecution.writeTransactions(result)).build().execute().andWait();

		ResultChart chart = new ResultChart(finished.getReportedResultFromResultFile());
		chart.waitUntilClosed();
	}
}
