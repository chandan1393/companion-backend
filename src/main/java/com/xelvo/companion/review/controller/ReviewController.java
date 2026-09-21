package com.xelvo.companion.review.controller;

import com.xelvo.companion.common.api.*;
import com.xelvo.companion.review.dto.*;
import com.xelvo.companion.review.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/reviews")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<ReviewResponse> create(@Valid @RequestBody CreateReviewRequest request) {
        return ApiResponse.success("Review submitted", reviewService.create(request));
    }

    @GetMapping("/public/companions/{companionId}/reviews")
    public ApiResponse<PageResponse<ReviewResponse>> byCompanion(
            @PathVariable Long companionId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.success(reviewService.byCompanion(companionId, pageable));
    }
}
