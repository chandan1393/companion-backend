package com.xelvo.companion.booking.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

public record CreateBookingRequest(
        @NotNull Long companionId,
        @NotNull Long experienceId,
        @NotNull @FutureOrPresent LocalDate bookingDate,
        @NotNull LocalTime startTime,
        @NotBlank @Size(max = 300) String location,
        @Size(max = 1000) String customerNote
) {}
