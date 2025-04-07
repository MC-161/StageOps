package com.operations.StageOps.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    // Hardcoded values for database connection
    private static final String USERNAME = "in2033t38_a";  // Admin username
    private static final String PASSWORD = "FgsY3iLp-sc";  // Admin password
    private static final String DB_URL = "jdbc:mysql://sst-stuproj.city.ac.uk:3306/in2033t38?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(DB_URL);  // Using the hardcoded database URL
        dataSource.setUsername(USERNAME);  // Using the hardcoded username
        dataSource.setPassword(PASSWORD);  // Using the hardcoded password
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
