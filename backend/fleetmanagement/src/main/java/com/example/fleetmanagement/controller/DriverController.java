package com.example.fleetmanagement.controller;

import com.example.fleetmanagement.dto.VehicleRequest;
import com.example.fleetmanagement.entity.Vehicle;
import com.example.fleetmanagement.service.VehicleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver/vehicles")
@CrossOrigin(origins = "http://localhost:3000")
public class DriverController {

    private final VehicleService vehicleService;

    public DriverController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PutMapping("/{vehicleId}/telemetry")
    public Vehicle updateTelemetry(@PathVariable String vehicleId,
                                   @RequestBody VehicleRequest request) {

        return vehicleService.updateVehicle(vehicleId, request);
    }
}