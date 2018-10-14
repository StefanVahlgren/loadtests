package testing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.neovisionaries.ws.client.ThreadType;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketState;

public class ChromeWebSocket {

	WebDriver driver;
	WebSocket ws = null;
	final Object waitCoordinator = new Object();
	final int messageTimeoutInSecs = 5;
	final String chromeJsonUrl;
	final String wsUrl;
	
	static final String NETWORK_THROTTLING_REQUEST_TEMPLATE = 
			"{\"id\":4,\"method\":\"Network.emulateNetworkConditions\",\"params\": {    \"offline\": %s,    \"latency\": %s,    \"downloadThroughput\": %s,    \"uploadThroughput\": %s  }}";
	
	static final String CACHE_REQUEST_TEMPLATE = 
			"{\"id\":8,\"method\":\"Network.setCacheDisabled\",\"params\": {    \"cacheDisabled\": %s}}";
	
	static final String CLEAR_CACHE_REQUEST_TEMPLATE = 
			"{\"id\":9,\"method\":\"Network.clearBrowserCache\" }";
	
	public static void main(String[] args) throws IOException, WebSocketException, InterruptedException {
		ChromeWebSocket chromeDevTools = new ChromeWebSocket(65522);
		chromeDevTools.launchBrowser(65522);
	}

	ChromeWebSocket(int port){
		this.chromeJsonUrl = String.format("http://localhost:%s/json", port);
		try {
		wsUrl = getWebSocketDebuggerUrl();
		}catch(Exception e) {
			System.out.println("fel" + e);
			throw new RuntimeException(e);
		}
	}
	protected void launchBrowser(int chromeWSPort) throws IOException, WebSocketException, InterruptedException {
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("useAutomationExtension", false);
		options.addArguments(Arrays.asList("--remote-debugging-port=" + chromeWSPort));

		DesiredCapabilities crcapabilities = DesiredCapabilities.chrome();
		crcapabilities.setCapability(ChromeOptions.CAPABILITY, options);
		crcapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

		System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, "d:/workspace3/node_selenium/chromedriver.exe");
		ChromeDriverService service = new ChromeDriverService.Builder().usingAnyFreePort().withVerbose(true).build();
		service.start();

		driver = new RemoteWebDriver(service.getUrl(), crcapabilities);
		driver.get("http://petstore.swagger.io/v2/swagger.json");
//		sendWSMessage(wsURL, networkThrottling());
		net(true, 500, 50_000, 50_000);
		driver.navigate().to("http://google.se");

		ws.disconnect();

		driver.close();
		driver.quit();

		service.stop();
	}

	private String getWebSocketDebuggerUrl() throws IOException {
		String webSocketDebuggerUrl = "";

		URL url = new URL(chromeJsonUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String json = org.apache.commons.io.IOUtils.toString(reader);
		JSONArray jsonArray = new JSONArray(json);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			if (jsonObject.getString("type").equals("page")) {
				webSocketDebuggerUrl = jsonObject.getString("webSocketDebuggerUrl");
				break;
			}
		}

		if (webSocketDebuggerUrl.equals(""))
			throw new RuntimeException("webSocketDebuggerUrl not found");
		return webSocketDebuggerUrl;
	}



	public static class WebSocketAdapterExtention extends WebSocketAdapter {
		String text;
		
		public String sync(){
			for(int i=0; i<200; i++) {
				if(text != null)
					break;
				sleep(100);
			}
			String result = text;
			this.text = null;
			return result;
		}

		public void sleep(long millis) {
			try {
				Thread.sleep(millis);
			}catch(Exception e) {  }
		}
		
		@Override
		public void onTextMessage(WebSocket ws, String message) {
			System.out.println("#########0###########" + message);
			this.text = message;
		}
		
	    @Override
	    public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception
	    {System.out.println("#########1###########");
	    }


	    @Override
	    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception
	    {System.out.println("#########2###########");
	    }


	    @Override
	    public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception
	    {System.out.println("#########3###########");
	    }


	    @Override
	    public void onDisconnected(WebSocket websocket,
	        WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
	        boolean closedByServer) throws Exception
	    {System.out.println("#########4###########");
	    }


	    @Override
	    public void onFrame(WebSocket websocket, WebSocketFrame frame) throws Exception
	    {System.out.println("#########5###########" + frame.getCloseReason() + frame.getPayloadText());
	    }


	    @Override
	    public void onContinuationFrame(WebSocket websocket, WebSocketFrame frame) throws Exception
	    {System.out.println("#########6###########");
	    }


	    @Override
	    public void onTextFrame(WebSocket websocket, WebSocketFrame frame) throws Exception
	    {System.out.println("#########7###########" + frame.getCloseReason() + frame.getPayloadText()); 
	    }


	    @Override
	    public void onBinaryFrame(WebSocket websocket, WebSocketFrame frame) throws Exception
	    {System.out.println("#########8###########");
	    }


	    @Override
	    public void onCloseFrame(WebSocket websocket, WebSocketFrame frame) throws Exception
	    {System.out.println("#########9###########");
	    }


	    @Override
	    public void onPingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception
	    {System.out.println("#########10###########");
	    }


	    @Override
	    public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception
	    {System.out.println("#########11###########");
	    }


	    @Override
	    public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception
	    {System.out.println("#########12###########");
	    }


	    @Override
	    public void onSendingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception
	    {System.out.println("#########13###########" + frame.getCloseReason() + frame.getPayloadText());
	    }


	    @Override
	    public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception
	    {System.out.println("#########14###########" + frame.getCloseReason() + frame.getPayloadText());
	    }


	    @Override
	    public void onFrameUnsent(WebSocket websocket, WebSocketFrame frame) throws Exception
	    {System.out.println("#########15###########");
	    }


	    @Override
	    public void onError(WebSocket websocket, WebSocketException cause) throws Exception
	    {System.out.println("#########16###########");
	    }


	    @Override
	    public void onFrameError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception
	    {System.out.println("#########17###########");
	    }


	    @Override
	    public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) throws Exception
	    {System.out.println("#########18###########");
	    }


	    @Override
	    public void onMessageDecompressionError(WebSocket websocket, WebSocketException cause, byte[] compressed) throws Exception
	    {System.out.println("#########19###########");
	    }


	    @Override
	    public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception
	    {System.out.println("#########20###########");
	    }


	    @Override
	    public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception
	    {System.out.println("#########21###########");
	    }


	    @Override
	    public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception
	    {System.out.println("#########1###########");
	    }


	    @Override
	    public void handleCallbackError(WebSocket websocket, Throwable cause) throws Exception
	    {System.out.println("#########22###########");
	    }


	    @Override
	    public void onSendingHandshake(WebSocket websocket, String requestLine, List<String[]> headers) throws Exception
	    {System.out.println("#########23###########");
	    }


	    @Override
	    public void onThreadCreated(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception
	    {System.out.println("#########24###########");
	    }


	    @Override
	    public void onThreadStarted(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception
	    {
	    }


	    @Override
	    public void onThreadStopping(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception
	    {
	    }
	    
	    
	    
		public String getResponseText() {
			return text;
		}
	}

	public String net(boolean offline, long latency, long download, long upload) {
		String message = String.format(NETWORK_THROTTLING_REQUEST_TEMPLATE, offline, latency, download, upload);
			return sendWSMessage(wsUrl, message).sync();
	}
	
	public String cache(boolean disabled) {
		String message = String.format(CACHE_REQUEST_TEMPLATE, disabled);
			return sendWSMessage(wsUrl, message).sync();
	}
	
	public String clearCache() {
		String message = String.format(CLEAR_CACHE_REQUEST_TEMPLATE);
			return sendWSMessage(wsUrl, message).sync();
	}
	
	WebSocketAdapterExtention ext;
	private WebSocketAdapterExtention sendWSMessage(String url, String message) {
		try {

			sleep(50);
			if (ws == null) {
				ext = new WebSocketAdapterExtention();
				ws = new WebSocketFactory().createSocket(url).addListener(ext).connect();
			}
			ws.sendText(message);
			return ext;
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void sleep(long millis) {
		try {
			Thread.sleep(millis);
		}catch(Exception e) {  }
	}
	

}
