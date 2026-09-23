package com.apidemo.dataquerypatterns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class DataQueryPatternsApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataQueryPatternsApplication.class, args);
    }
}
