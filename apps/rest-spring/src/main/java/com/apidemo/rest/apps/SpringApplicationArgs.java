package com.apidemo.rest.apps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Spring Boot allows you to get the command line arguments passed to the application. When
 * you have SpringApplication.run(SpringApplicationArgs.class, args), you
 * have access to the args in your beans. See MyComponent below.
 * Usage:
 * Command: ./gradlew build
 * The above command creates an executable JAR in the build/libs directory. Now you can run the executable JAR.
 * Command: java -jar build/libs/rest-spring-0.0.1.jar
 * You can pass arguments like this:
 * Command: java -jar build/libs/rest-spring-0.0.1.jar --enable arg1 arg2
 * Or
 * Command: java -jar build/libs/rest-spring-0.0.1.jar enable=true, files=["myfile.txt"]
 * You should get the same text for the enable arg and a list of arg1 and arg2.
 * @param args
 */
@SpringBootApplication
public class SpringApplicationArgs {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringApplicationArgs.class, args);
    }
}

// Using ApplicationAruments class
@Component
class MyComponent {

    private static final Logger log = LoggerFactory.getLogger(MyComponent.class);

    @Autowired
    public MyComponent(ApplicationArguments args) {
        // Step. Get if enable argument was passed in the form: --enable or
        // --enable=true
        boolean enable = args.containsOption("enable");
        if (enable)
            log.info("## > You are enable!");

        // Step. Get argument files in the form: myfile.txt or files=["myfile.txt"] or
        // enable=true, files=["myfile.txt"]
        log.info("## > extra args...");
        List<String> _args = args.getNonOptionArgs();
        if (!_args.isEmpty())
            _args.forEach(file -> log.info(file));
    }
}
