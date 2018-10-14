package testing;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.neovisionaries.ws.client.WebSocketException;

public class ChromeDriverUtils {

	
	public static ChromeDriverService getChromeDriverService(File chromeDriverLogDir) {

//		options.addArguments("disable-application-cache");
		System.setProperty("webdriver.chrome.logfile", new File(chromeDriverLogDir, "chromedriver.log").getAbsolutePath());
		System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, SeleniumUtils.getChromeDriverPath());
		ChromeDriverService service = new ChromeDriverService.Builder().usingAnyFreePort().withVerbose(true).build();
		return service;
	}
	
	public static WebDriver getWebDriver(int chromeWSPort, ChromeDriverService service) throws IOException, WebSocketException, InterruptedException {
		
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("useAutomationExtension", false);
		options.addArguments(Arrays.asList("--remote-debugging-port=" + chromeWSPort));
		options.addArguments("--disable-application-cache");
		
		DesiredCapabilities crcapabilities = DesiredCapabilities.chrome();
		crcapabilities.setCapability(ChromeOptions.CAPABILITY, options);
		crcapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

		WebDriver driver = new RemoteWebDriver(service.getUrl(), crcapabilities);
		
		return driver;
	}
	
	public static ChromeWebSocket getChromeWebSocket(int chromeWSPort) {
		ChromeWebSocket ws = new ChromeWebSocket(chromeWSPort);
		return ws;
	}
}
