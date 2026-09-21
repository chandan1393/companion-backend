package com.xelvo.companion.experience.service;

import com.xelvo.companion.common.api.PageResponse;
import com.xelvo.companion.common.exception.ResourceNotFoundException;
import com.xelvo.companion.companion.entity.CompanionProfile;
import com.xelvo.companion.companion.service.CompanionService;
import com.xelvo.companion.experience.dto.*;
import com.xelvo.companion.experience.entity.Experience;
import com.xelvo.companion.experience.repository.ExperienceRepository;
import com.xelvo.companion.security.CurrentUserService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final CompanionService companionService;
    private final CurrentUserService currentUserService;

    public ExperienceService(ExperienceRepository experienceRepository,
                              CompanionService companionService,
                              CurrentUserService currentUserService) {
        this.experienceRepository = experienceRepository;
        this.companionService = companionService;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public PageResponse<ExperienceResponse> publicList(Pageable pageable) {
        return PageResponse.from(
                experienceRepository.findByActiveTrue(pageable).map(this::toResponse)
        );
    }

    @Transactional(readOnly = true)
    public List<ExperienceResponse> publicByCompanion(Long companionId) {
        return experienceRepository.findByCompanionIdAndActiveTrue(companionId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ExperienceResponse> myExperiences() {
        CompanionProfile companion = companionService.getByIdForUser(currentUserService.get().getId());
        return experienceRepository.findByCompanionId(companion.getId())
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public ExperienceResponse create(ExperienceRequest request) {
        CompanionProfile companion = companionService.getByIdForUser(currentUserService.get().getId());

        Experience experience = new Experience(
                companion, request.title().trim(), request.description().trim(),
                request.price(), request.durationMinutes(), request.category()
        );

        return toResponse(experienceRepository.save(experience));
    }

    @Transactional
    public ExperienceResponse update(Long id, ExperienceRequest request) {
        Experience experience = get(id);
        ensureOwner(experience);

        experience.update(
                request.title().trim(), request.description().trim(),
                request.price(), request.durationMinutes(), request.category()
        );

        return toResponse(experienceRepository.save(experience));
    }

    @Transactional
    public void deactivate(Long id) {
        Experience experience = get(id);
        ensureOwner(experience);
        experience.setActive(false);
        experienceRepository.save(experience);
    }

    public Experience get(Long id) {
        return experienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found: " + id));
    }

    private void ensureOwner(Experience experience) {
        Long currentUserId = currentUserService.get().getId();
        if (!experience.getCompanion().getUser().getId().equals(currentUserId)) {
            throw new ResourceNotFoundException("Experience not found");
        }
    }

    private ExperienceResponse toResponse(Experience e) {
        return new ExperienceResponse(
                e.getId(), e.getCompanion().getId(), e.getCompanion().getDisplayName(),
                e.getTitle(), e.getDescription(), e.getPrice(), e.getDurationMinutes(),
                e.getCategory(), e.getImageUrl(), e.isActive()
        );
    }
}
