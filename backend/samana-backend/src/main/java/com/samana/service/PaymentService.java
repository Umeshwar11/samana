package com.samana.service;

import com.samana.dto.PaymentRequest;
import com.samana.dto.PaymentResponse;
import com.samana.entity.Booking;
import com.samana.entity.Payment;
import com.samana.entity.User;
import com.samana.enums.BookingStatus;
import com.samana.enums.PaymentStatus;
import com.samana.repository.BookingRepository;
import com.samana.repository.PaymentRepository;
import com.samana.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;


    // =========================================================
    // CREATE PAYMENT
    // =========================================================

    public PaymentResponse createPayment(PaymentRequest request) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User is not authenticated"
            );
        }


        // =====================================================
        // GET LOGGED-IN USER
        // =====================================================

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );


        // =====================================================
        // FIND BOOKING
        // =====================================================

        Booking booking = bookingRepository
                .findById(request.getBookingId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found"
                        )
                );


        // =====================================================
        // CHECK BOOKING BELONGS TO USER
        // =====================================================

        if (!booking.getUser()
                .getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "You are not allowed to pay for this booking"
            );
        }


        // =====================================================
        // CHECK EXISTING PAYMENT
        // =====================================================

        if (paymentRepository
                .findByBookingId(request.getBookingId())
                .isPresent()) {

            throw new RuntimeException(
                    "Payment already exists for this booking"
            );
        }


        // =====================================================
        // GET AMOUNT FROM BOOKING
        // =====================================================

        Double amount = booking.getTotalPrice();


        // =====================================================
        // CREATE PAYMENT
        // =====================================================

        Payment payment = new Payment();

        payment.setBookingId(
                booking.getId()
        );

        payment.setUserId(
                user.getId()
        );

        payment.setAmount(
                amount
        );

        payment.setPaymentMethod(
                request.getPaymentMethod()
        );

        payment.setStatus(
                PaymentStatus.SUCCESS
        );

        payment.setTransactionId(
                "TXN_" + UUID.randomUUID()
        );

        payment.setPaymentDate(
                LocalDateTime.now()
        );


        // =====================================================
        // SAVE PAYMENT
        // =====================================================

        Payment savedPayment =
                paymentRepository.save(payment);


        // =====================================================
        // UPDATE BOOKING STATUS
        // =====================================================

        booking.setStatus(
                BookingStatus.CONFIRMED
        );

        bookingRepository.save(booking);


        // =====================================================
        // RETURN RESPONSE
        // =====================================================

        return convertToResponse(savedPayment);
    }


    // =========================================================
    // GET PAYMENT BY ID
    // =========================================================

    public PaymentResponse getPaymentById(Long id) {

        Payment payment =
                paymentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found"
                                )
                        );

        return convertToResponse(payment);
    }


    // =========================================================
    // GET MY PAYMENTS
    // =========================================================

    public List<PaymentResponse> getMyPayments() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User is not authenticated"
            );
        }


        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );


        return paymentRepository
                .findByUserId(user.getId())
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    // =========================================================
    // GET PAYMENT BY BOOKING
    // =========================================================

    public PaymentResponse getPaymentByBooking(
            Long bookingId) {

        Payment payment =
                paymentRepository
                        .findByBookingId(bookingId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found for booking"
                                )
                        );

        return convertToResponse(payment);
    }


    // =========================================================
    // ADMIN - GET ALL PAYMENTS
    // =========================================================

    public List<PaymentResponse> getAllPayments() {

        return paymentRepository
                .findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    // =========================================================
    // ADMIN - GET PAYMENT BY TRANSACTION
    // =========================================================

    public PaymentResponse getPaymentByTransaction(
            String transactionId) {

        Payment payment =
                paymentRepository
                        .findByTransactionId(transactionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found"
                                )
                        );

        return convertToResponse(payment);
    }


    // =========================================================
    // ADMIN - UPDATE PAYMENT STATUS
    // =========================================================

    public PaymentResponse updatePaymentStatus(
            Long id,
            PaymentStatus status) {

        Payment payment =
                paymentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found"
                                )
                        );


        payment.setStatus(status);


        Payment updated =
                paymentRepository.save(payment);


        return convertToResponse(updated);
    }


    // =========================================================
    // CONVERT ENTITY → RESPONSE
    // =========================================================

    private PaymentResponse convertToResponse(
            Payment payment) {

        PaymentResponse response =
                new PaymentResponse();

        response.setId(
                payment.getId()
        );

        response.setBookingId(
                payment.getBookingId()
        );

        response.setUserId(
                payment.getUserId()
        );

        response.setAmount(
                payment.getAmount()
        );

        response.setPaymentMethod(
                payment.getPaymentMethod()
        );

        response.setStatus(
                payment.getStatus()
        );

        response.setTransactionId(
                payment.getTransactionId()
        );

        response.setPaymentDate(
                payment.getPaymentDate()
        );

        return response;
    }
}