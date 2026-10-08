 package com.samana.controller;

import com.samana.dto.ReviewRequest;
import com.samana.dto.ReviewResponse;
import com.samana.service.ReviewService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin("*")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;


    // =========================================================
    // CREATE REVIEW
    // =========================================================

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @Valid @RequestBody ReviewRequest request) {

        ReviewResponse response =
                reviewService.createReview(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // GET REVIEW BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReviewById(
            @PathVariable Long id) {

        ReviewResponse response =
                reviewService.getReviewById(id);

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // GET REVIEWS BY EXPERIENCE
    // =========================================================

    @GetMapping("/experience/{experienceId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByExperience(
            @PathVariable Long experienceId) {

        List<ReviewResponse> reviews =
                reviewService.getReviewsByExperience(
                        experienceId
                );

        return ResponseEntity.ok(reviews);
    }


    // =========================================================
    // GET MY REVIEWS
    // =========================================================

    @GetMapping("/my")
    public ResponseEntity<List<ReviewResponse>> getMyReviews() {

        List<ReviewResponse> reviews =
                reviewService.getMyReviews();

        return ResponseEntity.ok(reviews);
    }


    // =========================================================
    // UPDATE REVIEW
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewRequest request) {

        ReviewResponse response =
                reviewService.updateReview(
                        id,
                        request
                );

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // DELETE REVIEW
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(
            @PathVariable Long id) {

        String message =
                reviewService.deleteReview(id);

        return ResponseEntity.ok(message);
    }
}