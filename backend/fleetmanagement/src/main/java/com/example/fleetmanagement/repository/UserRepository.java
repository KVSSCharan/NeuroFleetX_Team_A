package com.example.fleetmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fleetmanagement.entity.User;
import com.example.fleetmanagement.entity.Role;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email (used during login)
    Optional<User> findByEmail(String email);

    // Check if email already exists (used during registration)
    boolean existsByEmail(String email);

    // Get users by role (useful for admin dashboard)
    List<User> findByRole(Role role);

}