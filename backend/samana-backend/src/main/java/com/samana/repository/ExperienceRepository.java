package com.samana.repository;

import com.samana.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    List<Experience> findByHostId(Long hostId);

    List<Experience> findByCategoryId(Long categoryId);

    // Search methods
    List<Experience> findByTitleContainingIgnoreCase(String title);

    List<Experience> findByDescriptionContainingIgnoreCase(String description);

    List<Experience> findByLocationContainingIgnoreCase(String location);

    List<Experience> findByStateContainingIgnoreCase(String state);

    // Price range filter
    List<Experience> findByPriceBetween(Double minPrice, Double maxPrice);

    // Duration filter
    List<Experience> findByDurationBetween(Integer minDuration, Integer maxDuration);

    // Guests filter
    @Query("SELECT e FROM Experience e WHERE e.maxGuests >= :minGuests")
    List<Experience> findByMaxGuestsGreaterThanOrEqual(@Param("minGuests") Integer minGuests);

    // Category filter (list of categories)
    List<Experience> findByCategoryIdIn(List<Long> categoryIds);

    // Complex search with multiple filters
    @Query("SELECT e FROM Experience e WHERE " +
           "(:title IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:state IS NULL OR LOWER(e.state) LIKE LOWER(CONCAT('%', :state, '%'))) AND " +
           "(:location IS NULL OR LOWER(e.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:categoryId IS NULL OR e.category.id = :categoryId) AND " +
           "(:minPrice IS NULL OR e.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR e.price <= :maxPrice) AND " +
           "(:minDuration IS NULL OR e.duration >= :minDuration) AND " +
           "(:maxDuration IS NULL OR e.duration <= :maxDuration) AND " +
           "(:minGuests IS NULL OR e.maxGuests >= :minGuests)")
    List<Experience> searchExperiences(
            @Param("title") String title,
            @Param("state") String state,
            @Param("location") String location,
            @Param("categoryId") Long categoryId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("minDuration") Integer minDuration,
            @Param("maxDuration") Integer maxDuration,
            @Param("minGuests") Integer minGuests
    );

    // Distinct states
    @Query("SELECT DISTINCT e.state FROM Experience e ORDER BY e.state")
    List<String> findAllStates();

    // Distinct locations
    @Query("SELECT DISTINCT e.location FROM Experience e ORDER BY e.location")
    List<String> findAllLocations();
}