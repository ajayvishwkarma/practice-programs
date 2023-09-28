package com.example.demo.ratelimiter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Test {
	public static void main(String[] args) {
		 LocalDateTime now = LocalDateTime.now();
		 System.out.println(now);
		 
		 
		 System.out.println(now.withMinute(0).withSecond(0));
		 //System.out.println(now.truncatedTo(ChronoUnit.SECONDS));
		 
		 
		 
	}
}
