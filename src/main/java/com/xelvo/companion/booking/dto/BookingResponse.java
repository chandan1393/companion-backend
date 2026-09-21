package com.xelvo.companion.booking.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record BookingResponse(
        Long id,
        Long customerId,
        String customerName,
        Long companionId,
        String companionName,
        Long experienceId,
        String experienceTitle,
        LocalDate bookingDate,
        LocalTime startTime,
        LocalTime endTime,
        BigDecimal baseAmount,
        BigDecimal platformFee,
        BigDecimal totalAmount,
        String location,
        String status,
        String customerNote
) {}
