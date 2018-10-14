package server.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import server.util.RestUtils;

@RestController
@RequestMapping(value = "/api")
public class APIController {

	String tenBytes = "0123456789"; 
	
	String kiloByte = multiplyString(tenBytes, 100);
	
	String megaByte = multiplyString(kiloByte, 1000);
	
	public APIController() {
		t.start();
	}
	
	String multiplyString(String toBeMultiplied, int times){
		String result = "";
		for(int i=0; i<times; i++) {
			result = result + toBeMultiplied;
		}
		return result;
	}
	
    @RequestMapping("/backend")
    public String greeting() {
    	
    	return "BackendInformation";
    }
    int statusCounter =0;
    long last =0;
    final long STATUS_SLEEPTIME_DEFAULT = 60; 
    long statusSleepTime = STATUS_SLEEPTIME_DEFAULT;
    @RequestMapping("/status")
    public String status() {
    	RestUtils.sleep(statusSleepTime);
    	synchronized (this) {
    		statusCounter ++;
    			
		}
    	return "Up";
    }
    
    Thread t = new Thread(()-> {
    	while(true) {
    		synchronized (this) {
    			
    			if(statusCounter > 30) {
    				statusSleepTime = STATUS_SLEEPTIME_DEFAULT + (statusCounter - 30)  *4;
    			}else {
    				statusSleepTime = STATUS_SLEEPTIME_DEFAULT;
    			}
    			statusCounter =0;
    		}
    		RestUtils.sleep(1000);
    	}
    }) ;
    
    @RequestMapping("/lotsofdata")
    public String lotsofdata() {
    	return kiloByte;
    }
    
    @RequestMapping("/secure/userdata")
    public String userData() {
    	return "Secret data about the user";
    }
    
}