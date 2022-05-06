package com.example.shoppingmanagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

@Configuration
public class TestConfiguration {

    @Container
    private static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:5.7")
            .withDatabaseName("test-order")
            .withUsername("test")
            .withPassword("test1234!")
            .withInitScript("schema.sql");

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;
    private static NamedParameterJdbcTemplate jdbcTemplate;

    public static void beforeAll() {
        config.setJdbcUrl(mysql.getJdbcUrl());
        config.setUsername(mysql.getUsername());
        config.setPassword(mysql.getPassword());
        dataSource = new HikariDataSource(config);
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
}
