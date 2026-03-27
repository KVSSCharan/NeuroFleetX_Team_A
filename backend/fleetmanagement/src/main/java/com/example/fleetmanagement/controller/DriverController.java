package com.example.fleetmanagement.controller;

import com.example.fleetmanagement.dto.VehicleRequest;
import com.example.fleetmanagement.entity.Trip;
import com.example.fleetmanagement.entity.Vehicle;
import com.example.fleetmanagement.service.TripService;
import com.example.fleetmanagement.service.VehicleService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver")
@CrossOrigin(origins = "http://localhost:3000")
public class DriverController {

    private final VehicleService vehicleService;
    private final TripService tripService;

    public DriverController(VehicleService vehicleService,
                            TripService tripService) {
        this.vehicleService = vehicleService;
        this.tripService = tripService;
    }

    @GetMapping("/vehicle")
    public Vehicle getAssignedVehicle(Authentication authentication) {
        return vehicleService.getVehicleByDriverEmail(authentication.getName());
    }

    @GetMapping("/trip/current")
    public Trip getCurrentTrip(Authentication authentication) {
        return tripService.getCurrentTripByDriverEmail(authentication.getName());
    }

    // 🔥 START TRIP
    @PostMapping("/trip/start/{tripId}")
    public Trip startTrip(@PathVariable Long tripId,
                          Authentication authentication) {

        return tripService.startTrip(tripId, authentication.getName());
    }

    // 🔥 END TRIP
    @PostMapping("/trip/end/{tripId}")
    public Trip endTrip(@PathVariable Long tripId,
                        Authentication authentication) {

        return tripService.endTrip(tripId, authentication.getName());
    }

    @PutMapping("/vehicles/{vehicleNumber}/telemetry")
    public Vehicle updateTelemetry(@PathVariable String vehicleNumber,
                                   @RequestBody VehicleRequest request) {

        return vehicleService.updateVehicle(vehicleNumber, request);
    }
}