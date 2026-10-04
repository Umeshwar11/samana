package com.samana.service;

import com.samana.dto.ReviewRequest;
import com.samana.dto.ReviewResponse;
import com.samana.entity.Review;
import com.samana.entity.User;
import com.samana.repository.ReviewRepository;
import com.samana.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {


@Autowired
private ReviewRepository reviewRepository;

@Autowired
private UserRepository userRepository;


// =========================================================
// CREATE REVIEW
// =========================================================

public ReviewResponse createReview(ReviewRequest request) {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    Long userId = getUserId(authentication);

    // User can review an experience only once
    if (reviewRepository
            .findByUserIdAndExperienceId(
                    userId,
                    request.getExperienceId()
            )
            .isPresent()) {

        throw new RuntimeException(
                "You have already reviewed this experience"
        );
    }

    Review review = new Review();

    review.setUserId(userId);
    review.setExperienceId(request.getExperienceId());
    review.setRating(request.getRating());
    review.setComment(request.getComment());
    review.setCreatedAt(LocalDateTime.now());

    Review savedReview =
            reviewRepository.save(review);

    return convertToResponse(savedReview);
}


// =========================================================
// GET REVIEW BY ID
// =========================================================

public ReviewResponse getReviewById(Long id) {

    Review review =
            reviewRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Review not found"
                            )
                    );

    return convertToResponse(review);
}


// =========================================================
// GET REVIEWS FOR EXPERIENCE
// =========================================================

public List<ReviewResponse> getReviewsByExperience(
        Long experienceId) {

    return reviewRepository
            .findByExperienceId(experienceId)
            .stream()
            .map(this::convertToResponse)
            .toList();
}


// =========================================================
// GET MY REVIEWS
// =========================================================

public List<ReviewResponse> getMyReviews() {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    Long userId = getUserId(authentication);

    return reviewRepository
            .findByUserId(userId)
            .stream()
            .map(this::convertToResponse)
            .toList();
}


// =========================================================
// UPDATE REVIEW
// =========================================================

public ReviewResponse updateReview(
        Long id,
        ReviewRequest request) {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    Long userId = getUserId(authentication);

    Review review =
            reviewRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Review not found"
                            )
                    );

    // Only owner can update
    if (!review.getUserId().equals(userId)) {

        throw new RuntimeException(
                "You are not allowed to update this review"
        );
    }

    review.setRating(request.getRating());
    review.setComment(request.getComment());

    Review updated =
            reviewRepository.save(review);

    return convertToResponse(updated);
}


// =========================================================
// DELETE REVIEW
// =========================================================

public String deleteReview(Long id) {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    Long userId = getUserId(authentication);

    Review review =
            reviewRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Review not found"
                            )
                    );

    if (!review.getUserId().equals(userId)) {

        throw new RuntimeException(
                "You are not allowed to delete this review"
        );
    }

    reviewRepository.delete(review);

    return "Review deleted successfully";
}


// =========================================================
// GET USER ID FROM JWT USERNAME/EMAIL
// =========================================================

private Long getUserId(Authentication authentication) {

    if (authentication == null ||
            !authentication.isAuthenticated()) {

        throw new RuntimeException(
                "User is not authenticated"
        );
    }

    String email = authentication.getName();

    User user =
            userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "User not found: " + email
                            )
                    );

    return user.getId();
}


// =========================================================
// CONVERT ENTITY TO RESPONSE
// =========================================================

private ReviewResponse convertToResponse(
        Review review) {

    ReviewResponse response =
            new ReviewResponse();

    response.setId(review.getId());

    response.setUserId(review.getUserId());

    response.setExperienceId(
            review.getExperienceId()
    );

    response.setRating(
            review.getRating()
    );

    response.setComment(
            review.getComment()
    );

    response.setCreatedAt(
            review.getCreatedAt()
    );

    return response;
}


}