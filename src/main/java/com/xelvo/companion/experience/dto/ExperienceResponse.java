package com.xelvo.companion.experience.dto;

import java.math.BigDecimal;

public record ExperienceResponse(
        Long id,
        Long companionId,
        String companionName,
        String title,
        String description,
        BigDecimal price,
        Integer durationMinutes,
        String category,
        String imageUrl,
        boolean active
) {}
