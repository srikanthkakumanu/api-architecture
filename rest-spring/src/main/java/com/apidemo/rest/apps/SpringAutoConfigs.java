package com.apidemo.rest.apps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This program demos auto-configuration:
 * - Enable auto-configuration: @SpringBootApplication annotation automatically enable auto-configuration by default. 
 *      Because it inherits @ComponentScan, @Configuration and @EnableAutoConfiguration annotations.
 * 
 * - Disable or Exclude auto-configuration: We can disable or Exclude specific configuration classes using 
 *      `exclude` or `excludeName` in @SpringBootApplication.
 * 
 * - Enable<Technology>: Enables specific technology
 */ 
@SpringBootApplication(excludeName={
    "org.springframework.boot.jms.autoconfigure.JmsAutoConfiguration",
    "org.springframework.boot.activemq.autoconfigure.ActiveMQAutoConfiguration",
    "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration",
    "org.springframework.boot.security.autoconfigure.servlet.SecurityAutoConfiguration"
})
// @EnableWebSocket
// @EnableScheduling
// @EnableIntegration
public class SpringAutoConfigs {

	public static void main(String[] args) {
		SpringApplication.run(SimpleSpringBootApp.class, args);
		// Or, alternative approach below
		// SpringApplication app = new SpringApplication(SimpleSpringBootApp.class);
		// app.run(args);
	}
}
