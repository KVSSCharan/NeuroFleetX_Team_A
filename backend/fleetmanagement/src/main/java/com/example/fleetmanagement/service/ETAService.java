package com.example.fleetmanagement.service;

import org.springframework.stereotype.Service;

@Service
public class ETAService {

    public String calculateETA(double distanceKm) {

        double averageSpeed = 50.0; // km/h

        double hours = distanceKm / averageSpeed;

        int minutes = (int) (hours * 60);

        if (minutes < 60) {
            return minutes + " min";
        }

        int hrs = minutes / 60;
        int rem = minutes % 60;

        return hrs + " hr " + rem + " min";
    }
}