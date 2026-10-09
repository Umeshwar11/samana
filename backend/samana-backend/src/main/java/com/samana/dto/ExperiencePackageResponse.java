package com.samana.dto;

import java.time.LocalDateTime;

/**
 * DTO for returning experience package data
 */
public class ExperiencePackageResponse {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private Integer duration;

    private Integer maxGuests;

    private String included;

    private String notIncluded;

    private String description_long;

    private Boolean available;

    private Boolean isFeatured;

    private Long experienceId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public ExperiencePackageResponse() {
    }

    public ExperiencePackageResponse(Long id, String name, Double price, Integer duration, Integer maxGuests) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.maxGuests = maxGuests;
    }

    public ExperiencePackageResponse(
            Long id,
            String name,
            String description,
            Double price,
            Integer duration,
            Integer maxGuests,
            String included,
            String notIncluded,
            String description_long,
            Boolean available,
            Boolean isFeatured,
            Long experienceId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.maxGuests = maxGuests;
        this.included = included;
        this.notIncluded = notIncluded;
        this.description_long = description_long;
        this.available = available;
        this.isFeatured = isFeatured;
        this.experienceId = experienceId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ==========================================
    // GETTERS AND SETTERS
    // ==========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(Integer maxGuests) {
        this.maxGuests = maxGuests;
    }

    public String getIncluded() {
        return included;
    }

    public void setIncluded(String included) {
        this.included = included;
    }

    public String getNotIncluded() {
        return notIncluded;
    }

    public void setNotIncluded(String notIncluded) {
        this.notIncluded = notIncluded;
    }

    public String getDescription_long() {
        return description_long;
    }

    public void setDescription_long(String description_long) {
        this.description_long = description_long;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Long getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(Long experienceId) {
        this.experienceId = experienceId;
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
}
