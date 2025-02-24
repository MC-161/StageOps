package com.operations.StageOps.controller;

import com.operations.StageOps.service.DatabaseTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseTestController {

    private final DatabaseTestService databaseTestService;

    @Autowired
    public DatabaseTestController(DatabaseTestService databaseTestService) {
        this.databaseTestService = databaseTestService;
    }

    @GetMapping("/test-db-connection")
    public String testConnection() {
        return databaseTestService.testDatabaseConnection().toString();
    }
}
