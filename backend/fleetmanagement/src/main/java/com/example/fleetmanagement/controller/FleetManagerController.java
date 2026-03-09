package com.example.fleetmanagement.controller;

import com.example.fleetmanagement.dto.VehicleRequest;
import com.example.fleetmanagement.entity.Vehicle;
import com.example.fleetmanagement.service.VehicleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fleet/vehicles")
public class FleetManagerController {

    private final VehicleService vehicleService;

    public FleetManagerController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public Vehicle addVehicle(@RequestBody VehicleRequest request) {
        return vehicleService.addVehicle(request);
    }

    @PutMapping("/{vehicleId}")
    public Vehicle updateVehicle(@PathVariable String vehicleId,
                                 @RequestBody VehicleRequest request) {
        return vehicleService.updateVehicle(vehicleId, request);
    }

    @DeleteMapping("/{vehicleId}")
    public void deleteVehicle(@PathVariable String vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
    }
}