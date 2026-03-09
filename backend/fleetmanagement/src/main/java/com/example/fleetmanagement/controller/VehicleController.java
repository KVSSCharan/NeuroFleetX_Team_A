package com.example.fleetmanagement.controller;

import com.example.fleetmanagement.entity.Vehicle;
import com.example.fleetmanagement.service.VehicleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/{vehicleId}")
    public Optional<Vehicle> getVehicleByVehicleId(@PathVariable String vehicleId) {
        return vehicleService.getVehicleByVehicleId(vehicleId);
    }
}