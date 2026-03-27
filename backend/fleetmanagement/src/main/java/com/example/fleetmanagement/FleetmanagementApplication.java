package com.example.fleetmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.fleetmanagement.entity")
public class FleetmanagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(FleetmanagementApplication.class, args);
    }
}