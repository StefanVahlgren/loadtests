package testing;

import static org.testng.Assert.assertNotNull;

import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

public class SeleniumLoadTest {

	@Test(groups = "manual")
	public void chromeDriverEnvPropertyTest() {
		String chromeDriverPath = SeleniumUtils.getChromeDriverPath();
		System.out.println("CHROMEDRIVER:" +chromeDriverPath);
		assertNotNull(chromeDriverPath);
	}
	
	@Test(groups = "manual")
	public void testing() throws Exception{
		
        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, System.getProperty("user.dir") + "/target/chromedriver.log");
		System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, SeleniumUtils.getChromeDriverPath());
		ChromeOptions options = new ChromeOptions();
		
		DesiredCapabilities crcapabilities = DesiredCapabilities.chrome();
		crcapabilities.setCapability(ChromeOptions.CAPABILITY, options);
		crcapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		
		ChromeDriverService service = new ChromeDriverService.Builder().usingAnyFreePort().withVerbose(true).build();
		service.start();
		
		service.stop();
	}
}
