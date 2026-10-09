package com.samana.dto;

import jakarta.validation.constraints.*;

/**
 * DTO for creating/updating an experience package
 */
public class ExperiencePackageRequest {

    @NotBlank(message = "Package name is required")
    @Size(min = 3, max = 100, message = "Package name must be between 3 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be greater than 0")
    @Max(value = 365, message = "Duration cannot exceed 365 days")
    private Integer duration;

    @NotNull(message = "Max guests is required")
    @Positive(message = "Max guests must be greater than 0")
    private Integer maxGuests;

    @Size(max = 2000, message = "Included items description cannot exceed 2000 characters")
    private String included;

    @Size(max = 2000, message = "Not included items description cannot exceed 2000 characters")
    private String notIncluded;

    @Size(max = 2000, message = "Detailed description cannot exceed 2000 characters")
    private String description_long;

    private Boolean available = true;

    private Boolean isFeatured = false;

    // ==========================================
    // GETTERS AND SETTERS
    // ==========================================

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
}
