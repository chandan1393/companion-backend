package com.xelvo.companion.review.dto;

import java.time.Instant;

public record ReviewResponse(
        Long id,
        Long bookingId,
        Long companionId,
        String customerName,
        Integer rating,
        String comment,
        Instant createdAt
) {}
