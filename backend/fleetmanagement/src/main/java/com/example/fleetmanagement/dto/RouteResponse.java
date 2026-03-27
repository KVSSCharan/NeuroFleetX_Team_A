package com.example.fleetmanagement.dto;

import java.util.List;

public class RouteResponse {

    private double distance;
    private String eta;
    private List<List<Double>> coordinates;

    public RouteResponse(double distance, String eta, List<List<Double>> coordinates) {
        this.distance = distance;
        this.eta = eta;
        this.coordinates = coordinates;
    }

    public double getDistance() {
        return distance;
    }

    public String getEta() {
        return eta;
    }

    public List<List<Double>> getCoordinates() {
        return coordinates;
    }
}