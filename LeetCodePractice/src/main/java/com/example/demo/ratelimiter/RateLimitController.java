package com.example.demo.ratelimiter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimitController {
	RateLimiter rl1 = new RateLimiter(60000,5);
    @GetMapping("/rateLimit")
    public ResponseEntity<String> limitRequest() throws InterruptedException {
    	
    	System.out.println("In rateLIMIT");
        if(!rl1.store()) {
            System.out.println("Rate Limit Exceeded");
            return new ResponseEntity<>("Rate Limit Exceeded", HttpStatus.OK);
        }
        return new ResponseEntity<>("Accepted", HttpStatus.OK);
    }



}

