package com.xelvo.companion.availability.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AvailabilityResponse(
        Long id,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String status
) {}
