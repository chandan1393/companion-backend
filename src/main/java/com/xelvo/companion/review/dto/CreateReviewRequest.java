package com.xelvo.companion.review.dto;

import jakarta.validation.constraints.*;

public record CreateReviewRequest(
        @NotNull Long bookingId,
        @NotNull @Min(1) @Max(5) Integer rating,
        @NotBlank @Size(max = 1000) String comment
) {}
