package com.samana.service;

import com.samana.dto.ExperienceResponse;
import com.samana.entity.Experience;
import com.samana.repository.ExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for searching and filtering experiences
 * Provides advanced search capabilities with multiple filter combinations
 */
@Service
public class ExperienceSearchService {

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private ExperienceService experienceService;

    /**
     * Search experiences with multiple filter criteria
     * @param title Festival/experience name (optional)
     * @param state State (optional)
     * @param location City/location (optional)
     * @param categoryId Category ID (optional)
     * @param minPrice Minimum price (optional)
     * @param maxPrice Maximum price (optional)
     * @param minDuration Minimum duration in days (optional)
     * @param maxDuration Maximum duration in days (optional)
     * @param minGuests Minimum guest capacity (optional)
     * @return List of experiences matching criteria
     */
    public List<ExperienceResponse> searchExperiences(
            String title,
            String state,
            String location,
            Long categoryId,
            Double minPrice,
            Double maxPrice,
            Integer minDuration,
            Integer maxDuration,
            Integer minGuests) {

        // Validate price range
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }

        // Validate duration range
        if (minDuration != null && maxDuration != null && minDuration > maxDuration) {
            throw new IllegalArgumentException("Minimum duration cannot be greater than maximum duration");
        }

        List<Experience> experiences = experienceRepository.searchExperiences(
                title,
                state,
                location,
                categoryId,
                minPrice,
                maxPrice,
                minDuration,
                maxDuration,
                minGuests
        );

        return experiences.stream()
                .map(experienceService::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all distinct states for filter dropdown
     */
    public List<String> getAllStates() {
        return experienceRepository.findAllStates();
    }

    /**
     * Get all distinct locations for filter dropdown
     */
    public List<String> getAllLocations() {
        return experienceRepository.findAllLocations();
    }

    /**
     * Search by title only (quick search)
     */
    public List<ExperienceResponse> quickSearch(String query) {
        List<Experience> experiences = experienceRepository.findByTitleContainingIgnoreCase(query);
        return experiences.stream()
                .map(experienceService::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get experiences by price range
     */
    public List<ExperienceResponse> filterByPriceRange(Double minPrice, Double maxPrice) {
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
        List<Experience> experiences = experienceRepository.findByPriceBetween(minPrice, maxPrice);
        return experiences.stream()
                .map(experienceService::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get experiences by duration range
     */
    public List<ExperienceResponse> filterByDurationRange(Integer minDuration, Integer maxDuration) {
        if (minDuration > maxDuration) {
            throw new IllegalArgumentException("Minimum duration cannot be greater than maximum duration");
        }
        List<Experience> experiences = experienceRepository.findByDurationBetween(minDuration, maxDuration);
        return experiences.stream()
                .map(experienceService::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get experiences by state
     */
    public List<ExperienceResponse> filterByState(String state) {
        List<Experience> experiences = experienceRepository.findByStateContainingIgnoreCase(state);
        return experiences.stream()
                .map(experienceService::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get experiences by location
     */
    public List<ExperienceResponse> filterByLocation(String location) {
        List<Experience> experiences = experienceRepository.findByLocationContainingIgnoreCase(location);
        return experiences.stream()
                .map(experienceService::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get experiences by multiple categories
     */
    public List<ExperienceResponse> filterByCategories(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new IllegalArgumentException("Category list cannot be empty");
        }
        List<Experience> experiences = experienceRepository.findByCategoryIdIn(categoryIds);
        return experiences.stream()
                .map(experienceService::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get experiences by guest capacity
     */
    public List<ExperienceResponse> filterByGuestCapacity(Integer minGuests) {
        List<Experience> experiences = experienceRepository.findByMaxGuestsGreaterThanOrEqual(minGuests);
        return experiences.stream()
                .map(experienceService::convertToResponse)
                .collect(Collectors.toList());
    }
}
