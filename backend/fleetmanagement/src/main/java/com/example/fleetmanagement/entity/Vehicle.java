package com.example.fleetmanagement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String vehicleId;

    private String vehicleName;

    private String vehicleNumber;

    private String vehicleType;

    private int modelYear;

    private String driverName;

    private String driverLicense;

    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    private int fuelLevel;

    private int speed;

    private double latitude;

    private double longitude;

    public Vehicle() {
    }

    public Long getId() {
        return id;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public int getModelYear() {
        return modelYear;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverLicense() {
        return driverLicense;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public int getFuelLevel() {
        return fuelLevel;
    }

    public int getSpeed() {
        return speed;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public void setFuelLevel(int fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}