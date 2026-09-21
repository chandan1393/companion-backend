package com.xelvo.companion.favorite.service;

import com.xelvo.companion.common.exception.DuplicateResourceException;
import com.xelvo.companion.common.exception.ResourceNotFoundException;
import com.xelvo.companion.companion.entity.CompanionProfile;
import com.xelvo.companion.companion.service.CompanionService;
import com.xelvo.companion.favorite.entity.Favorite;
import com.xelvo.companion.favorite.repository.FavoriteRepository;
import com.xelvo.companion.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final CompanionService companionService;
    private final CurrentUserService currentUserService;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           CompanionService companionService,
                           CurrentUserService currentUserService) {
        this.favoriteRepository = favoriteRepository;
        this.companionService = companionService;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public void add(Long companionId) {
        if (favoriteRepository.existsByUserIdAndCompanionId(
                currentUserService.get().getId(), companionId)) {
            throw new DuplicateResourceException("Companion is already in favorites");
        }

        CompanionProfile companion = companionService.getById(companionId);
        favoriteRepository.save(new Favorite(currentUserService.get(), companion));
    }

    @Transactional
    public void remove(Long companionId) {
        if (!favoriteRepository.existsByUserIdAndCompanionId(
                currentUserService.get().getId(), companionId)) {
            throw new ResourceNotFoundException("Favorite not found");
        }

        favoriteRepository.deleteByUserIdAndCompanionId(
                currentUserService.get().getId(), companionId);
    }

    @Transactional(readOnly = true)
    public List<Favorite> mine() {
        return favoriteRepository.findByUserIdOrderByCreatedAtDesc(currentUserService.get().getId());
    }

    @Transactional(readOnly = true)
    public boolean isFavorite(Long companionId) {
        return favoriteRepository.existsByUserIdAndCompanionId(
                currentUserService.get().getId(), companionId);
    }
}
