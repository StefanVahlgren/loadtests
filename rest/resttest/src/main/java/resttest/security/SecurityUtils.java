package resttest.security;

import java.util.Base64;

import org.springframework.http.HttpHeaders;

public class SecurityUtils {

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
}
