package com.example.fleetmanagement.service;

import com.example.fleetmanagement.dto.VehicleRequest;
import com.example.fleetmanagement.entity.Vehicle;
import com.example.fleetmanagement.entity.VehicleStatus;
import com.example.fleetmanagement.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle addVehicle(VehicleRequest request) {

        Vehicle vehicle = new Vehicle();

        vehicle.setVehicleId(request.getVehicleId());
        vehicle.setVehicleName(request.getVehicleName());
        vehicle.setVehicleNumber(request.getVehicleNumber());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setModelYear(request.getModelYear());
        vehicle.setDriverName(request.getDriverName());
        vehicle.setDriverLicense(request.getDriverLicense());
        vehicle.setStatus(request.getStatus());
        vehicle.setFuelLevel(request.getFuelLevel());
        vehicle.setSpeed(request.getSpeed());
        vehicle.setLatitude(request.getLatitude());
        vehicle.setLongitude(request.getLongitude());

        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> getVehicleByVehicleId(String vehicleId) {
        return vehicleRepository.findByVehicleId(vehicleId);
    }

    public Vehicle updateVehicle(String vehicleId, VehicleRequest request) {

        Optional<Vehicle> optionalVehicle = vehicleRepository.findByVehicleId(vehicleId);

        if (optionalVehicle.isPresent()) {

            Vehicle vehicle = optionalVehicle.get();

            vehicle.setVehicleName(request.getVehicleName());
            vehicle.setVehicleNumber(request.getVehicleNumber());
            vehicle.setVehicleType(request.getVehicleType());
            vehicle.setModelYear(request.getModelYear());
            vehicle.setDriverName(request.getDriverName());
            vehicle.setDriverLicense(request.getDriverLicense());
            vehicle.setStatus(request.getStatus());
            vehicle.setFuelLevel(request.getFuelLevel());
            vehicle.setSpeed(request.getSpeed());
            vehicle.setLatitude(request.getLatitude());
            vehicle.setLongitude(request.getLongitude());

            return vehicleRepository.save(vehicle);
        }

        return null;
    }

    public void deleteVehicle(String vehicleId) {

        Optional<Vehicle> optionalVehicle = vehicleRepository.findByVehicleId(vehicleId);

        optionalVehicle.ifPresent(vehicleRepository::delete);
    }

    public List<Vehicle> getAvailableVehicles() {
        return vehicleRepository.findByStatus(VehicleStatus.AVAILABLE);
    }
}