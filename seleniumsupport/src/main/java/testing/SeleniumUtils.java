package testing;

public class SeleniumUtils {

	public static String getChromeDriverPath() {
		String env = System.getenv("CHROMEDRIVER");
		return env;
	}
}
