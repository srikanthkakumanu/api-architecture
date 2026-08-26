package com.apidemo.rest.apps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class SpringBootCustomProps {

    private static final Logger log = LoggerFactory.getLogger(SpringBootCustomProps.class);

    @Value("${myapp.server-ip}")
    String serverIp;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootCustomProps.class, args);
    }

    @Autowired
    MyAppProperties props;

    @Bean
    CommandLineRunner props() { return args -> { 
        log.info(" > The Server IP is: " + serverIp);
        log.info(" > App Name: " + props.getName());
        log.info(" > App Info: " + props.getDescription());
    };}

    @Component
    @ConfigurationProperties(prefix="myapp")
    public static class MyAppProperties {
        private String name;
        private String description;
        private String serverIp;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public String getServerIp() {
            return serverIp;
        }
        public void setServerIp(String serverIp) {
            this.serverIp = serverIp;
        }
    }  
}
