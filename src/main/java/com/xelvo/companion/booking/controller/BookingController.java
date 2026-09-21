package com.xelvo.companion.booking.controller;

import com.xelvo.companion.booking.dto.*;
import com.xelvo.companion.booking.service.BookingService;
import com.xelvo.companion.common.api.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<BookingResponse> create(@Valid @RequestBody CreateBookingRequest request) {
        return ApiResponse.success("Booking created", bookingService.create(request));
    }

    @GetMapping("/mine")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<PageResponse<BookingResponse>> mine(
            @PageableDefault(size = 20, sort = "bookingDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.success(bookingService.customerBookings(pageable));
    }

    @GetMapping("/companion")
    @PreAuthorize("hasRole('COMPANION')")
    public ApiResponse<PageResponse<BookingResponse>> companionBookings(
            @PageableDefault(size = 20, sort = "bookingDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.success(bookingService.companionBookings(pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<BookingResponse> get(@PathVariable Long id) {
        return ApiResponse.success(bookingService.getForCurrentUser(id));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<BookingResponse> changeStatus(
            @PathVariable Long id,
            @Valid @RequestBody ChangeBookingStatusRequest request) {
        return ApiResponse.success("Booking status updated", bookingService.changeStatus(id, request.status()));
    }
}
