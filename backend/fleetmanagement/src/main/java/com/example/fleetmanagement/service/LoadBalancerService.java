package com.example.fleetmanagement.service;

import com.example.fleetmanagement.entity.Vehicle;
import com.example.fleetmanagement.entity.VehicleStatus;
import com.example.fleetmanagement.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoadBalancerService {

    private final VehicleRepository vehicleRepository;

    public LoadBalancerService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle assignVehicle(int passengers) {

        List<Vehicle> vehicles = vehicleRepository.findAll();

        for (Vehicle v : vehicles) {

            if (v.getStatus() == VehicleStatus.AVAILABLE && v.getFuelLevel() > 20) {
                return v;
            }
        }

        return null;
    }
}