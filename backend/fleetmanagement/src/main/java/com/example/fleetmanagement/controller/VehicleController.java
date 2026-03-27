package com.example.fleetmanagement.controller;

import com.example.fleetmanagement.dto.VehicleRequest;
import com.example.fleetmanagement.entity.Vehicle;
import com.example.fleetmanagement.service.VehicleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "http://localhost:3000")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // Fleet Manager → Add vehicle
    @PostMapping("/add")
    public Vehicle addVehicle(@RequestBody VehicleRequest request) {
        return vehicleService.addVehicle(request);
    }

    // Get all vehicles
    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    // Get vehicle by registration number
    @GetMapping("/{vehicleNumber}")
    public Optional<Vehicle> getVehicleByVehicleNumber(@PathVariable String vehicleNumber) {
        return vehicleService.getVehicleByVehicleNumber(vehicleNumber);
    }

    // Update vehicle (driver telemetry or manager edits)
    @PutMapping("/{vehicleNumber}")
    public Vehicle updateVehicle(
            @PathVariable String vehicleNumber,
            @RequestBody VehicleRequest request
    ) {
        return vehicleService.updateVehicle(vehicleNumber, request);
    }

    // Delete vehicle (fleet manager)
    @DeleteMapping("/{vehicleNumber}")
    public void deleteVehicle(@PathVariable String vehicleNumber) {
        vehicleService.deleteVehicle(vehicleNumber);
    }

}