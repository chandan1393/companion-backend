package com.xelvo.companion.dashboard.service;

import com.xelvo.companion.booking.entity.BookingStatus;
import com.xelvo.companion.booking.repository.BookingRepository;
import com.xelvo.companion.companion.entity.CompanionProfile;
import com.xelvo.companion.companion.service.CompanionService;
import com.xelvo.companion.favorite.repository.FavoriteRepository;
import com.xelvo.companion.message.repository.MessageRepository;
import com.xelvo.companion.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final CurrentUserService currentUserService;
    private final BookingRepository bookingRepository;
    private final FavoriteRepository favoriteRepository;
    private final MessageRepository messageRepository;
    private final CompanionService companionService;

    public DashboardService(CurrentUserService currentUserService,
                            BookingRepository bookingRepository,
                            FavoriteRepository favoriteRepository,
                            MessageRepository messageRepository,
                            CompanionService companionService) {
        this.currentUserService = currentUserService;
        this.bookingRepository = bookingRepository;
        this.favoriteRepository = favoriteRepository;
        this.messageRepository = messageRepository;
        this.companionService = companionService;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> customerDashboard() {
        Long userId = currentUserService.get().getId();

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalBookings", bookingRepository.countByCustomerId(userId));
        data.put("favorites", favoriteRepository.findByUserIdOrderByCreatedAtDesc(userId).size());
        data.put("unreadMessages", messageRepository.countByRecipientIdAndReadAtIsNull(userId));
        data.put("completedBookings", bookingRepository.findByCustomerIdOrderByBookingDateDescStartTimeDesc(userId)
                .stream().filter(b -> b.getStatus() == BookingStatus.COMPLETED).count());

        return data;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> companionDashboard() {
        CompanionProfile companion = companionService.getByIdForUser(currentUserService.get().getId());

        var bookings = bookingRepository.findByCompanionIdOrderByBookingDateDescStartTimeDesc(companion.getId());

        BigDecimal earnings = bookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.COMPLETED || b.getStatus() == BookingStatus.CONFIRMED)
                .map(b -> b.getBaseAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("upcomingBookings", bookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.PENDING || b.getStatus() == BookingStatus.CONFIRMED)
                .count());
        data.put("totalBookings", bookings.size());
        data.put("earnings", earnings);
        data.put("rating", companion.getRating());
        data.put("reviewCount", companion.getReviewCount());
        data.put("completedBookings", companion.getCompletedBookings());
        data.put("unreadMessages",
                messageRepository.countByRecipientIdAndReadAtIsNull(currentUserService.get().getId()));

        return data;
    }
}
