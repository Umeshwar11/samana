package com.samana.repository;

import com.samana.entity.ExperiencePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository for ExperiencePackage entity
 * Handles database operations for experience packages
 */
public interface ExperiencePackageRepository extends JpaRepository<ExperiencePackage, Long> {

    /**
     * Find all packages for a specific experience
     */
    List<ExperiencePackage> findByExperienceId(Long experienceId);

    /**
     * Find all available packages for a specific experience
     */
    @Query("SELECT p FROM ExperiencePackage p WHERE p.experience.id = :experienceId AND p.available = true")
    List<ExperiencePackage> findAvailablePackagesByExperienceId(@Param("experienceId") Long experienceId);

    /**
     * Find featured package for an experience
     */
    @Query("SELECT p FROM ExperiencePackage p WHERE p.experience.id = :experienceId AND p.isFeatured = true")
    Optional<ExperiencePackage> findFeaturedPackageByExperienceId(@Param("experienceId") Long experienceId);

    /**
     * Find packages within a price range
     */
    @Query("SELECT p FROM ExperiencePackage p WHERE p.experience.id = :experienceId AND p.price BETWEEN :minPrice AND :maxPrice AND p.available = true")
    List<ExperiencePackage> findPackagesByPriceRange(
            @Param("experienceId") Long experienceId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );

    /**
     * Find packages by duration
     */
    @Query("SELECT p FROM ExperiencePackage p WHERE p.experience.id = :experienceId AND p.duration >= :minDays AND p.duration <= :maxDays AND p.available = true")
    List<ExperiencePackage> findPackagesByDuration(
            @Param("experienceId") Long experienceId,
            @Param("minDays") Integer minDays,
            @Param("maxDays") Integer maxDays
    );

    /**
     * Find packages that can accommodate a specific number of guests
     */
    @Query("SELECT p FROM ExperiencePackage p WHERE p.experience.id = :experienceId AND p.maxGuests >= :numberOfGuests AND p.available = true")
    List<ExperiencePackage> findPackagesByGuestCount(
            @Param("experienceId") Long experienceId,
            @Param("numberOfGuests") Integer numberOfGuests
    );

    /**
     * Check if a package exists for an experience
     */
    boolean existsByExperienceIdAndId(Long experienceId, Long packageId);

    /**
     * Count packages for an experience
     */
    long countByExperienceId(Long experienceId);
}
