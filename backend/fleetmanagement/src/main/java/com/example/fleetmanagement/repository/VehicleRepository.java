package com.example.fleetmanagement.repository;

import com.example.fleetmanagement.entity.Vehicle;
import com.example.fleetmanagement.entity.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByVehicleNumber(String vehicleNumber);

    List<Vehicle> findByStatus(VehicleStatus status);

    // 🔥 NEW METHOD (REQUIRED FOR DRIVER MODULE)
    Optional<Vehicle> findByDriverNameAndDriverLicense(String driverName, String driverLicense);
}