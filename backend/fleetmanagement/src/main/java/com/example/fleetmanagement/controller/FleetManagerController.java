package com.example.fleetmanagement.controller;

import com.example.fleetmanagement.dto.VehicleRequest;
import com.example.fleetmanagement.entity.Role;
import com.example.fleetmanagement.entity.User;
import com.example.fleetmanagement.entity.Vehicle;
import com.example.fleetmanagement.repository.UserRepository;
import com.example.fleetmanagement.service.VehicleService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fleet/vehicles")
public class FleetManagerController {

    private final VehicleService vehicleService;
    private final UserRepository userRepository;

    public FleetManagerController(
            VehicleService vehicleService,
            UserRepository userRepository) {

        this.vehicleService = vehicleService;
        this.userRepository = userRepository;
    }

    // PROFILE
    @GetMapping("/profile")
    public User getProfile(Authentication authentication) {

        String email = authentication.getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // GET ALL VEHICLES
    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    // GET DRIVERS FOR DROPDOWN
    @GetMapping("/drivers")
    public List<User> getDrivers() {
        return userRepository.findByRole(Role.DRIVER);
    }

    // ADD VEHICLE
    @PostMapping
    public Vehicle addVehicle(@RequestBody VehicleRequest request) {
        return vehicleService.addVehicle(request);
    }

    // UPDATE VEHICLE
    @PutMapping("/{vehicleNumber:[A-Za-z0-9\\-]+}")
    public Vehicle updateVehicle(
            @PathVariable String vehicleNumber,
            @RequestBody VehicleRequest request) {

        return vehicleService.updateVehicle(vehicleNumber, request);
    }

    // DELETE VEHICLE
    @DeleteMapping("/{vehicleNumber:[A-Za-z0-9\\-]+}")
    public void deleteVehicle(@PathVariable String vehicleNumber) {
        vehicleService.deleteVehicle(vehicleNumber);
    }
}