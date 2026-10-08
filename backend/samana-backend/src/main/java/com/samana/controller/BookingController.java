package com.samana.controller;

import com.samana.dto.BookingRequest;
import com.samana.dto.BookingResponse;
import com.samana.service.BookingService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin("*")
public class BookingController {

    @Autowired
    private BookingService bookingService;


    // =====================================================
    // CREATE BOOKING
    // =====================================================

    @PostMapping
    public BookingResponse createBooking(
            @Valid @RequestBody BookingRequest request,
            Authentication authentication) {

        String email =
                authentication.getName();


        return bookingService.createBooking(
                request,
                email
        );
    }


    // =====================================================
    // GET MY BOOKINGS
    // =====================================================

    @GetMapping("/my")
    public List<BookingResponse> getMyBookings(
            Authentication authentication) {

        String email =
                authentication.getName();


        return bookingService.getMyBookings(
                email
        );
    }


    // =====================================================
    // CANCEL MY BOOKING
    // =====================================================

    @PutMapping("/{id}/cancel")
    public BookingResponse cancelBooking(
            @PathVariable Long id,
            Authentication authentication) {

        String email =
                authentication.getName();


        return bookingService.cancelBooking(
                id,
                email
        );
    }


    // =====================================================
    // ADMIN - GET ALL BOOKINGS
    // =====================================================

    @GetMapping("/admin/all")
    public List<BookingResponse> getAllBookings() {

        return bookingService.getAllBookings();
    }


    // =====================================================
    // ADMIN - GET BOOKING BY ID
    // =====================================================

    @GetMapping("/admin/{id}")
    public BookingResponse getBookingById(
            @PathVariable Long id) {

        return bookingService.getBookingById(id);
    }


    // =====================================================
    // ADMIN - CONFIRM BOOKING
    // =====================================================

    @PutMapping("/admin/{id}/confirm")
    public BookingResponse confirmBooking(
            @PathVariable Long id) {

        return bookingService.confirmBooking(id);
    }


    // =====================================================
    // ADMIN - CANCEL BOOKING
    // =====================================================

    @PutMapping("/admin/{id}/cancel")
    public BookingResponse adminCancelBooking(
            @PathVariable Long id) {

        return bookingService.adminCancelBooking(id);
    }


    // =====================================================
    // ADMIN - DELETE BOOKING
    // =====================================================

    @DeleteMapping("/admin/{id}")
    public String deleteBooking(
            @PathVariable Long id) {

        return bookingService.deleteBooking(id);
    }
}