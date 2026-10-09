package com.samana.controller;

import com.samana.dto.AvailableDateRequest;
import com.samana.dto.AvailableDateResponse;
import com.samana.service.FestivalCalendarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

/**
 * Controller for festival calendar and available dates management
 * Provides endpoints for calendar view, festival discovery, and date management
 */
@RestController
@RequestMapping("/api/calendar")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "https://samana.com"})
public class FestivalCalendarController {

    @Autowired
    private FestivalCalendarService calendarService;

    /**
     * Create available date for an experience
     * POST /api/calendar/available-dates
     */
    @PostMapping("/available-dates")
    public ResponseEntity<Map<String, Object>> createAvailableDate(
            @Valid @RequestBody AvailableDateRequest request,
            Authentication authentication) {

        try {
            AvailableDateResponse response = calendarService.createAvailableDate(request);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "Available date created successfully");
            result.put("data", response);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Get all available dates for an experience
     * GET /api/calendar/experiences/{experienceId}/available-dates
     */
    @GetMapping("/experiences/{experienceId}/available-dates")
    public ResponseEntity<Map<String, Object>> getAvailableDatesByExperience(
            @PathVariable Long experienceId) {

        List<AvailableDateResponse> dates = calendarService.getAvailableDatesByExperience(experienceId);

        Map<String, Object> response = new HashMap<>();
        response.put("total", dates.size());
        response.put("experience_id", experienceId);
        response.put("dates", dates);

        return ResponseEntity.ok(response);
    }

    /**
     * Get available dates for an experience in a specific month
     * GET /api/calendar/experiences/{experienceId}/month/{month}/year/{year}
     */
    @GetMapping("/experiences/{experienceId}/month/{month}/year/{year}")
    public ResponseEntity<Map<String, Object>> getAvailableDatesByMonth(
            @PathVariable Long experienceId,
            @PathVariable int month,
            @PathVariable int year) {

        if (month < 1 || month > 12) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid month. Must be between 1 and 12");
            return ResponseEntity.badRequest().body(error);
        }

        List<AvailableDateResponse> dates = calendarService.getAvailableDatesByExperienceAndMonth(experienceId, month, year);

        Map<String, Object> response = new HashMap<>();
        response.put("total", dates.size());
        response.put("experience_id", experienceId);
        response.put("month", month);
        response.put("year", year);
        response.put("dates", dates);

        return ResponseEntity.ok(response);
    }

    /**
     * Get festivals for a specific month
     * GET /api/calendar/festivals/month/{month}
     */
    @GetMapping("/festivals/month/{month}")
    public ResponseEntity<Map<String, Object>> getFestivalsByMonth(@PathVariable int month) {
        if (month < 1 || month > 12) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid month. Must be between 1 and 12");
            return ResponseEntity.badRequest().body(error);
        }

        List<AvailableDateResponse> festivals = calendarService.getFestivalsByMonth(month);

        Map<String, Object> response = new HashMap<>();
        response.put("total", festivals.size());
        response.put("month", month);
        response.put("festivals", festivals);

        return ResponseEntity.ok(response);
    }

    /**
     * Get festivals for a specific month and year
     * GET /api/calendar/festivals/month/{month}/year/{year}
     */
    @GetMapping("/festivals/month/{month}/year/{year}")
    public ResponseEntity<Map<String, Object>> getFestivalsByMonthAndYear(
            @PathVariable int month,
            @PathVariable int year) {

        if (month < 1 || month > 12) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid month. Must be between 1 and 12");
            return ResponseEntity.badRequest().body(error);
        }

        List<AvailableDateResponse> festivals = calendarService.getFestivalsByMonthAndYear(month, year);

        Map<String, Object> response = new HashMap<>();
        response.put("total", festivals.size());
        response.put("month", month);
        response.put("year", year);
        response.put("festivals", festivals);

        return ResponseEntity.ok(response);
    }

    /**
     * Get festivals for a specific state and month
     * GET /api/calendar/festivals/state/{state}/month/{month}
     */
    @GetMapping("/festivals/state/{state}/month/{month}")
    public ResponseEntity<Map<String, Object>> getFestivalsByStateAndMonth(
            @PathVariable String state,
            @PathVariable int month) {

        if (month < 1 || month > 12) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid month. Must be between 1 and 12");
            return ResponseEntity.badRequest().body(error);
        }

        List<AvailableDateResponse> festivals = calendarService.getFestivalsByStateAndMonth(state, month);

        Map<String, Object> response = new HashMap<>();
        response.put("total", festivals.size());
        response.put("state", state);
        response.put("month", month);
        response.put("festivals", festivals);

        return ResponseEntity.ok(response);
    }

    /**
     * Get all festivals for a specific state
     * GET /api/calendar/festivals/state/{state}
     */
    @GetMapping("/festivals/state/{state}")
    public ResponseEntity<Map<String, Object>> getFestivalsByState(@PathVariable String state) {
        List<AvailableDateResponse> festivals = calendarService.getFestivalsByState(state);

        Map<String, Object> response = new HashMap<>();
        response.put("total", festivals.size());
        response.put("state", state);
        response.put("festivals", festivals);

        return ResponseEntity.ok(response);
    }

    /**
     * Get calendar view for a month
     * GET /api/calendar/month/{month}/view
     */
    @GetMapping("/month/{month}/view")
    public ResponseEntity<Map<String, Object>> getMonthCalendarView(@PathVariable int month) {
        if (month < 1 || month > 12) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid month. Must be between 1 and 12");
            return ResponseEntity.badRequest().body(error);
        }

        Map<String, Object> calendarView = calendarService.getMonthCalendarView(month);
        return ResponseEntity.ok(calendarView);
    }

    /**
     * Get upcoming festivals
     * GET /api/calendar/festivals/upcoming
     */
    @GetMapping("/festivals/upcoming")
    public ResponseEntity<Map<String, Object>> getUpcomingFestivals() {
        List<AvailableDateResponse> upcoming = calendarService.getUpcomingFestivals();

        Map<String, Object> response = new HashMap<>();
        response.put("total", upcoming.size());
        response.put("festivals", upcoming);

        return ResponseEntity.ok(response);
    }

    /**
     * Get all festival states
     * GET /api/calendar/festival-states
     */
    @GetMapping("/festival-states")
    public ResponseEntity<Map<String, Object>> getAllFestivalStates() {
        List<String> states = calendarService.getAllFestivalStates();

        Map<String, Object> response = new HashMap<>();
        response.put("total", states.size());
        response.put("states", states);

        return ResponseEntity.ok(response);
    }

    /**
     * Update available date
     * PUT /api/calendar/available-dates/{dateId}
     */
    @PutMapping("/available-dates/{dateId}")
    public ResponseEntity<Map<String, Object>> updateAvailableDate(
            @PathVariable Long dateId,
            @Valid @RequestBody AvailableDateRequest request,
            Authentication authentication) {

        try {
            AvailableDateResponse response = calendarService.updateAvailableDate(dateId, request);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "Available date updated successfully");
            result.put("data", response);

            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Delete available date
     * DELETE /api/calendar/available-dates/{dateId}
     */
    @DeleteMapping("/available-dates/{dateId}")
    public ResponseEntity<Map<String, Object>> deleteAvailableDate(
            @PathVariable Long dateId,
            Authentication authentication) {

        try {
            calendarService.deleteAvailableDate(dateId);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "Available date deleted successfully");

            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Get festival statistics
     * GET /api/calendar/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getFestivalStatistics() {
        Map<String, Object> stats = calendarService.getFestivalStatistics();
        return ResponseEntity.ok(stats);
    }
}
