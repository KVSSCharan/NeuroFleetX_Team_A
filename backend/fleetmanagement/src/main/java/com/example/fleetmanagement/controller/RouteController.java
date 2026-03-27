package com.example.fleetmanagement.controller;

import com.example.fleetmanagement.dto.RouteRequest;
import com.example.fleetmanagement.dto.RouteResponse;
import com.example.fleetmanagement.service.RouteOptimizationService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@CrossOrigin
public class RouteController {

    private final RouteOptimizationService routeService;

    public RouteController(RouteOptimizationService routeService) {
        this.routeService = routeService;
    }

    @PostMapping("/optimize")
    public List<RouteResponse> optimizeRoute(@RequestBody RouteRequest request) {
        return routeService.getOptimizedRoutes(request);
    }
}