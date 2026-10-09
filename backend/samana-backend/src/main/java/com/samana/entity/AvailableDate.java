package com.samana.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * AvailableDate Entity
 * Represents available dates for booking an experience
 * Tracks date and available slots
 */
@Entity
@Table(name = "available_dates", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"experience_id", "date"})
})
public class AvailableDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experience_id", nullable = false)
    private Experience experience;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "available_slots", nullable = false)
    private Integer availableSlots;

    @Column(name = "total_slots", nullable = false)
    private Integer totalSlots;

    @Column(name = "is_festival_date", nullable = false)
    private Boolean isFestivalDate = false;

    @Column(name = "festival_name")
    private String festivalName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // =====================================
    // CONSTRUCTORS
    // =====================================

    public AvailableDate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public AvailableDate(Experience experience, LocalDate date, Integer totalSlots) {
        this.experience = experience;
        this.date = date;
        this.totalSlots = totalSlots;
        this.availableSlots = totalSlots;
        this.isFestivalDate = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public AvailableDate(Experience experience, LocalDate date, Integer totalSlots,
                        String festivalName, Boolean isFestivalDate) {
        this.experience = experience;
        this.date = date;
        this.totalSlots = totalSlots;
        this.availableSlots = totalSlots;
        this.festivalName = festivalName;
        this.isFestivalDate = isFestivalDate;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // =====================================
    // JPA LIFECYCLE CALLBACKS
    // =====================================

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
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

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
        return availableSlots > 0;
    }

    public void bookSlot() {
        if (availableSlots > 0) {
            this.availableSlots--;
        }
    }

    public void cancelSlot() {
        if (availableSlots < totalSlots) {
            this.availableSlots++;
        }
    }

    @Override
    public String toString() {
        return "AvailableDate{" +
                "id=" + id +
                ", date=" + date +
                ", availableSlots=" + availableSlots +
                ", totalSlots=" + totalSlots +
                ", isFestivalDate=" + isFestivalDate +
                ", festivalName='" + festivalName + '\'' +
                '}';
    }
}
