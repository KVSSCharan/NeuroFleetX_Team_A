package com.example.fleetmanagement.service;

import com.example.fleetmanagement.dto.BookingRequest;
import com.example.fleetmanagement.entity.Booking;
import com.example.fleetmanagement.entity.Trip;
import com.example.fleetmanagement.entity.User;
import com.example.fleetmanagement.entity.Vehicle;
import com.example.fleetmanagement.entity.VehicleStatus;
import com.example.fleetmanagement.repository.BookingRepository;
import com.example.fleetmanagement.repository.TripRepository;
import com.example.fleetmanagement.repository.UserRepository;
import com.example.fleetmanagement.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class TripService {

    private final BookingRepository bookingRepository;
    private final LoadBalancerService loadBalancerService;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public TripService(BookingRepository bookingRepository,
                       LoadBalancerService loadBalancerService,
                       TripRepository tripRepository,
                       UserRepository userRepository,
                       VehicleRepository vehicleRepository) {

        this.bookingRepository = bookingRepository;
        this.loadBalancerService = loadBalancerService;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    // 🔥 BOOKING → TRIP CREATION
    public Booking createBooking(BookingRequest request) {

        Vehicle vehicle = loadBalancerService.assignVehicle(request.getPassengers());

        if (vehicle == null) {
            throw new RuntimeException("No vehicle available");
        }

        Booking booking = new Booking();

        booking.setPickupLat(request.getPickupLat());
        booking.setPickupLng(request.getPickupLng());
        booking.setDestinationLat(request.getDestinationLat());
        booking.setDestinationLng(request.getDestinationLng());
        booking.setPassengers(request.getPassengers());
        booking.setVehicle(vehicle);
        booking.setStatus("BOOKED");

        User customer = userRepository.findByEmail(request.getCustomerEmail())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        booking.setCustomer(customer);

        Booking savedBooking = bookingRepository.save(booking);

        // 🔥 FIND DRIVER
        User driver = userRepository.findAll().stream()
                .filter(u -> u.getFullName().equals(vehicle.getDriverName())
                        && u.getLicenseNo().equals(vehicle.getDriverLicense()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        // 🔥 CREATE TRIP
        Trip trip = new Trip();

        trip.setStartLat(request.getPickupLat());
        trip.setStartLng(request.getPickupLng());
        trip.setEndLat(request.getDestinationLat());
        trip.setEndLng(request.getDestinationLng());

        trip.setStatus("CREATED");
        trip.setStartTime(LocalDateTime.now());

        trip.setVehicle(vehicle);
        trip.setDriver(driver);

        tripRepository.save(trip);

        return savedBooking;
    }

    // 🔥 GET CURRENT TRIP
    public Trip getCurrentTripByDriverEmail(String email) {

        User driver = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        return tripRepository
                .findFirstByDriverAndStatusIn(
                        driver,
                        Arrays.asList("CREATED", "STARTED")
                )
                .orElse(null);
    }

    // 🔥 START TRIP
    public Trip startTrip(Long tripId, String email) {

        if (tripId == null) {
            throw new RuntimeException("Trip ID cannot be null");
        }

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (!trip.getDriver().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized driver");
        }

        trip.setStatus("STARTED");
        trip.setStartTime(LocalDateTime.now());

        Vehicle vehicle = trip.getVehicle();
        vehicle.setStatus(VehicleStatus.IN_TRANSIT);

        vehicleRepository.save(vehicle);
        return tripRepository.save(trip);
    }

    // 🔥 END TRIP
    public Trip endTrip(Long tripId, String email) {

        if (tripId == null) {
            throw new RuntimeException("Trip ID cannot be null");
        }

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (!trip.getDriver().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized driver");
        }

        trip.setStatus("COMPLETED");
        trip.setEndTime(LocalDateTime.now());

        Vehicle vehicle = trip.getVehicle();
        vehicle.setStatus(VehicleStatus.AVAILABLE);

        vehicleRepository.save(vehicle);
        return tripRepository.save(trip);
    }
}