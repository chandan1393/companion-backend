package com.xelvo.companion.user.dto;

import jakarta.validation.constraints.*;

public record UpdateUserProfileRequest(
        @NotBlank @Size(max = 120) String fullName,
        @Size(max = 30) String phone,
        @Size(max = 100) String city
) {}
