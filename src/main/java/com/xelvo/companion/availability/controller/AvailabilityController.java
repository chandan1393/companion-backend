package com.xelvo.companion.availability.controller;

import com.xelvo.companion.availability.dto.*;
import com.xelvo.companion.availability.service.AvailabilityService;
import com.xelvo.companion.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class AvailabilityController {

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping("/public/companions/{companionId}/availability")
    public ApiResponse<List<AvailabilityResponse>> publicAvailability(
            @PathVariable Long companionId,
            @RequestParam @DateTimeFormat(pattern = DATE_PATTERN) LocalDate from,
            @RequestParam @DateTimeFormat(pattern = DATE_PATTERN) LocalDate to) {
        return ApiResponse.success(availabilityService.publicAvailability(companionId, from, to));
    }

    @GetMapping("/companion/availability")
    @PreAuthorize("hasRole('COMPANION')")
    public ApiResponse<List<AvailabilityResponse>> mine(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {
        return ApiResponse.success(availabilityService.myAvailability(from, to));
    }

    @PostMapping("/companion/availability")
    @PreAuthorize("hasRole('COMPANION')")
    public ApiResponse<AvailabilityResponse> create(@Valid @RequestBody AvailabilityRequest request) {
        return ApiResponse.success("Availability added", availabilityService.create(request));
    }

    @DeleteMapping("/companion/availability/{id}")
    @PreAuthorize("hasRole('COMPANION')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        availabilityService.delete(id);
        return ApiResponse.success("Availability removed", null);
    }
}
