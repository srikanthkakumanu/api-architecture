package com.apidemo.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariDataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;

import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DatasourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.main")
    public HikariDataSource hikariDataSource() { 
        return DataSourceBuilder
                    .create()
                    .type(HikariDataSource.class)
                    .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(HikariDataSource datasource) {
        return new JdbcTemplate(datasource);
    }
}