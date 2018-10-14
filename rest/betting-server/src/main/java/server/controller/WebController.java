package server.controller;


import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import integration.BackEndIntegration;
import dto.Customer;
import dto.Greeting;
import dto.OddsList;

@RestController
public class WebController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    long sleepTime = 1000;
    
    BackEndIntegration backend = new BackEndIntegration();
    
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
    	
    	Customer info = backend.fetchUserData();
    	
    	return new Greeting(counter.incrementAndGet(), info);
    }
    
    @RequestMapping("/odds")
    public OddsList odds() {
    	
    	return new OddsList();
    }
    
    void sleep(long millis) {
    	try {
    		Thread.sleep(millis);
    	}catch(InterruptedException i) {}
    }
    
}