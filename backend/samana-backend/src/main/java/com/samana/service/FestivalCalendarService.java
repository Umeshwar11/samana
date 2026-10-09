package com.samana.service;

import com.samana.dto.AvailableDateRequest;
import com.samana.dto.AvailableDateResponse;
import com.samana.entity.AvailableDate;
import com.samana.entity.Experience;
import com.samana.repository.AvailableDateRepository;
import com.samana.repository.ExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing festival calendar and available dates
 * Handles festival discovery by month, state, and experience
 */
@Service
public class FestivalCalendarService {

    @Autowired
    private AvailableDateRepository availableDateRepository;

    @Autowired
    private ExperienceRepository experienceRepository;

    /**
     * Create available date for an experience
     */
    public AvailableDateResponse createAvailableDate(AvailableDateRequest request) {
        Experience experience = experienceRepository.findById(request.getExperienceId())
                .orElseThrow(() -> new IllegalArgumentException("Experience not found"));

        // Check if date already exists for this experience
        Optional<AvailableDate> existing = availableDateRepository.findByExperienceIdAndDate(
                request.getExperienceId(), request.getDate());

        if (existing.isPresent()) {
            throw new IllegalArgumentException("Available date already exists for this experience on this date");
        }

        AvailableDate availableDate = new AvailableDate(
                experience,
                request.getDate(),
                request.getTotalSlots(),
                request.getFestivalName(),
                request.getIsFestivalDate()
        );

        availableDateRepository.save(availableDate);
        return convertToResponse(availableDate);
    }

    /**
     * Get all available dates for an experience
     */
    public List<AvailableDateResponse> getAvailableDatesByExperience(Long experienceId) {
        List<AvailableDate> dates = availableDateRepository.findByExperienceIdOrderByDateAsc(experienceId);
        return dates.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    /**
     * Get available dates for an experience in a specific month
     */
    public List<AvailableDateResponse> getAvailableDatesByExperienceAndMonth(Long experienceId, int month, int year) {
        List<AvailableDate> dates = availableDateRepository.findByExperienceAndMonth(experienceId, month, year);
        return dates.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    /**
     * Get festivals for a specific month (all experiences, all states)
     */
    public List<AvailableDateResponse> getFestivalsByMonth(int month) {
        List<AvailableDate> festivals = availableDateRepository.findFestivalsByMonth(month);
        return festivals.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    /**
     * Get festivals for a specific month and year
     */
    public List<AvailableDateResponse> getFestivalsByMonthAndYear(int month, int year) {
        List<AvailableDate> festivals = availableDateRepository.findFestivalsByMonthAndYear(month, year);
        return festivals.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    /**
     * Get festivals for a specific state and month
     */
    public List<AvailableDateResponse> getFestivalsByStateAndMonth(String state, int month) {
        List<AvailableDate> festivals = availableDateRepository.findFestivalsByStateAndMonth(state, month);
        return festivals.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    /**
     * Get all festivals for a specific state
     */
    public List<AvailableDateResponse> getFestivalsByState(String state) {
        List<AvailableDate> festivals = availableDateRepository.findFestivalsByState(state);
        return festivals.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    /**
     * Get calendar grid for a month (for UI display)
     * Returns grouped by state and experience
     */
    public Map<String, Object> getMonthCalendarView(int month) {
        List<AvailableDate> festivals = availableDateRepository.findFestivalsByMonth(month);

        Map<String, List<AvailableDateResponse>> groupedByState = festivals.stream()
                .map(this::convertToResponse)
                .collect(Collectors.groupingBy(
                        ad -> ad.getExperienceTitle(),
                        Collectors.toList()
                ));

        Map<String, Object> response = new HashMap<>();
        response.put("month", month);
        response.put("total_festivals", festivals.size());
        response.put("festivals", groupedByState);

        return response;
    }

    /**
     * Get upcoming festivals (next 90 days)
     */
    public List<AvailableDateResponse> getUpcomingFestivals() {
        LocalDate today = LocalDate.now();
        List<AvailableDate> upcoming = availableDateRepository.findUpcomingFestivals(today);
        return upcoming.stream()
                .limit(50)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get festivals by date range
     */
    public List<AvailableDateResponse> getFestivalsByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        List<AvailableDate> festivals = availableDateRepository.findByExperienceAndDateRange(
                null, startDate, endDate);

        return festivals.stream()
                .filter(ad -> ad.getIsFestivalDate())
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all distinct festival states
     */
    public List<String> getAllFestivalStates() {
        return availableDateRepository.findAllFestivalStates();
    }

    /**
     * Update available date
     */
    public AvailableDateResponse updateAvailableDate(Long dateId, AvailableDateRequest request) {
        AvailableDate availableDate = availableDateRepository.findById(dateId)
                .orElseThrow(() -> new IllegalArgumentException("Available date not found"));

        availableDate.setTotalSlots(request.getTotalSlots());
        availableDate.setAvailableSlots(request.getAvailableSlots());
        availableDate.setIsFestivalDate(request.getIsFestivalDate());
        availableDate.setFestivalName(request.getFestivalName());

        availableDateRepository.save(availableDate);
        return convertToResponse(availableDate);
    }

    /**
     * Delete available date
     */
    public void deleteAvailableDate(Long dateId) {
        if (!availableDateRepository.existsById(dateId)) {
            throw new IllegalArgumentException("Available date not found");
        }
        availableDateRepository.deleteById(dateId);
    }

    /**
     * Book a slot on an available date
     */
    public AvailableDateResponse bookSlot(Long dateId) {
        AvailableDate availableDate = availableDateRepository.findById(dateId)
                .orElseThrow(() -> new IllegalArgumentException("Available date not found"));

        if (!availableDate.hasAvailableSlots()) {
            throw new IllegalArgumentException("No available slots on this date");
        }

        availableDate.bookSlot();
        availableDateRepository.save(availableDate);
        return convertToResponse(availableDate);
    }

    /**
     * Cancel a booking (release a slot)
     */
    public AvailableDateResponse cancelSlot(Long dateId) {
        AvailableDate availableDate = availableDateRepository.findById(dateId)
                .orElseThrow(() -> new IllegalArgumentException("Available date not found"));

        availableDate.cancelSlot();
        availableDateRepository.save(availableDate);
        return convertToResponse(availableDate);
    }

    /**
     * Get festival statistics
     */
    public Map<String, Object> getFestivalStatistics() {
        List<String> states = getAllFestivalStates();
        Map<String, Object> stats = new HashMap<>();

        stats.put("total_festival_states", states.size());
        stats.put("states", states);

        LocalDate today = LocalDate.now();
        List<AvailableDate> upcoming = availableDateRepository.findUpcomingFestivals(today);
        stats.put("upcoming_festivals_count", upcoming.size());

        return stats;
    }

    // =====================================
    // PRIVATE HELPER METHODS
    // =====================================

    private AvailableDateResponse convertToResponse(AvailableDate availableDate) {
        AvailableDateResponse response = new AvailableDateResponse(
                availableDate.getId(),
                availableDate.getExperience().getId(),
                availableDate.getExperience().getTitle(),
                availableDate.getDate(),
                availableDate.getAvailableSlots(),
                availableDate.getTotalSlots(),
                availableDate.getIsFestivalDate(),
                availableDate.getFestivalName()
        );

        response.setCreatedAt(availableDate.getCreatedAt());
        response.setUpdatedAt(availableDate.getUpdatedAt());

        return response;
    }
}
