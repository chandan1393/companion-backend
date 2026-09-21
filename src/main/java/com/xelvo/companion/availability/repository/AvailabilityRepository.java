package com.xelvo.companion.availability.repository;

import com.xelvo.companion.availability.entity.AvailabilitySlot;
import com.xelvo.companion.availability.entity.AvailabilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AvailabilityRepository extends JpaRepository<AvailabilitySlot, Long> {
    List<AvailabilitySlot> findByCompanionIdAndAvailableDateBetweenOrderByAvailableDateAscStartTimeAsc(
            Long companionId, LocalDate from, LocalDate to);

    boolean existsByCompanionIdAndAvailableDateAndStartTimeLessThanAndEndTimeGreaterThanAndStatus(
            Long companionId, LocalDate date, LocalTime endTime, LocalTime startTime, AvailabilityStatus status);
}
