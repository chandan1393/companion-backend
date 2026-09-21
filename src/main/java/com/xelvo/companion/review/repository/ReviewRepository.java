package com.xelvo.companion.review.repository;

import com.xelvo.companion.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByCompanionIdOrderByCreatedAtDesc(Long companionId, Pageable pageable);
    boolean existsByBookingId(Long bookingId);
}
