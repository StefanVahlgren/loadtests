package testing;

import java.io.IOException;
import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import com.neovisionaries.ws.client.WebSocketException;

public class ChromeWebSocketTest {

	WebDriver driver;
	
	@Test(groups = "manual")
	public void main() {
		try {
			launchBrowser(65522);
		} catch (Exception e) {

		}
	}

	private void launchBrowser(int chromeWSPort) throws IOException, WebSocketException, InterruptedException {
		
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("useAutomationExtension", false);
		options.addArguments(Arrays.asList("--remote-debugging-port=" + chromeWSPort));
		options.addArguments("--disk-cache-size=1");

		options.addArguments("--aggressive-cache-discard");
		DesiredCapabilities crcapabilities = DesiredCapabilities.chrome();
		crcapabilities.setCapability(ChromeOptions.CAPABILITY, options);
		crcapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

		System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, "/usr/bin/chromedriver");
		ChromeDriverService service = new ChromeDriverService.Builder().usingAnyFreePort().withVerbose(true).build();
		service.start();

		driver = new RemoteWebDriver(service.getUrl(), crcapabilities);
		driver.get("http://petstore.swagger.io/v2/swagger.json");
		ChromeWebSocket ws = new ChromeWebSocket(chromeWSPort);
		String result = ws.net(false, 500, 150_000, 150_000);

		driver.navigate().to("http://google.se");
		result = ws.clearCache();
		
		ws.net(false, 1000, 150_000, 150_000);
		driver.navigate().to("http://google.se");
		
		driver.close();
		driver.quit();

		service.stop();
	}
	

}
