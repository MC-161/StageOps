package com.operations.StageOps.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DatabaseTestService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseTestService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Object testDatabaseConnection() {
        try {
            String sql = "SELECT * FROM clients";
            List<Map<String, Object>> clients = jdbcTemplate.queryForList(sql);

            if (clients.isEmpty()) {
                return "Database connected, but no clients found.";
            }

            return clients; // Returns the list of clients in JSON format
        } catch (Exception e) {
            return "Database connection failed: " + e.getMessage();
        }
    }
}

