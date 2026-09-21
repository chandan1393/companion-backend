package com.xelvo.companion.booking.dto;

import com.xelvo.companion.booking.entity.BookingStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeBookingStatusRequest(@NotNull BookingStatus status) {}
