package com.apidemo.rest.apps;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

/**
 * Using ApplicationRunner and CommandLineRunner
 * Command: java -jar build/libs/rest-spring-0.0.1.jar --enable arg1 arg2
 * Or
 * Command: java -jar build/libs/rest-spring-0.0.1.jar enable=true, files=["myfile.txt"]
 */
@SpringBootApplication
public class SpringBootRunners implements CommandLineRunner, ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(SpringBootRunners.class.getCanonicalName());

    @Autowired
    String info;

    public static void main(String[] args) {
        log.info("STARTING : Spring boot application starting");
        SpringApplication.run(SpringBootRunners.class, args);
        log.info("STOPPED  : Spring boot application stopped");
    }

    @Bean
    String info() { return "Just a simple String bean"; }

    @Override
    /**
     * This is CommandLineRunner.run() method
     */
    public void run(String... args) throws Exception {
        log.info("## > CommandLineRunner Implementation...");
        log.info("Accessing the Info bean: " + info);
        for(String arg: args) 
            log.info(arg);
    }

    @Override
    /**
     * This is ApplicationRunner.run() method
     */
    public void run(ApplicationArguments args) throws Exception {
        log.info("## > ApplicationRunner Implementation...");
        log.info("Accessing the Info bean: " + info);
        args.getNonOptionArgs().forEach(file -> log.info(file)); 
    }

    /**
     * Java 8 - functional way using lambdas
     * 1. No need to use implements at class Level
     * 2. This way we can add many method that returns CommandLineRunner or ApplicationRunner as we want.
     * 3. If you wanted to execute in a certain order, you can use the @Order annotation.

     */
    // @Bean
    // CommandLineRunner myMethod() {
    //     return args -> {
    //         log.info("## > CommandLineRunner Implementation...");
    //         log.info("Accessing the Info bean: " + info);
    //         for (String arg : args)
    //             log.info(arg);
    //     };
    // }
}

