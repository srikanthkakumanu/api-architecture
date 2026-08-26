package com.apidemo.rest.apps;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

/**
 * This program shows passing application configuration parameters using different approaches.
 * 1. build.gradle
 *      1.1 add the following inside plugins block : id 'application'
 *      1.2 add the following: application { mainClass = 'com.apidemo.rest.ApplicationConfigParams' }
 * 
 * 2. ./gradlew build
 * 
 * 3. Use the below approaches
 * To pass a configuration parameter via command-line: java -jar build/libs/rest-spring-0.0.1.jar --server.ip=192.158.12.1
 * To pickup from application.properties/YAML file: ./gradlew bootRun
 * To use JSON for configuration parameters: java -jar build/libs/rest-spring-0.0.1.jar --spring.application.json='{"server":{"ip":"192.158.12.3"}}'
 * To use via Environment variable, we can use either of the approaches:
 *      A) SPRING_APPLICATION_JSON='{"server":{"ip":"192.158.12.4"}}' java -jar build/libs/rest-spring-0.0.1.jar
 *      B) SERVER_IP=192.158.12.5 ./gradlew bootRun
 *      C) SERVER_IP=192.158.12.6 java -jar build/libs/rest-spring-0.0.1.jar
 */
@SpringBootApplication
public class ApplicationConfigParams {
    private static Logger log = LoggerFactory.getLogger(ApplicationConfigParams.class);

    @Value("${server.ip}")
    String serverIp;
    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfigParams.class, args);       
    }

    // functional way of using CommandLineRunner
    @Bean
    CommandLineRunner values() { 
        return args -> log.info(" > The Server IP is: " + serverIp);
    }
}
