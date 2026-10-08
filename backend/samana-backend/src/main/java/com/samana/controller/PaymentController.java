package com.samana.controller;

import com.samana.dto.PaymentRequest;
import com.samana.dto.PaymentResponse;
import com.samana.enums.PaymentStatus;
import com.samana.service.PaymentService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;


    // =========================================================
    // CREATE PAYMENT
    // =========================================================

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody PaymentRequest request) {

        PaymentResponse response =
                paymentService.createPayment(request);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }


    // =========================================================
    // GET MY PAYMENTS
    // =========================================================

    @GetMapping("/my")
    public ResponseEntity<List<PaymentResponse>> getMyPayments() {

        List<PaymentResponse> payments =
                paymentService.getMyPayments();

        return ResponseEntity.ok(payments);
    }


    // =========================================================
    // GET PAYMENT BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(
            @PathVariable Long id) {

        PaymentResponse response =
                paymentService.getPaymentById(id);

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // GET PAYMENT BY BOOKING ID
    // =========================================================

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentResponse> getPaymentByBooking(
            @PathVariable Long bookingId) {

        PaymentResponse response =
                paymentService.getPaymentByBooking(
                        bookingId
                );

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // ADMIN - GET ALL PAYMENTS
    // =========================================================

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {

        List<PaymentResponse> payments =
                paymentService.getAllPayments();

        return ResponseEntity.ok(payments);
    }


    // =========================================================
    // ADMIN - GET PAYMENT BY TRANSACTION ID
    // =========================================================

    @GetMapping("/admin/transaction/{transactionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> getPaymentByTransaction(
            @PathVariable String transactionId) {

        PaymentResponse response =
                paymentService.getPaymentByTransaction(
                        transactionId
                );

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // ADMIN - UPDATE PAYMENT STATUS
    // =========================================================

    @PutMapping("/admin/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam PaymentStatus status) {

        PaymentResponse response =
                paymentService.updatePaymentStatus(
                        id,
                        status
                );

        return ResponseEntity.ok(response);
    }
}