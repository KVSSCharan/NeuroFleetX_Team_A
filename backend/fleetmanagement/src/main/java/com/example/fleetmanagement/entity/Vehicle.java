package com.example.fleetmanagement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_name")
    private String vehicleName;

    @Column(name = "vehicle_registration_number", unique = true)
    private String vehicleNumber;

    @Column(name = "driver_name")
    private String driverName;

    @Column(name = "driver_license_number")
    private String driverLicense;

    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "model_year")
    private int modelYear;

    @Column(name = "millage")
    private double millage;

    // Telemetry fields (updated by driver)

    @Column(name = "fuel_level")
    private int fuelLevel;

    private String location;

    private double latitude;

    private double longitude;

    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    public Vehicle() {}

    public Long getId() {
        return id;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverLicense() {
        return driverLicense;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public int getModelYear() {
        return modelYear;
    }

    public double getMillage() {
        return millage;
    }

    public int getFuelLevel() {
        return fuelLevel;
    }

    public String getLocation() {
        return location;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }

    public void setMillage(double millage) {
        this.millage = millage;
    }

    public void setFuelLevel(int fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }
}