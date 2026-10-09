package com.samana.controller;

import com.samana.dto.ExperiencePackageRequest;
import com.samana.dto.ExperiencePackageResponse;
import com.samana.service.ExperiencePackageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Experience Packages
 * Handles HTTP requests for package CRUD operations
 */
@RestController
@RequestMapping("/api/packages")
@CrossOrigin(origins = "*")
public class ExperiencePackageController {

    @Autowired
    private ExperiencePackageService packageService;

    // ==========================================
    // CREATE
    // ==========================================

    /**
     * Create a new package for an experience
     * POST /api/packages
     */
    @PostMapping
    public ResponseEntity<ExperiencePackageResponse> createPackage(
            @RequestParam Long experienceId,
            @Valid @RequestBody ExperiencePackageRequest request,
            Authentication authentication
    ) {
        try {
            ExperiencePackageResponse response = packageService.createPackage(experienceId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ==========================================
    // READ
    // ==========================================

    /**
     * Get a single package by ID
     * GET /api/packages/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExperiencePackageResponse> getPackageById(@PathVariable Long id) {
        try {
            ExperiencePackageResponse response = packageService.getPackageById(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all packages for an experience
     * GET /api/packages/experience/{experienceId}
     */
    @GetMapping("/experience/{experienceId}")
    public ResponseEntity<List<ExperiencePackageResponse>> getPackagesByExperienceId(
            @PathVariable Long experienceId
    ) {
        try {
            List<ExperiencePackageResponse> packages = packageService.getPackagesByExperienceId(experienceId);
            return ResponseEntity.ok(packages);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get available packages for an experience
     * GET /api/packages/experience/{experienceId}/available
     */
    @GetMapping("/experience/{experienceId}/available")
    public ResponseEntity<List<ExperiencePackageResponse>> getAvailablePackagesByExperienceId(
            @PathVariable Long experienceId
    ) {
        try {
            List<ExperiencePackageResponse> packages = packageService.getAvailablePackagesByExperienceId(experienceId);
            return ResponseEntity.ok(packages);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get featured package for an experience
     * GET /api/packages/experience/{experienceId}/featured
     */
    @GetMapping("/experience/{experienceId}/featured")
    public ResponseEntity<ExperiencePackageResponse> getFeaturedPackageByExperienceId(
            @PathVariable Long experienceId
    ) {
        try {
            ExperiencePackageResponse response = packageService.getFeaturedPackageByExperienceId(experienceId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ==========================================
    // UPDATE
    // ==========================================

    /**
     * Update an existing package
     * PUT /api/packages/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExperiencePackageResponse> updatePackage(
            @PathVariable Long id,
            @Valid @RequestBody ExperiencePackageRequest request,
            Authentication authentication
    ) {
        try {
            ExperiencePackageResponse response = packageService.updatePackage(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ==========================================
    // DELETE
    // ==========================================

    /**
     * Delete a package
     * DELETE /api/packages/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(
            @PathVariable Long id,
            Authentication authentication
    ) {
        try {
            packageService.deletePackage(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ==========================================
    // UTILITY ENDPOINTS
    // ==========================================

    /**
     * Validate if package can accommodate guests
     * GET /api/packages/{id}/validate?guests={count}
     */
    @GetMapping("/{id}/validate")
    public ResponseEntity<Boolean> validatePackageCapacity(
            @PathVariable Long id,
            @RequestParam Integer guests
    ) {
        try {
            boolean canAccommodate = packageService.canAccommodateGuests(id, guests);
            return ResponseEntity.ok(canAccommodate);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Validate package availability
     * GET /api/packages/{id}/availability
     */
    @GetMapping("/{id}/availability")
    public ResponseEntity<String> validatePackageAvailability(@PathVariable Long id) {
        try {
            packageService.validatePackageAvailability(id);
            return ResponseEntity.ok("Package is available");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Package is not available");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
