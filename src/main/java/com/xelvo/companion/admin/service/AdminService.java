package com.xelvo.companion.admin.service;

import com.xelvo.companion.booking.repository.BookingRepository;
import com.xelvo.companion.booking.entity.BookingStatus;
import com.xelvo.companion.common.api.PageResponse;
import com.xelvo.companion.companion.entity.CompanionProfile;
import com.xelvo.companion.companion.entity.CompanionStatus;
import com.xelvo.companion.companion.repository.CompanionRepository;
import com.xelvo.companion.user.entity.User;
import com.xelvo.companion.user.entity.UserStatus;
import com.xelvo.companion.user.repository.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final CompanionRepository companionRepository;
    private final BookingRepository bookingRepository;

    public AdminService(UserRepository userRepository,
                        CompanionRepository companionRepository,
                        BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.companionRepository = companionRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> dashboard() {
        return Map.of(
                "users", userRepository.count(),
                "companions", companionRepository.count(),
                "pendingCompanions", companionRepository.findByStatus(
                        CompanionStatus.PENDING, PageRequest.of(0, 5)).getTotalElements(),
                "bookings", bookingRepository.count(),
                "confirmedBookings", bookingRepository.countByStatus(BookingStatus.CONFIRMED),
                "completedBookings", bookingRepository.countByStatus(BookingStatus.COMPLETED)
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<UserSummary> users(Pageable pageable) {
        return PageResponse.from(userRepository.findAll(pageable).map(this::toUserSummary));
    }

    @Transactional
    public UserSummary changeUserStatus(Long id, UserStatus status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        user.changeStatus(status);
        return toUserSummary(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public PageResponse<CompanionSummary> companions(Pageable pageable) {
        return PageResponse.from(companionRepository.findAll(pageable).map(this::toCompanionSummary));
    }

    @Transactional(readOnly = true)
    public PageResponse<BookingSummary> bookings(Pageable pageable) {
        return PageResponse.from(bookingRepository.findAll(pageable).map(b ->
                new BookingSummary(
                        b.getId(),
                        b.getCustomer().getFullName(),
                        b.getCompanion().getDisplayName(),
                        b.getExperience().getTitle(),
                        b.getBookingDate(),
                        b.getTotalAmount(),
                        b.getStatus().name()
                )
        ));
    }

    public record UserSummary(Long id, String fullName, String email, String role, String status) {}
    public record CompanionSummary(Long id, String displayName, String city, String status, BigDecimal rating) {}
    public record BookingSummary(Long id, String customerName, String companionName,
                                 String experience, java.time.LocalDate bookingDate,
                                 BigDecimal amount, String status) {}

    private UserSummary toUserSummary(User u) {
        return new UserSummary(u.getId(), u.getFullName(), u.getEmail(), u.getRole().name(), u.getStatus().name());
    }

    private CompanionSummary toCompanionSummary(CompanionProfile p) {
        return new CompanionSummary(p.getId(), p.getDisplayName(), p.getCity(), p.getStatus().name(), p.getRating());
    }
}
