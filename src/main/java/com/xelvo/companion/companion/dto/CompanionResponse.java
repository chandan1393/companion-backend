package com.xelvo.companion.companion.dto;

import java.math.BigDecimal;
import java.util.List;

public record CompanionResponse(
        Long id,
        Long userId,
        String displayName,
        Integer age,
        String city,
        String gender,
        String bio,
        String tagline,
        String coverPhotoUrl,
        String status,
        BigDecimal hourlyRate,
        BigDecimal rating,
        Integer reviewCount,
        Integer completedBookings,
        Integer profileCompletion,
        List<String> interests,
        List<String> photoUrls
) {}
