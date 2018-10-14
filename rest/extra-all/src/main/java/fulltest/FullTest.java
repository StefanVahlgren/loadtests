package fulltest;

import static com.loadcoder.statics.LogbackLogging.getNewLogDir;
import static com.loadcoder.statics.LogbackLogging.setResultDestination;
import static com.loadcoder.statics.StopDesisions.duration;
import static com.loadcoder.statics.ThrottleMode.PER_THREAD;
import static com.loadcoder.statics.Time.PER_MINUTE;
import static com.loadcoder.statics.Time.PER_SECOND;
import static com.loadcoder.statics.Time.SECOND;

import java.io.File;
import java.lang.reflect.Method;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;

import com.loadcoder.load.chart.logic.ResultChart;
import com.loadcoder.load.chart.logic.RuntimeChart;
import com.loadcoder.load.scenario.ExecutionBuilder;
import com.loadcoder.load.scenario.Load;
import com.loadcoder.load.scenario.LoadBuilder;
import com.loadcoder.load.scenario.LoadScenario;
import com.loadcoder.result.Result;
import com.loadcoder.result.clients.GrafanaClient;
import com.loadcoder.result.clients.InfluxDBClient;
import com.loadcoder.result.clients.InfluxDBClient.InfluxDBTestExecution;

import testing.ChromeDriverUtils;

public class FullTest {

	static String db = "stefansDB";
	static InfluxDBClient client = new InfluxDBClient("localhost", 8086, false, db);
	static GrafanaClient grafana = new GrafanaClient("localhost", 3000, false, "Basic YWRtaW46YWRtaW4=");

	@Test
	public void extraAll(Method method) {
		File logDir =  getNewLogDir("target", method.getName());
		setResultDestination(logDir);
		LoadScenario restScenario = new LoadScenario() {
			RestTemplate template = new RestTemplate();
			
			@Override
			public void loadScenario() {
				load("rest-status",
						() -> template.getForEntity("http://localhost:8080/api/status", String.class))
				.perform();
			}
		};
		
		ChromeDriverService service = ChromeDriverUtils.getChromeDriverService(logDir);
		WebDriver driver;
		try {
			service.start();
			driver = ChromeDriverUtils.getWebDriver(65522, service);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		WebDriverWait wait = new WebDriverWait(driver, 2, 20);
		
		LoadScenario chromeLs = new LoadScenario() {
			
			@Override
			public void loadScenario() {
				load("selenium-loadpage", ()-> {
					driver.get("http://localhost:8080/betting.html");
					WebElement elementToBeUpdated = driver.findElement(By.id("status"));
					wait.until(ExpectedConditions.attributeToBe(elementToBeUpdated, "status", "updated"));
				}).perform();
			}
		};
		
		long executionTime = 60 * SECOND;
		Load restLoad = new LoadBuilder(restScenario)
				.amountOfThreads(20)
				.stopDecision(duration(executionTime))
				.throttle(3, PER_SECOND, PER_THREAD)
				.rampup(40 * SECOND)
				.build();
		
		Load chromeLoad = new LoadBuilder(chromeLs)
				.stopDecision(duration(executionTime))
				.throttle(40, PER_MINUTE, PER_THREAD)
				.build();
		
		Result r = new ExecutionBuilder(chromeLoad, restLoad).runtimeResultUser(new RuntimeChart()).build()
				.execute().andWait().getReportedResultFromResultFile();
		
		driver.quit();
		service.stop();
		
		String exe = "exe" + System.currentTimeMillis();
		InfluxDBTestExecution influxDBTestExecution = client.createTestExecution(exe);

		influxDBTestExecution.writeTransactions(r.getResultLists());
		grafana.createNewDashboardFromResult(method.getName(), exe, r);

		
		ResultChart resultChart =  new ResultChart(r);
		resultChart.waitUntilClosed();
	}
}
