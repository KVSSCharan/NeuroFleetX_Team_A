package com.example.fleetmanagement.service;

import com.example.fleetmanagement.dto.RegisterRequest;
import com.example.fleetmanagement.entity.User;

public interface UserService {

    User registerUser(RegisterRequest request);

}