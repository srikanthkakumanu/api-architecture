package com.apidemo.rest.apps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This program demos the bootstrap for the Spring Boot application that is 
 * executed in the main method. You need to pass the class that is executed.
 */ 
@SpringBootApplication
public class SimpleSpringBootApp {

	public static void main(String[] args) {
		SpringApplication.run(SimpleSpringBootApp.class, args);
		// Or, alternative approach below
		// SpringApplication app = new SpringApplication(SimpleSpringBootApp.class);
		// app.run(args);
	}

	
}
