package com.example.fleetmanagement.repository;

import com.example.fleetmanagement.entity.Vehicle;
import com.example.fleetmanagement.entity.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByVehicleId(String vehicleId);

    List<Vehicle> findByStatus(VehicleStatus status);

}