package com.xelvo.companion.availability.service;

import com.xelvo.companion.availability.dto.*;
import com.xelvo.companion.availability.entity.*;
import com.xelvo.companion.availability.repository.AvailabilityRepository;
import com.xelvo.companion.common.exception.BusinessException;
import com.xelvo.companion.companion.entity.CompanionProfile;
import com.xelvo.companion.companion.service.CompanionService;
import com.xelvo.companion.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final CompanionService companionService;
    private final CurrentUserService currentUserService;

    public AvailabilityService(AvailabilityRepository availabilityRepository,
                                CompanionService companionService,
                                CurrentUserService currentUserService) {
        this.availabilityRepository = availabilityRepository;
        this.companionService = companionService;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public List<AvailabilityResponse> myAvailability(LocalDate from, LocalDate to) {
        CompanionProfile companion = companionService.getByIdForUser(currentUserService.get().getId());

        return availabilityRepository
                .findByCompanionIdAndAvailableDateBetweenOrderByAvailableDateAscStartTimeAsc(
                        companion.getId(), from, to)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AvailabilityResponse> publicAvailability(Long companionId, LocalDate from, LocalDate to) {
        companionService.getById(companionId);

        return availabilityRepository
                .findByCompanionIdAndAvailableDateBetweenOrderByAvailableDateAscStartTimeAsc(
                        companionId, from, to)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public AvailabilityResponse create(AvailabilityRequest request) {
        if (!request.endTime().isAfter(request.startTime())) {
            throw new BusinessException("End time must be after start time");
        }

        if (request.date().isBefore(LocalDate.now())) {
            throw new BusinessException("Availability date cannot be in the past");
        }

        CompanionProfile companion = companionService.getByIdForUser(currentUserService.get().getId());

        boolean overlap = availabilityRepository
                .existsByCompanionIdAndAvailableDateAndStartTimeLessThanAndEndTimeGreaterThanAndStatus(
                        companion.getId(),
                        request.date(),
                        request.endTime(),
                        request.startTime(),
                        AvailabilityStatus.AVAILABLE
                );

        if (overlap) {
            throw new BusinessException("Availability slot overlaps an existing slot");
        }

        AvailabilitySlot slot = new AvailabilitySlot(
                companion, request.date(), request.startTime(), request.endTime()
        );

        return toResponse(availabilityRepository.save(slot));
    }

    @Transactional
    public void delete(Long id) {
        AvailabilitySlot slot = availabilityRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Availability slot not found"));

        if (!slot.getCompanion().getUser().getId().equals(currentUserService.get().getId())) {
            throw new BusinessException("You cannot modify this availability slot");
        }

        availabilityRepository.delete(slot);
    }

    private AvailabilityResponse toResponse(AvailabilitySlot slot) {
        return new AvailabilityResponse(
                slot.getId(),
                slot.getAvailableDate(),
                slot.getStartTime(),
                slot.getEndTime(),
                slot.getStatus().name()
        );
    }
}
