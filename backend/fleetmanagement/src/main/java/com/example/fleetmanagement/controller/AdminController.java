package com.example.fleetmanagement.controller;

import com.example.fleetmanagement.entity.Vehicle;
import com.example.fleetmanagement.service.VehicleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/vehicles")
public class AdminController {

    private final VehicleService vehicleService;

    public AdminController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }
}