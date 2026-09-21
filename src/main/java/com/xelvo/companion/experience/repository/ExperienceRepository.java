package com.xelvo.companion.experience.repository;

import com.xelvo.companion.experience.entity.Experience;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    Page<Experience> findByActiveTrue(Pageable pageable);
    List<Experience> findByCompanionIdAndActiveTrue(Long companionId);
    List<Experience> findByCompanionId(Long companionId);
}
