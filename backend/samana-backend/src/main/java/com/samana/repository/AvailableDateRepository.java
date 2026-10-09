package com.samana.repository;

import com.samana.entity.AvailableDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for AvailableDate entity
 * Provides database operations for managing available dates and festival calendar
 */
public interface AvailableDateRepository extends JpaRepository<AvailableDate, Long> {

    // Find all available dates for an experience
    List<AvailableDate> findByExperienceIdOrderByDateAsc(Long experienceId);

    // Find available dates for an experience in a specific month
    @Query("SELECT ad FROM AvailableDate ad WHERE ad.experience.id = :experienceId " +
           "AND EXTRACT(MONTH FROM ad.date) = :month " +
           "AND EXTRACT(YEAR FROM ad.date) = :year " +
           "ORDER BY ad.date ASC")
    List<AvailableDate> findByExperienceAndMonth(
            @Param("experienceId") Long experienceId,
            @Param("month") int month,
            @Param("year") int year
    );

    // Find festival dates for a specific month
    @Query("SELECT ad FROM AvailableDate ad WHERE " +
           "ad.isFestivalDate = true AND " +
           "EXTRACT(MONTH FROM ad.date) = :month " +
           "ORDER BY ad.date ASC, ad.experience.title ASC")
    List<AvailableDate> findFestivalsByMonth(@Param("month") int month);

    // Find festival dates for a specific month and year
    @Query("SELECT ad FROM AvailableDate ad WHERE " +
           "ad.isFestivalDate = true AND " +
           "EXTRACT(MONTH FROM ad.date) = :month AND " +
           "EXTRACT(YEAR FROM ad.date) = :year " +
           "ORDER BY ad.date ASC, ad.experience.title ASC")
    List<AvailableDate> findFestivalsByMonthAndYear(
            @Param("month") int month,
            @Param("year") int year
    );

    // Find festival dates for a specific state and month
    @Query("SELECT ad FROM AvailableDate ad WHERE " +
           "ad.isFestivalDate = true AND " +
           "ad.experience.state = :state AND " +
           "EXTRACT(MONTH FROM ad.date) = :month " +
           "ORDER BY ad.date ASC")
    List<AvailableDate> findFestivalsByStateAndMonth(
            @Param("state") String state,
            @Param("month") int month
    );

    // Find all festivals for a specific state
    @Query("SELECT ad FROM AvailableDate ad WHERE " +
           "ad.isFestivalDate = true AND " +
           "ad.experience.state = :state " +
           "ORDER BY ad.date ASC")
    List<AvailableDate> findFestivalsByState(@Param("state") String state);

    // Find available dates for an experience between specific dates
    @Query("SELECT ad FROM AvailableDate ad WHERE " +
           "ad.experience.id = :experienceId AND " +
           "ad.date BETWEEN :startDate AND :endDate " +
           "ORDER BY ad.date ASC")
    List<AvailableDate> findByExperienceAndDateRange(
            @Param("experienceId") Long experienceId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Find available dates with slots available
    @Query("SELECT ad FROM AvailableDate ad WHERE " +
           "ad.experience.id = :experienceId AND " +
           "ad.availableSlots > 0 AND " +
           "ad.date >= :minDate " +
           "ORDER BY ad.date ASC")
    List<AvailableDate> findAvailableSlotsByExperience(
            @Param("experienceId") Long experienceId,
            @Param("minDate") LocalDate minDate
    );

    // Check if a date exists for an experience
    Optional<AvailableDate> findByExperienceIdAndDate(Long experienceId, LocalDate date);

    // Find all distinct states with festivals
    @Query("SELECT DISTINCT ad.experience.state FROM AvailableDate ad WHERE ad.isFestivalDate = true ORDER BY ad.experience.state")
    List<String> findAllFestivalStates();

    // Find upcoming festivals
    @Query("SELECT ad FROM AvailableDate ad WHERE " +
           "ad.isFestivalDate = true AND " +
           "ad.date >= :fromDate " +
           "ORDER BY ad.date ASC")
    List<AvailableDate> findUpcomingFestivals(@Param("fromDate") LocalDate fromDate);

    // Find count of festivals by month
    @Query("SELECT EXTRACT(MONTH FROM ad.date) as month, COUNT(ad) as count FROM AvailableDate ad " +
           "WHERE ad.isFestivalDate = true " +
           "GROUP BY EXTRACT(MONTH FROM ad.date) " +
           "ORDER BY month ASC")
    List<Object[]> findFestivalCountByMonth();
}
