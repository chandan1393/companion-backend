package com.xelvo.companion.companion.controller;

import com.xelvo.companion.common.api.*;
import com.xelvo.companion.companion.dto.*;
import com.xelvo.companion.companion.entity.CompanionStatus;
import com.xelvo.companion.companion.service.CompanionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class CompanionController {

    private final CompanionService companionService;

    public CompanionController(CompanionService companionService) {
        this.companionService = companionService;
    }

    @GetMapping("/public/companions")
    public ApiResponse<PageResponse<CompanionResponse>> search(
            @RequestParam(required = false) String city,
            @PageableDefault(size = 12, sort = "rating", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.success(companionService.search(city, pageable));
    }

    @GetMapping("/public/companions/{id}")
    public ApiResponse<CompanionResponse> get(@PathVariable Long id) {
        return ApiResponse.success(companionService.getPublicProfile(id));
    }

    @GetMapping("/companion/profile")
    @PreAuthorize("hasRole('COMPANION')")
    public ApiResponse<CompanionResponse> myProfile() {
        return ApiResponse.success(companionService.myProfile());
    }

    @PutMapping("/companion/profile")
    @PreAuthorize("hasRole('COMPANION')")
    public ApiResponse<CompanionResponse> update(@Valid @RequestBody UpdateCompanionRequest request) {
        return ApiResponse.success("Profile updated", companionService.updateMyProfile(request));
    }

    @PutMapping("/companion/profile/interests")
    @PreAuthorize("hasRole('COMPANION')")
    public ApiResponse<CompanionResponse> updateInterests(
            @Valid @RequestBody UpdateInterestsRequest request) {
        return ApiResponse.success("Interests updated", companionService.updateInterests(request));
    }

    @PostMapping(value = "/companion/profile/photos/upload", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('COMPANION')")
    public ApiResponse<CompanionResponse> uploadPhoto(@RequestPart("file") MultipartFile file) {
        return ApiResponse.success("Photo uploaded", companionService.uploadPhoto(file));
    }

    @PostMapping("/companion/profile/photos")
    @PreAuthorize("hasRole('COMPANION')")
    public ApiResponse<CompanionResponse> addPhoto(@RequestParam String photoUrl) {
        return ApiResponse.success("Photo added", companionService.addPhoto(photoUrl));
    }

    @DeleteMapping("/companion/profile/photos")
    @PreAuthorize("hasRole('COMPANION')")
    public ApiResponse<CompanionResponse> removePhoto(@RequestParam String photoUrl) {
        return ApiResponse.success("Photo removed", companionService.removePhoto(photoUrl));
    }

    @PatchMapping("/admin/companions/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CompanionResponse> changeStatus(
            @PathVariable Long id,
            @RequestParam CompanionStatus status) {
        return ApiResponse.success("Companion status updated", companionService.changeStatus(id, status));
    }
}
