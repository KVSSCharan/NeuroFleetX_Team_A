package com.example.fleetmanagement.controller;

import com.example.fleetmanagement.dto.BookingRequest;
import com.example.fleetmanagement.entity.Booking;
import com.example.fleetmanagement.entity.Vehicle;
import com.example.fleetmanagement.service.TripService;
import com.example.fleetmanagement.service.VehicleService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {

    private final VehicleService vehicleService;
    private final TripService tripService;

    public CustomerController(VehicleService vehicleService,
                              TripService tripService) {
        this.vehicleService = vehicleService;
        this.tripService = tripService;
    }

    // 🔥 1. GET AVAILABLE VEHICLES
    @GetMapping("/vehicles")
    public List<Vehicle> getAvailableVehicles() {
        return vehicleService.getAvailableVehicles();
    }

    // 🔥 2. SIMPLE RECOMMENDATION
    @GetMapping("/recommendations")
    public List<Vehicle> getRecommendedVehicles() {

        List<Vehicle> vehicles = vehicleService.getAvailableVehicles();

        // return first 3 vehicles (simple AI placeholder)
        return vehicles.stream().limit(3).toList();
    }

    // 🔥 3. BOOK VEHICLE
    @PostMapping("/book")
    public Booking bookVehicle(@RequestBody BookingRequest request,
                               Authentication authentication) {

        // attach customer
        request.setCustomerEmail(authentication.getName());

        return tripService.createBooking(request);
    }
}