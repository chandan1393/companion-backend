package com.xelvo.companion.companion.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record UpdateCompanionRequest(
        @NotBlank @Size(max = 100) String displayName,
        @NotNull @Min(18) @Max(100) Integer age,
        @NotBlank @Size(max = 100) String city,
        @NotBlank @Size(max = 20) String gender,
        @Size(max = 1200) String bio,
        @Size(max = 500) String tagline,
        @NotNull @DecimalMin("0.00") BigDecimal hourlyRate
) {}
