package com.xelvo.companion.favorite.controller;

import com.xelvo.companion.common.api.ApiResponse;
import com.xelvo.companion.favorite.service.FavoriteService;
import com.xelvo.companion.companion.dto.CompanionResponse;
import com.xelvo.companion.companion.service.CompanionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@PreAuthorize("hasRole('USER')")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final CompanionService companionService;

    public FavoriteController(FavoriteService favoriteService, CompanionService companionService) {
        this.favoriteService = favoriteService;
        this.companionService = companionService;
    }

    @GetMapping
    public ApiResponse<List<CompanionResponse>> mine() {
        return ApiResponse.success(
                favoriteService.mine().stream()
                        .map(f -> companionService.getPublicProfile(f.getCompanion().getId()))
                        .toList()
        );
    }

    @PostMapping("/{companionId}")
    public ApiResponse<Void> add(@PathVariable Long companionId) {
        favoriteService.add(companionId);
        return ApiResponse.success("Added to favorites", null);
    }

    @DeleteMapping("/{companionId}")
    public ApiResponse<Void> remove(@PathVariable Long companionId) {
        favoriteService.remove(companionId);
        return ApiResponse.success("Removed from favorites", null);
    }

    @GetMapping("/{companionId}/exists")
    public ApiResponse<Boolean> exists(@PathVariable Long companionId) {
        return ApiResponse.success(favoriteService.isFavorite(companionId));
    }
}
