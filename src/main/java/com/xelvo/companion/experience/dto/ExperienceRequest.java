package com.xelvo.companion.experience.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ExperienceRequest(
        @NotBlank @Size(max = 150) String title,
        @NotBlank @Size(max = 1000) String description,
        @NotNull @DecimalMin("0.00") BigDecimal price,
        @NotNull @Min(15) Integer durationMinutes,
        @Size(max = 50) String category
) {}
