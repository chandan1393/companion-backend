package com.xelvo.companion.experience.controller;

import com.xelvo.companion.common.api.*;
import com.xelvo.companion.experience.dto.*;
import com.xelvo.companion.experience.service.ExperienceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExperienceController {

    private final ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @GetMapping("/public/experiences")
    public ApiResponse<PageResponse<ExperienceResponse>> list(
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.success(experienceService.publicList(pageable));
    }

    @GetMapping("/public/companions/{companionId}/experiences")
    public ApiResponse<List<ExperienceResponse>> byCompanion(@PathVariable Long companionId) {
        return ApiResponse.success(experienceService.publicByCompanion(companionId));
    }

    @GetMapping("/companion/experiences")
    @PreAuthorize("hasRole('COMPANION')")
    public ApiResponse<List<ExperienceResponse>> mine() {
        return ApiResponse.success(experienceService.myExperiences());
    }

    @PostMapping("/companion/experiences")
    @PreAuthorize("hasRole('COMPANION')")
    public ApiResponse<ExperienceResponse> create(@Valid @RequestBody ExperienceRequest request) {
        return ApiResponse.success("Experience created", experienceService.create(request));
    }

    @PutMapping("/companion/experiences/{id}")
    @PreAuthorize("hasRole('COMPANION')")
    public ApiResponse<ExperienceResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ExperienceRequest request) {
        return ApiResponse.success("Experience updated", experienceService.update(id, request));
    }

    @DeleteMapping("/companion/experiences/{id}")
    @PreAuthorize("hasRole('COMPANION')")
    public ApiResponse<Void> deactivate(@PathVariable Long id) {
        experienceService.deactivate(id);
        return ApiResponse.success("Experience deactivated", null);
    }
}
