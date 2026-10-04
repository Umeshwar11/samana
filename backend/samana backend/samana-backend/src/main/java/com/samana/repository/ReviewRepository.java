package com.samana.repository;

import com.samana.entity.Review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository
        extends JpaRepository<Review, Long> {

    List<Review> findByExperienceId(Long experienceId);

    List<Review> findByUserId(Long userId);

    Optional<Review> findByUserIdAndExperienceId(
            Long userId,
            Long experienceId
    );
}