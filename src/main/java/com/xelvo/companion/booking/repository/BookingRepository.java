package com.xelvo.companion.booking.repository;

import com.xelvo.companion.booking.entity.Booking;
import com.xelvo.companion.booking.entity.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByCustomerIdOrderByBookingDateDescStartTimeDesc(Long customerId, Pageable pageable);
    Page<Booking> findByCompanionIdOrderByBookingDateDescStartTimeDesc(Long companionId, Pageable pageable);
    List<Booking> findByCustomerIdOrderByBookingDateDescStartTimeDesc(Long customerId);
    List<Booking> findByCompanionIdOrderByBookingDateDescStartTimeDesc(Long companionId);
    long countByCustomerId(Long customerId);
    long countByCompanionId(Long companionId);
    long countByStatus(BookingStatus status);

    boolean existsByCompanionIdAndBookingDateAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(
            Long companionId, LocalDate bookingDate, List<BookingStatus> statuses,
            LocalTime endTime, LocalTime startTime);
}
