package com.xelvo.companion.review.service;

import com.xelvo.companion.common.api.PageResponse;
import com.xelvo.companion.booking.entity.Booking;
import com.xelvo.companion.booking.entity.BookingStatus;
import com.xelvo.companion.booking.repository.BookingRepository;
import com.xelvo.companion.common.exception.BusinessException;
import com.xelvo.companion.common.exception.ResourceNotFoundException;
import com.xelvo.companion.companion.entity.CompanionProfile;
import com.xelvo.companion.review.dto.*;
import com.xelvo.companion.review.entity.Review;
import com.xelvo.companion.review.repository.ReviewRepository;
import com.xelvo.companion.security.CurrentUserService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final CurrentUserService currentUserService;

    public ReviewService(ReviewRepository reviewRepository,
                         BookingRepository bookingRepository,
                         CurrentUserService currentUserService) {
        this.reviewRepository = reviewRepository;
        this.bookingRepository = bookingRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public ReviewResponse create(CreateReviewRequest request) {
        Booking booking = bookingRepository.findById(request.bookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getCustomer().getId().equals(currentUserService.get().getId())) {
            throw new BusinessException("Only booking customer can submit a review");
        }

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new BusinessException("Review can be submitted only after completion");
        }

        if (reviewRepository.existsByBookingId(booking.getId())) {
            throw new BusinessException("A review already exists for this booking");
        }

        Review review = reviewRepository.save(new Review(
                booking,
                booking.getCustomer(),
                booking.getCompanion(),
                request.rating(),
                request.comment().trim()
        ));

        updateCompanionRating(booking.getCompanion(), request.rating());
        return toResponse(review);
    }

    @Transactional(readOnly = true)
    public PageResponse<ReviewResponse> byCompanion(Long companionId, Pageable pageable) {
        return PageResponse.from(
                reviewRepository.findByCompanionIdOrderByCreatedAtDesc(companionId, pageable)
                        .map(this::toResponse)
        );
    }

    private void updateCompanionRating(CompanionProfile companion, int newRating) {
        int oldCount = companion.getReviewCount();
        BigDecimal oldRating = companion.getRating();

        BigDecimal total = oldRating.multiply(BigDecimal.valueOf(oldCount))
                .add(BigDecimal.valueOf(newRating));

        BigDecimal updated = total
                .divide(BigDecimal.valueOf(oldCount + 1), 2, RoundingMode.HALF_UP);

        companion.addReview(oldCount + 1, updated);
    }

    private ReviewResponse toResponse(Review r) {
        return new ReviewResponse(
                r.getId(),
                r.getBooking().getId(),
                r.getCompanion().getId(),
                r.getCustomer().getFullName(),
                r.getRating(),
                r.getComment(),
                r.getCreatedAt()
        );
    }
}
