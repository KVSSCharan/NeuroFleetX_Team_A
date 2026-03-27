package com.example.fleetmanagement.service;

import com.example.fleetmanagement.dto.RegisterRequest;
import com.example.fleetmanagement.entity.User;
import com.example.fleetmanagement.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(RegisterRequest request) {

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Map DTO → Entity
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setGender(request.getGender());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRole());

        // Role specific fields
        user.setCompanyName(request.getCompanyName());
        user.setLicenseNo(request.getLicenseNo());
        user.setGovtId(request.getGovtId());

        // Save user
        return userRepository.save(user);
    }
}