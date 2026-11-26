package com.apidevops.dev.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;

@RestController
public class HealthController {

    private final DataSource dataSource;

    @Value("${app.environment}")
    private String environment;

    public HealthController(
            DataSource dataSource
    ) {
        this.dataSource = dataSource;
    }

    @GetMapping("/health")
    public Map<String, String> health()
            throws Exception {

        String databaseStatus;

        try (
                Connection ignored =
                        dataSource.getConnection()
        ) {

            databaseStatus = "UP";

        } catch (Exception e) {

            databaseStatus = "DOWN";
        }

        return Map.of(
                "status",
                "UP",

                "database",
                databaseStatus,

                "environment",
                environment,

                "version",
                "v1"
        );
    }
}