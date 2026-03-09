package com.example.fleetmanagement.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.fleetmanagement.dto.LoginRequest;
import com.example.fleetmanagement.dto.RegisterRequest;
import com.example.fleetmanagement.entity.Role;
import com.example.fleetmanagement.entity.User;
import com.example.fleetmanagement.repository.UserRepository;
import com.example.fleetmanagement.security.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================
    // USER REGISTRATION
    // =========================
    @PostMapping("/signup")
    public Map<String, String> signup(@RequestBody RegisterRequest request) {

        Map<String, String> response = new HashMap<>();

        // Duplicate email check
        if (userRepository.existsByEmail(request.getEmail())) {
            response.put("message", "Email already exists!");
            return response;
        }

        // Basic required fields validation
        if (request.getName() == null || request.getName().isEmpty() ||
            request.getEmail() == null || request.getEmail().isEmpty() ||
            request.getPassword() == null || request.getPassword().isEmpty() ||
            request.getGender() == null || request.getGender().isEmpty() ||
            request.getRole() == null) {

            response.put("message", "All required fields must be filled");
            return response;
        }

        // Role specific validation
        Role role = request.getRole();

        switch (role) {

            case ADMIN:
                if (request.getPhone() == null || request.getPhone().isEmpty()) {
                    response.put("message", "Phone number is required for Admin");
                    return response;
                }
                break;

            case FLEET_MANAGER:
                if (request.getCompany() == null || request.getCompany().isEmpty() ||
                    request.getPhone() == null || request.getPhone().isEmpty()) {

                    response.put("message", "Company name and phone are required for Fleet Manager");
                    return response;
                }
                break;

            case DRIVER:
                if (request.getLicenseNo() == null || request.getLicenseNo().isEmpty() ||
                    request.getVehicleNo() == null || request.getVehicleNo().isEmpty() ||
                    request.getPhone() == null || request.getPhone().isEmpty()) {

                    response.put("message", "License number, vehicle number, and phone are required for Driver");
                    return response;
                }
                break;

            case CUSTOMER:
                if (request.getGovtId() == null || request.getGovtId().isEmpty() ||
                    request.getPhone() == null || request.getPhone().isEmpty()) {

                    response.put("message", "Government ID and phone are required for Customer");
                    return response;
                }
                break;
        }

        // Create user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setGender(request.getGender());
        user.setRole(role);

        // Role specific field assignment
        switch (role) {

            case ADMIN:
                user.setPhone(request.getPhone());
                break;

            case FLEET_MANAGER:
                user.setCompany(request.getCompany());
                user.setPhone(request.getPhone());
                break;

            case DRIVER:
                user.setLicenseNo(request.getLicenseNo());
                user.setVehicleNo(request.getVehicleNo());
                user.setPhone(request.getPhone());
                break;

            case CUSTOMER:
                user.setGovtId(request.getGovtId());
                user.setPhone(request.getPhone());
                break;
        }

        userRepository.save(user);

        response.put("message", "User registered successfully!");
        return response;
    }

    // =========================
    // USER LOGIN
    // =========================
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getRole().name());

        return response;
    }
}