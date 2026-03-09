package com.example.fleetmanagement.controller;

import com.example.fleetmanagement.entity.Vehicle;
import com.example.fleetmanagement.service.VehicleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer/vehicles")
public class CustomerController {

    private final VehicleService vehicleService;

    public CustomerController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<Vehicle> getAvailableVehicles() {
        return vehicleService.getAvailableVehicles();
    }
}