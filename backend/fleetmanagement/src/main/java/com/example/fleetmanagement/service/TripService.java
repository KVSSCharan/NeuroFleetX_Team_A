package com.example.fleetmanagement.service;

import com.example.fleetmanagement.dto.BookingRequest;
import com.example.fleetmanagement.entity.*;
import com.example.fleetmanagement.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Service
public class TripService {

    private final BookingRepository bookingRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final LoadBalancerService loadBalancerService;
    private final ETAService etaService;

    public TripService(
            BookingRepository bookingRepository,
            VehicleRepository vehicleRepository,
            UserRepository userRepository,
            TripRepository tripRepository,
            LoadBalancerService loadBalancerService,
            ETAService etaService
    ) {
        this.bookingRepository = bookingRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.loadBalancerService = loadBalancerService;
        this.etaService = etaService;
    }

    // 🔥 CREATE BOOKING + TRIP
    public Booking createBooking(BookingRequest request) {

        User customer = userRepository.findByEmail(request.getCustomerEmail())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Vehicle vehicle = loadBalancerService.assignVehicle(request.getPassengers());

        // 🔥 CREATE BOOKING
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setVehicle(vehicle);
        booking.setPickupLat(request.getPickupLat());
        booking.setPickupLng(request.getPickupLng());
        booking.setDestinationLat(request.getDestinationLat());
        booking.setDestinationLng(request.getDestinationLng());
        booking.setPassengers(request.getPassengers());
        booking.setStatus("BOOKED");

        bookingRepository.save(booking);

        // 🔥 CREATE TRIP
        Trip trip = new Trip();
        trip.setVehicle(vehicle);
        trip.setDriver(vehicle.getDriver());

        trip.setStartLat(request.getPickupLat());
        trip.setStartLng(request.getPickupLng());
        trip.setEndLat(request.getDestinationLat());
        trip.setEndLng(request.getDestinationLng());

        double distance = calculateDistance(
                request.getPickupLat(),
                request.getPickupLng(),
                request.getDestinationLat(),
                request.getDestinationLng()
        );

        trip.setDistance(distance);
        trip.setEta(etaService.calculateETA(distance));
        trip.setStatus("ASSIGNED");

        tripRepository.save(trip);

        // 🔥 UPDATE VEHICLE STATUS (FIXED HERE)
        vehicle.setStatus(VehicleStatus.IN_TRANSIT);
        vehicleRepository.save(vehicle);

        return booking;
    }

    // 🔥 GET DRIVER CURRENT TRIP
    public Trip getCurrentTripByDriverEmail(String email) {

        User driver = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        return tripRepository
                .findByDriverAndStatus(driver, "ASSIGNED")
                .orElse(null);
    }

    // 🔥 START TRIP
    public Trip startTrip(@NonNull Long tripId, String email) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        trip.setStatus("ONGOING");
        trip.setStartTime(LocalDateTime.now());

        return tripRepository.save(trip);
    }

    // 🔥 END TRIP
    public Trip endTrip(@NonNull Long tripId, String email) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        trip.setStatus("COMPLETED");
        trip.setEndTime(LocalDateTime.now());

        Vehicle vehicle = trip.getVehicle();

        // 🔥 BACK TO AVAILABLE
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        vehicleRepository.save(vehicle);

        return tripRepository.save(trip);
    }

    // 🔥 DISTANCE CALC
    private double calculateDistance(double lat1, double lon1,
                                     double lat2, double lon2) {

        double R = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}