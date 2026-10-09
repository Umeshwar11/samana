package com.samana.controller;

import com.samana.dto.ExperienceResponse;
import com.samana.service.ExperienceSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Controller for advanced search and filtering of experiences
 * Provides multiple endpoints for discovering experiences
 */
@RestController
@RequestMapping("/api/experiences")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "https://samana.com"})
public class ExperienceSearchController {

    @Autowired
    private ExperienceSearchService searchService;

    /**
     * Advanced search with multiple filter criteria
     * GET /api/experiences/search?title=...&state=...&location=...&categoryId=...&minPrice=...&maxPrice=...&minDuration=...&maxDuration=...&minGuests=...
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchExperiences(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minDuration,
            @RequestParam(required = false) Integer maxDuration,
            @RequestParam(required = false) Integer minGuests) {

        try {
            List<ExperienceResponse> results = searchService.searchExperiences(
                    title, state, location, categoryId,
                    minPrice, maxPrice, minDuration, maxDuration, minGuests
            );

            Map<String, Object> response = new HashMap<>();
            response.put("total", results.size());
            response.put("results", results);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Quick search by title (festival/experience name)
     * GET /api/experiences/quick-search?q=Holi
     */
    @GetMapping("/quick-search")
    public ResponseEntity<Map<String, Object>> quickSearch(
            @RequestParam(value = "q", required = true) String query) {

        List<ExperienceResponse> results = searchService.quickSearch(query);

        Map<String, Object> response = new HashMap<>();
        response.put("total", results.size());
        response.put("results", results);

        return ResponseEntity.ok(response);
    }

    /**
     * Filter experiences by price range
     * GET /api/experiences/filter/price?minPrice=1000&maxPrice=5000
     */
    @GetMapping("/filter/price")
    public ResponseEntity<Map<String, Object>> filterByPrice(
            @RequestParam(value = "minPrice", required = true) Double minPrice,
            @RequestParam(value = "maxPrice", required = true) Double maxPrice) {

        try {
            List<ExperienceResponse> results = searchService.filterByPriceRange(minPrice, maxPrice);

            Map<String, Object> response = new HashMap<>();
            response.put("total", results.size());
            response.put("results", results);
            response.put("priceRange", Map.of("min", minPrice, "max", maxPrice));

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Filter experiences by duration range
     * GET /api/experiences/filter/duration?minDuration=1&maxDuration=7
     */
    @GetMapping("/filter/duration")
    public ResponseEntity<Map<String, Object>> filterByDuration(
            @RequestParam(value = "minDuration", required = true) Integer minDuration,
            @RequestParam(value = "maxDuration", required = true) Integer maxDuration) {

        try {
            List<ExperienceResponse> results = searchService.filterByDurationRange(minDuration, maxDuration);

            Map<String, Object> response = new HashMap<>();
            response.put("total", results.size());
            response.put("results", results);
            response.put("durationRange", Map.of("min", minDuration, "max", maxDuration));

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Filter experiences by state
     * GET /api/experiences/filter/state?state=Rajasthan
     */
    @GetMapping("/filter/state")
    public ResponseEntity<Map<String, Object>> filterByState(
            @RequestParam(value = "state", required = true) String state) {

        List<ExperienceResponse> results = searchService.filterByState(state);

        Map<String, Object> response = new HashMap<>();
        response.put("total", results.size());
        response.put("results", results);
        response.put("state", state);

        return ResponseEntity.ok(response);
    }

    /**
     * Filter experiences by location
     * GET /api/experiences/filter/location?location=Jaipur
     */
    @GetMapping("/filter/location")
    public ResponseEntity<Map<String, Object>> filterByLocation(
            @RequestParam(value = "location", required = true) String location) {

        List<ExperienceResponse> results = searchService.filterByLocation(location);

        Map<String, Object> response = new HashMap<>();
        response.put("total", results.size());
        response.put("results", results);
        response.put("location", location);

        return ResponseEntity.ok(response);
    }

    /**
     * Filter experiences by categories
     * GET /api/experiences/filter/categories?ids=1&ids=2&ids=3
     */
    @GetMapping("/filter/categories")
    public ResponseEntity<Map<String, Object>> filterByCategories(
            @RequestParam(value = "ids", required = true) List<Long> categoryIds) {

        try {
            List<ExperienceResponse> results = searchService.filterByCategories(categoryIds);

            Map<String, Object> response = new HashMap<>();
            response.put("total", results.size());
            response.put("results", results);
            response.put("categoryCount", categoryIds.size());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Filter experiences by guest capacity
     * GET /api/experiences/filter/guests?minGuests=4
     */
    @GetMapping("/filter/guests")
    public ResponseEntity<Map<String, Object>> filterByGuests(
            @RequestParam(value = "minGuests", required = true) Integer minGuests) {

        List<ExperienceResponse> results = searchService.filterByGuestCapacity(minGuests);

        Map<String, Object> response = new HashMap<>();
        response.put("total", results.size());
        response.put("results", results);
        response.put("minGuests", minGuests);

        return ResponseEntity.ok(response);
    }

    /**
     * Get all available states for filter dropdown
     * GET /api/experiences/filter-options/states
     */
    @GetMapping("/filter-options/states")
    public ResponseEntity<Map<String, Object>> getAllStates() {
        List<String> states = searchService.getAllStates();

        Map<String, Object> response = new HashMap<>();
        response.put("total", states.size());
        response.put("states", states);

        return ResponseEntity.ok(response);
    }

    /**
     * Get all available locations for filter dropdown
     * GET /api/experiences/filter-options/locations
     */
    @GetMapping("/filter-options/locations")
    public ResponseEntity<Map<String, Object>> getAllLocations() {
        List<String> locations = searchService.getAllLocations();

        Map<String, Object> response = new HashMap<>();
        response.put("total", locations.size());
        response.put("locations", locations);

        return ResponseEntity.ok(response);
    }
}
