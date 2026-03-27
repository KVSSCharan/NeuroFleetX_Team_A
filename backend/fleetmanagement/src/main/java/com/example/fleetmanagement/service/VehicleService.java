package com.example.fleetmanagement.service;

import com.example.fleetmanagement.dto.VehicleRequest;
import com.example.fleetmanagement.entity.User;
import com.example.fleetmanagement.entity.Vehicle;
import com.example.fleetmanagement.entity.VehicleStatus;
import com.example.fleetmanagement.repository.UserRepository;
import com.example.fleetmanagement.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public VehicleService(VehicleRepository vehicleRepository,
                          UserRepository userRepository) {
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    public Vehicle addVehicle(VehicleRequest request) {

        Vehicle vehicle = new Vehicle();

        vehicle.setVehicleName(request.getVehicleName());
        vehicle.setVehicleNumber(request.getVehicleNumber());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setModelYear(request.getModelYear());
        vehicle.setDriverName(request.getDriverName());
        vehicle.setDriverLicense(request.getDriverLicense());
        vehicle.setMillage(request.getMillage());

        vehicle.setFuelLevel(request.getFuelLevel());
        vehicle.setLocation(request.getLocation());
        vehicle.setLatitude(request.getLatitude());
        vehicle.setLongitude(request.getLongitude());

        vehicle.setStatus(request.getStatus());

        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> getVehicleByVehicleNumber(String vehicleNumber) {
        return vehicleRepository.findByVehicleNumber(vehicleNumber);
    }

    public Vehicle updateVehicle(String vehicleNumber, VehicleRequest request) {

        Optional<Vehicle> optionalVehicle = vehicleRepository.findByVehicleNumber(vehicleNumber);

        if (optionalVehicle.isPresent()) {

            Vehicle vehicle = optionalVehicle.get();

            vehicle.setVehicleName(request.getVehicleName());
            vehicle.setVehicleNumber(request.getVehicleNumber());
            vehicle.setVehicleType(request.getVehicleType());
            vehicle.setModelYear(request.getModelYear());
            vehicle.setDriverName(request.getDriverName());
            vehicle.setDriverLicense(request.getDriverLicense());
            vehicle.setMillage(request.getMillage());

            vehicle.setFuelLevel(request.getFuelLevel());
            vehicle.setLocation(request.getLocation());
            vehicle.setLatitude(request.getLatitude());
            vehicle.setLongitude(request.getLongitude());

            vehicle.setStatus(request.getStatus());

            return vehicleRepository.save(vehicle);
        }

        return null;
    }

    public void deleteVehicle(String vehicleNumber) {

        Optional<Vehicle> optionalVehicle = vehicleRepository.findByVehicleNumber(vehicleNumber);

        optionalVehicle.ifPresent(vehicleRepository::delete);
    }

    public List<Vehicle> getAvailableVehicles() {
        return vehicleRepository.findByStatus(VehicleStatus.AVAILABLE);
    }

    // 🔥 NEW METHOD (IMPORTANT)
    public Vehicle getVehicleByDriverEmail(String email) {

        User driver = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        return vehicleRepository
                .findByDriverNameAndDriverLicense(
                        driver.getFullName(),
                        driver.getLicenseNo()
                )
                .orElse(null);
    }
}