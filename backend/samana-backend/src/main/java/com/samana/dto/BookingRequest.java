package com.samana.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class BookingRequest {

    @NotNull
    private Long experienceId;

    @NotNull
    @Min(1)
    private Integer numberOfPeople;

    @NotNull
    @Future
    private LocalDateTime bookingDate;


    // =========================
    // CONSTRUCTOR
    // =========================

    public BookingRequest() {
    }


    // =========================
    // GETTERS & SETTERS
    // =========================

    public Long getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(Long experienceId) {
        this.experienceId = experienceId;
    }


    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }


    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }
}