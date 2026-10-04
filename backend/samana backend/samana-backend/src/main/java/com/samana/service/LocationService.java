package com.samana.service;

import com.samana.dto.LocationRequest;
import com.samana.dto.LocationResponse;
import com.samana.entity.Location;
import com.samana.repository.LocationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;


    // =========================
    // CREATE LOCATION
    // =========================

    public LocationResponse createLocation(
            LocationRequest request) {

        Location location = new Location();

        location.setCountry(request.getCountry());
        location.setState(request.getState());
        location.setCity(request.getCity());
        location.setAddress(request.getAddress());
        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());

        Location saved = locationRepository.save(location);

        return convertToResponse(saved);
    }


    // =========================
    // GET ALL LOCATIONS
    // =========================

    public List<LocationResponse> getAllLocations() {

        return locationRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }


    // =========================
    // GET LOCATION BY ID
    // =========================

    public LocationResponse getLocationById(Long id) {

        Location location = locationRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Location not found"
                        ));

        return convertToResponse(location);
    }


    // =========================
    // CONVERT ENTITY → RESPONSE
    // =========================

    private LocationResponse convertToResponse(
            Location location) {

        return new LocationResponse(
                location.getId(),
                location.getCountry(),
                location.getState(),
                location.getCity(),
                location.getAddress(),
                location.getLatitude(),
                location.getLongitude()
        );
    }

    // =========================
// UPDATE LOCATION
// =========================

public LocationResponse updateLocation(
        Long id,
        LocationRequest request) {

    Location location = locationRepository
            .findById(id)
            .orElseThrow(() ->
                    new RuntimeException(
                            "Location not found"
                    ));

    location.setCountry(request.getCountry());
    location.setState(request.getState());
    location.setCity(request.getCity());
    location.setAddress(request.getAddress());
    location.setLatitude(request.getLatitude());
    location.setLongitude(request.getLongitude());

    Location updated = locationRepository.save(location);

    return convertToResponse(updated);
}
    
         // =========================
// DELETE LOCATION
// =========================

public void deleteLocation(Long id) {

    Location location = locationRepository
            .findById(id)
            .orElseThrow(() ->
                    new RuntimeException("Location not found"));

    locationRepository.delete(location);
}
}
