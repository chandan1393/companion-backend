package com.xelvo.companion.companion.repository;

import com.xelvo.companion.companion.entity.CompanionProfile;
import com.xelvo.companion.companion.entity.CompanionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CompanionRepository extends JpaRepository<CompanionProfile, Long> {
    Optional<CompanionProfile> findByUserId(Long userId);
    Page<CompanionProfile> findByStatusAndCityIgnoreCase(CompanionStatus status, String city, Pageable pageable);
    Page<CompanionProfile> findByStatus(CompanionStatus status, Pageable pageable);
}
