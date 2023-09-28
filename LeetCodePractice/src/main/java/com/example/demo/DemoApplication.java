package com.example.demo;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		  int desiredHour = 11;
	        int desiredMinute = 0;

	        // Get the current time
	        int currentHour = java.time.LocalTime.now().getHour();
	        int currentMinute = java.time.LocalTime.now().getMinute();

	        // Calculate the remaining time in minutes
	        int remainingMinutes = (desiredHour - currentHour) * 60 + (desiredMinute - currentMinute);

	        if (remainingMinutes <= 0) {
	            System.out.println("Working hours are already over.");
	            return;
	        }

	        // Schedule the system shutdown command
	        String shutdownCommand = "";
	        String operatingSystem = System.getProperty("os.name").toLowerCase();

	        try {
	            if (operatingSystem.contains("win")) {
	                // Windows OS
	                shutdownCommand = "shutdown.exe -s -t " + remainingMinutes * 60;
	            } else if (operatingSystem.contains("nix") || operatingSystem.contains("nux") || operatingSystem.contains("mac")) {
	                // Linux or Mac OS
	                shutdownCommand = "shutdown -h +" + remainingMinutes;
	            } else {
	                System.out.println("Unsupported operating system.");
	                return;
	            }

	            // Execute the shutdown command
	            Runtime.getRuntime().exec(shutdownCommand);

	            System.out.println("System will shut down in " + remainingMinutes + " minutes.");

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}


