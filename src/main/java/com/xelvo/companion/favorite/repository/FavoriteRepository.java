package com.xelvo.companion.favorite.repository;

import com.xelvo.companion.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Favorite> findByUserIdAndCompanionId(Long userId, Long companionId);
    boolean existsByUserIdAndCompanionId(Long userId, Long companionId);
    void deleteByUserIdAndCompanionId(Long userId, Long companionId);
}
