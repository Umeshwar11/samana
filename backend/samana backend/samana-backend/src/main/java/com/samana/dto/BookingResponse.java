package com.samana.dto;

import com.samana.enums.BookingStatus;

import java.time.LocalDateTime;

public class BookingResponse {

    private Long id;

    private Long userId;

    private Long experienceId;

    private String experienceTitle;

    private Integer numberOfPeople;

    private LocalDateTime bookingDate;

    private Double totalPrice;

    private BookingStatus status;


    // =========================
    // CONSTRUCTOR
    // =========================

    public BookingResponse() {
    }


    public BookingResponse(
            Long id,
            Long userId,
            Long experienceId,
            String experienceTitle,
            Integer numberOfPeople,
            LocalDateTime bookingDate,
            Double totalPrice,
            BookingStatus status) {

        this.id = id;
        this.userId = userId;
        this.experienceId = experienceId;
        this.experienceTitle = experienceTitle;
        this.numberOfPeople = numberOfPeople;
        this.bookingDate = bookingDate;
        this.totalPrice = totalPrice;
        this.status = status;
    }


    // =========================
    // GETTERS & SETTERS
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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


    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }


    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}