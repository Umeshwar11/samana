package com.samana.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * ExperiencePackage Entity
 * Represents a booking package for an experience (e.g., 1-day, 3-day, 7-day)
 * Each experience can have multiple packages with different prices and durations
 */
@Entity
@Table(name = "experience_packages")
public class ExperiencePackage {

    // ==========================================
    // PRIMARY KEY
    // ==========================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==========================================
    // PACKAGE BASIC INFO
    // ==========================================

    @Column(nullable = false)
    private String name;  // e.g., "1-Day Experience", "3-Day Package", "7-Day Full"

    @Column(length = 500)
    private String description;

    // ==========================================
    // PRICING & DURATION
    // ==========================================

    @Column(nullable = false)
    private Double price;  // Price per person

    @Column(nullable = false)
    private Integer duration;  // Duration in days

    @Column(nullable = false)
    private Integer maxGuests;  // Maximum number of guests for this package

    // ==========================================
    // PACKAGE DETAILS
    // ==========================================

    @Column(columnDefinition = "TEXT")
    private String included;  // What's included in this package

    @Column(columnDefinition = "TEXT")
    private String notIncluded;  // What's not included

    @Column(columnDefinition = "TEXT")
    private String description_long;  // Detailed description

    // ==========================================
    // AVAILABILITY
    // ==========================================

    @Column(nullable = false)
    private Boolean available = true;

    @Column(nullable = false)
    private Boolean isFeatured = false;  // Is this the featured/default package?

    // ==========================================
    // RELATIONSHIP: Experience
    // ==========================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experience_id", nullable = false)
    private Experience experience;

    // ==========================================
    // TIMESTAMPS
    // ==========================================

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ==========================================
    // LIFECYCLE HOOKS
    // ==========================================

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public ExperiencePackage() {
    }

    public ExperiencePackage(String name, Double price, Integer duration, Integer maxGuests, Experience experience) {
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.maxGuests = maxGuests;
        this.experience = experience;
        this.available = true;
        this.isFeatured = false;
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

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
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

    // ==========================================
    // UTILITY METHODS
    // ==========================================

    @Override
    public String toString() {
        return "ExperiencePackage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", maxGuests=" + maxGuests +
                ", available=" + available +
                ", isFeatured=" + isFeatured +
                '}';
    }
}
