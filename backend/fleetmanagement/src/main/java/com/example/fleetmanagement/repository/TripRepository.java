package com.example.fleetmanagement.repository;

import com.example.fleetmanagement.entity.Trip;
import com.example.fleetmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {

    Optional<Trip> findFirstByDriverAndStatusIn(User driver, java.util.List<String> statuses);
}