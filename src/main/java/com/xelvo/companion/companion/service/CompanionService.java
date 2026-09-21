package com.xelvo.companion.companion.service;

import com.xelvo.companion.common.api.PageResponse;
import com.xelvo.companion.common.exception.BusinessException;
import com.xelvo.companion.common.storage.FileStorageService;
import org.springframework.web.multipart.MultipartFile;
import com.xelvo.companion.common.exception.ResourceNotFoundException;
import com.xelvo.companion.companion.dto.*;
import com.xelvo.companion.companion.entity.*;
import com.xelvo.companion.companion.repository.CompanionRepository;
import com.xelvo.companion.security.CurrentUserService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanionService {

    private final CompanionRepository companionRepository;
    private final CurrentUserService currentUserService;
    private final FileStorageService fileStorageService;

    public CompanionService(CompanionRepository companionRepository,
                            CurrentUserService currentUserService,
                            FileStorageService fileStorageService) {
        this.companionRepository = companionRepository;
        this.currentUserService = currentUserService;
        this.fileStorageService = fileStorageService;
    }

    @Transactional(readOnly = true)
    public PageResponse<CompanionResponse> search(String city, Pageable pageable) {
        Page<CompanionProfile> page = city == null || city.isBlank()
                ? companionRepository.findByStatus(CompanionStatus.VERIFIED, pageable)
                : companionRepository.findByStatusAndCityIgnoreCase(CompanionStatus.VERIFIED, city.trim(), pageable);

        return PageResponse.from(page.map(this::toResponse));
    }

    @Transactional(readOnly = true)
    public CompanionResponse getPublicProfile(Long id) {
        CompanionProfile profile = getById(id);
        return toResponse(profile);
    }

    @Transactional(readOnly = true)
    public CompanionResponse myProfile() {
        CompanionProfile profile = companionRepository.findByUserId(currentUserService.get().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Companion profile not found"));
        return toResponse(profile);
    }

    @Transactional
    public CompanionResponse updateMyProfile(UpdateCompanionRequest request) {
        CompanionProfile profile = companionRepository.findByUserId(currentUserService.get().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Companion profile not found"));

        profile.updateProfile(
                request.displayName().trim(),
                request.age(),
                request.city().trim(),
                request.gender().trim(),
                request.bio(),
                request.tagline(),
                request.hourlyRate()
        );

        return toResponse(companionRepository.save(profile));
    }

    @Transactional
    public CompanionResponse updateInterests(UpdateInterestsRequest request) {
        CompanionProfile profile = getByIdForUser(currentUserService.get().getId());
        profile.updateInterests(request.interests());
        return toResponse(companionRepository.save(profile));
    }

    @Transactional
    public CompanionResponse uploadPhoto(MultipartFile file) {
        CompanionProfile profile = getByIdForUser(currentUserService.get().getId());

        if (profile.getPhotoUrls().size() >= 6) {
            throw new BusinessException("Maximum 6 profile photos allowed");
        }

        String photoUrl = fileStorageService.storeCompanionPhoto(profile.getId(), file);
        profile.addPhoto(photoUrl);
        return toResponse(companionRepository.save(profile));
    }

    @Transactional
    public CompanionResponse addPhoto(String photoUrl) {
        CompanionProfile profile = getByIdForUser(currentUserService.get().getId());
        profile.addPhoto(photoUrl);
        return toResponse(companionRepository.save(profile));
    }

    @Transactional
    public CompanionResponse removePhoto(String photoUrl) {
        CompanionProfile profile = getByIdForUser(currentUserService.get().getId());
        profile.removePhoto(photoUrl);
        return toResponse(companionRepository.save(profile));
    }

    @Transactional
    public CompanionResponse changeStatus(Long id, CompanionStatus status) {
        CompanionProfile profile = getById(id);
        profile.changeStatus(status);
        return toResponse(companionRepository.save(profile));
    }

    public CompanionProfile getByIdForUser(Long userId) {
        return companionRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Companion profile not found"));
    }

    public CompanionProfile getById(Long id) {
        return companionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Companion not found: " + id));
    }

    private CompanionResponse toResponse(CompanionProfile p) {
        return new CompanionResponse(
                p.getId(), p.getUser().getId(), p.getDisplayName(), p.getAge(), p.getCity(), p.getGender(),
                p.getBio(), p.getTagline(), p.getCoverPhotoUrl(), p.getStatus().name(),
                p.getHourlyRate(), p.getRating(), p.getReviewCount(),
                p.getCompletedBookings(), p.getProfileCompletion(), p.getInterests(), p.getPhotoUrls()
        );
    }
}
