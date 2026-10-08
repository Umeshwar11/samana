package com.samana.entity;

import com.samana.enums.BookingStatus;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =========================
    // USER
    // =========================

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // =========================
    // EXPERIENCE
    // =========================

    @ManyToOne
    @JoinColumn(name = "experience_id", nullable = false)
    private Experience experience;


    // =========================
    // BOOKING DETAILS
    // =========================

    @Column(nullable = false)
    private Integer numberOfPeople;


    @Column(nullable = false)
    private LocalDateTime bookingDate;


    @Column(nullable = false)
    private Double totalPrice;


    // =========================
    // STATUS
    // =========================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;


    // =========================
    // CREATED DATE
    // =========================

    @Column(nullable = false)
    private LocalDateTime createdAt;


    // =========================
    // CONSTRUCTOR
    // =========================

    public Booking() {
    }


    // =========================
    // GETTERS AND SETTERS
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
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


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}