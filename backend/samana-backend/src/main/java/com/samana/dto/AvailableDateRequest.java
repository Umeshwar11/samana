package com.samana.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * DTO for creating/updating available dates
 */
public class AvailableDateRequest {

    @NotNull(message = "Experience ID is required")
    @Positive(message = "Experience ID must be positive")
    private Long experienceId;

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Date must be today or in the future")
    private LocalDate date;

    @NotNull(message = "Total slots is required")
    @Positive(message = "Total slots must be greater than 0")
    private Integer totalSlots;

    @NotNull(message = "Available slots is required")
    @PositiveOrZero(message = "Available slots cannot be negative")
    private Integer availableSlots;

    private Boolean isFestivalDate = false;

    private String festivalName;

    // =====================================
    // CONSTRUCTORS
    // =====================================

    public AvailableDateRequest() {
    }

    public AvailableDateRequest(Long experienceId, LocalDate date, Integer totalSlots) {
        this.experienceId = experienceId;
        this.date = date;
        this.totalSlots = totalSlots;
        this.availableSlots = totalSlots;
        this.isFestivalDate = false;
    }

    // =====================================
    // GETTERS AND SETTERS
    // =====================================

    public Long getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(Long experienceId) {
        this.experienceId = experienceId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(Integer totalSlots) {
        this.totalSlots = totalSlots;
    }

    public Integer getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(Integer availableSlots) {
        this.availableSlots = availableSlots;
    }

    public Boolean getIsFestivalDate() {
        return isFestivalDate;
    }

    public void setIsFestivalDate(Boolean isFestivalDate) {
        this.isFestivalDate = isFestivalDate;
    }

    public String getFestivalName() {
        return festivalName;
    }

    public void setFestivalName(String festivalName) {
        this.festivalName = festivalName;
    }
}
