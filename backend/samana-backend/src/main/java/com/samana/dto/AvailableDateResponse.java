package com.samana.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for returning available date data
 */
public class AvailableDateResponse {

    private Long id;
    private Long experienceId;
    private String experienceTitle;
    private LocalDate date;
    private Integer availableSlots;
    private Integer totalSlots;
    private Boolean isFestivalDate;
    private String festivalName;
    private Integer dayOfMonth;
    private Integer monthValue;
    private Integer year;
    private String dayOfWeek;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // =====================================
    // CONSTRUCTORS
    // =====================================

    public AvailableDateResponse() {
    }

    public AvailableDateResponse(Long id, Long experienceId, String experienceTitle, LocalDate date,
                                Integer availableSlots, Integer totalSlots, Boolean isFestivalDate,
                                String festivalName) {
        this.id = id;
        this.experienceId = experienceId;
        this.experienceTitle = experienceTitle;
        this.date = date;
        this.availableSlots = availableSlots;
        this.totalSlots = totalSlots;
        this.isFestivalDate = isFestivalDate;
        this.festivalName = festivalName;
        this.dayOfMonth = date.getDayOfMonth();
        this.monthValue = date.getMonthValue();
        this.year = date.getYear();
        this.dayOfWeek = date.getDayOfWeek().toString();
    }

    // =====================================
    // GETTERS AND SETTERS
    // =====================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(Long experienceId) {
        this.experienceId = experienceId;
    }

    public String getExperienceTitle() {
        return experienceTitle;
    }

    public void setExperienceTitle(String experienceTitle) {
        this.experienceTitle = experienceTitle;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
        if (date != null) {
            this.dayOfMonth = date.getDayOfMonth();
            this.monthValue = date.getMonthValue();
            this.year = date.getYear();
            this.dayOfWeek = date.getDayOfWeek().toString();
        }
    }

    public Integer getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(Integer availableSlots) {
        this.availableSlots = availableSlots;
    }

    public Integer getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(Integer totalSlots) {
        this.totalSlots = totalSlots;
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

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public Integer getMonthValue() {
        return monthValue;
    }

    public void setMonthValue(Integer monthValue) {
        this.monthValue = monthValue;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // =====================================
    // UTILITY METHODS
    // =====================================

    public boolean hasAvailableSlots() {
        return availableSlots != null && availableSlots > 0;
    }

    public double getOccupancyPercentage() {
        if (totalSlots == null || totalSlots == 0) {
            return 0;
        }
        return ((totalSlots - availableSlots) / (double) totalSlots) * 100;
    }
}
