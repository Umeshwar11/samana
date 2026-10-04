package com.samana.controller;

import com.samana.dto.LocationRequest;
import com.samana.dto.LocationResponse;
import com.samana.service.LocationService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin("*")
public class LocationController {

    @Autowired
    private LocationService locationService;


    // =========================
    // CREATE LOCATION
    // =========================

    @PostMapping
    public LocationResponse createLocation(
            @Valid @RequestBody LocationRequest request) {

        return locationService.createLocation(request);
    }


    // =========================
    // GET ALL LOCATIONS
    // =========================

    @GetMapping
    public List<LocationResponse> getAllLocations() {

        return locationService.getAllLocations();
    }


    // =========================
    // GET LOCATION BY ID
    // =========================

    @GetMapping("/{id}")
    public LocationResponse getLocationById(
            @PathVariable Long id) {

        return locationService.getLocationById(id);
    }


    // =========================
    // UPDATE LOCATION
    // =========================

    @PutMapping("/{id}")
    public LocationResponse updateLocation(
            @PathVariable Long id,
            @Valid @RequestBody LocationRequest request) {

        return locationService.updateLocation(id, request);
    }

    @DeleteMapping("/{id}")
public void deleteLocation(
        @PathVariable Long id) {

    locationService.deleteLocation(id);
}
}