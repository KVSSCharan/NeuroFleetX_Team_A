package com.example.fleetmanagement.controller;

import com.example.fleetmanagement.dto.BookingRequest;
import com.example.fleetmanagement.entity.Booking;
import com.example.fleetmanagement.service.TripService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trips")
@CrossOrigin
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping("/book")
    public Booking createBooking(@RequestBody BookingRequest request) {
        return tripService.createBooking(request);
    }
}