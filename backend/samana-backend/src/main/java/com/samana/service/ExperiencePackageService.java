package com.samana.service;

import com.samana.dto.ExperiencePackageRequest;
import com.samana.dto.ExperiencePackageResponse;
import com.samana.entity.Experience;
import com.samana.entity.ExperiencePackage;
import com.samana.repository.ExperiencePackageRepository;
import com.samana.repository.ExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing experience packages
 * Handles business logic, validation, and data access for packages
 */
@Service
public class ExperiencePackageService {

    @Autowired
    private ExperiencePackageRepository packageRepository;

    @Autowired
    private ExperienceRepository experienceRepository;

    // ==========================================
    // CREATE
    // ==========================================

    /**
     * Create a new package for an experience
     */
    public ExperiencePackageResponse createPackage(Long experienceId, ExperiencePackageRequest request) {
        // Validate experience exists
        Experience experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new IllegalArgumentException("Experience not found: " + experienceId));

        // Validate request
        validatePackageRequest(request);

        // Create package
        ExperiencePackage package_ = new ExperiencePackage();
        package_.setName(request.getName());
        package_.setDescription(request.getDescription());
        package_.setPrice(request.getPrice());
        package_.setDuration(request.getDuration());
        package_.setMaxGuests(request.getMaxGuests());
        package_.setIncluded(request.getIncluded());
        package_.setNotIncluded(request.getNotIncluded());
        package_.setDescription_long(request.getDescription_long());
        package_.setAvailable(request.getAvailable() != null ? request.getAvailable() : true);
        package_.setIsFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false);
        package_.setExperience(experience);

        // Save
        ExperiencePackage saved = packageRepository.save(package_);

        return convertToResponse(saved);
    }

    // ==========================================
    // READ
    // ==========================================

    /**
     * Get package by ID
     */
    public ExperiencePackageResponse getPackageById(Long packageId) {
        ExperiencePackage package_ = packageRepository.findById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found: " + packageId));
        return convertToResponse(package_);
    }

    /**
     * Get all packages for an experience
     */
    public List<ExperiencePackageResponse> getPackagesByExperienceId(Long experienceId) {
        // Validate experience exists
        if (!experienceRepository.existsById(experienceId)) {
            throw new IllegalArgumentException("Experience not found: " + experienceId);
        }

        List<ExperiencePackage> packages = packageRepository.findByExperienceId(experienceId);
        return packages.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get available packages for an experience
     */
    public List<ExperiencePackageResponse> getAvailablePackagesByExperienceId(Long experienceId) {
        if (!experienceRepository.existsById(experienceId)) {
            throw new IllegalArgumentException("Experience not found: " + experienceId);
        }

        List<ExperiencePackage> packages = packageRepository.findAvailablePackagesByExperienceId(experienceId);
        return packages.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get featured package for an experience
     */
    public ExperiencePackageResponse getFeaturedPackageByExperienceId(Long experienceId) {
        if (!experienceRepository.existsById(experienceId)) {
            throw new IllegalArgumentException("Experience not found: " + experienceId);
        }

        ExperiencePackage package_ = packageRepository.findFeaturedPackageByExperienceId(experienceId)
                .orElseThrow(() -> new IllegalArgumentException("No featured package found for experience: " + experienceId));

        return convertToResponse(package_);
    }

    // ==========================================
    // UPDATE
    // ==========================================

    /**
     * Update an existing package
     */
    public ExperiencePackageResponse updatePackage(Long packageId, ExperiencePackageRequest request) {
        // Validate request
        validatePackageRequest(request);

        // Get package
        ExperiencePackage package_ = packageRepository.findById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found: " + packageId));

        // Update fields
        package_.setName(request.getName());
        package_.setDescription(request.getDescription());
        package_.setPrice(request.getPrice());
        package_.setDuration(request.getDuration());
        package_.setMaxGuests(request.getMaxGuests());
        package_.setIncluded(request.getIncluded());
        package_.setNotIncluded(request.getNotIncluded());
        package_.setDescription_long(request.getDescription_long());
        package_.setAvailable(request.getAvailable() != null ? request.getAvailable() : true);
        package_.setIsFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false);

        // Save
        ExperiencePackage updated = packageRepository.save(package_);

        return convertToResponse(updated);
    }

    // ==========================================
    // DELETE
    // ==========================================

    /**
     * Delete a package
     */
    public void deletePackage(Long packageId) {
        if (!packageRepository.existsById(packageId)) {
            throw new IllegalArgumentException("Package not found: " + packageId);
        }
        packageRepository.deleteById(packageId);
    }

    // ==========================================
    // VALIDATION
    // ==========================================

    /**
     * Validate package request data
     */
    private void validatePackageRequest(ExperiencePackageRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Package request cannot be null");
        }

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Package name is required");
        }

        if (request.getPrice() == null || request.getPrice() <= 0) {
            throw new IllegalArgumentException("Package price must be greater than 0");
        }

        if (request.getDuration() == null || request.getDuration() <= 0) {
            throw new IllegalArgumentException("Package duration must be greater than 0");
        }

        if (request.getDuration() > 365) {
            throw new IllegalArgumentException("Package duration cannot exceed 365 days");
        }

        if (request.getMaxGuests() == null || request.getMaxGuests() <= 0) {
            throw new IllegalArgumentException("Max guests must be greater than 0");
        }
    }

    /**
     * Validate package availability for booking
     */
    public void validatePackageAvailability(Long packageId) {
        ExperiencePackage package_ = packageRepository.findById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found: " + packageId));

        if (!package_.getAvailable()) {
            throw new IllegalArgumentException("Package is not available for booking");
        }
    }

    /**
     * Check if package can accommodate guests
     */
    public boolean canAccommodateGuests(Long packageId, Integer numberOfGuests) {
        ExperiencePackage package_ = packageRepository.findById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found: " + packageId));

        return package_.getMaxGuests() >= numberOfGuests;
    }

    // ==========================================
    // HELPER METHODS
    // ==========================================

    /**
     * Convert entity to response DTO
     */
    private ExperiencePackageResponse convertToResponse(ExperiencePackage package_) {
        return new ExperiencePackageResponse(
                package_.getId(),
                package_.getName(),
                package_.getDescription(),
                package_.getPrice(),
                package_.getDuration(),
                package_.getMaxGuests(),
                package_.getIncluded(),
                package_.getNotIncluded(),
                package_.getDescription_long(),
                package_.getAvailable(),
                package_.getIsFeatured(),
                package_.getExperience().getId(),
                package_.getCreatedAt(),
                package_.getUpdatedAt()
        );
    }
}
