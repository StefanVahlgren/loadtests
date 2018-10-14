package server.util;

import java.io.IOException;
import java.util.Base64;

import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class RestUtils {


	public static HttpHeaders createHeaders(String username, String password){
		   return new HttpHeaders() {{
		         String auth = username + ":" + password;

		         String unEncoded = username + ":" + password;
		         byte[] unEncodedBytes = unEncoded.getBytes();
		         
		         byte[] encodedBytes = Base64.getEncoder().encode(unEncodedBytes);
		         String encoded = new String(encodedBytes);
		         String authHeader = "Basic " +encoded;
		         set( "Authorization", authHeader );
		      }};
		}
	
	public static ResponseErrorHandler getErrorHandler(){
		
		ResponseErrorHandler erro = new ResponseErrorHandler() {
			
			@Override
			public boolean hasError(ClientHttpResponse response) throws IOException {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
				// TODO Auto-generated method stub
				
			}
		};
		return erro;
	}
	
    public static void sleep(long millis) {
    	try {
    		Thread.sleep(millis);
    	}catch(InterruptedException i) {}
    }
    
}
