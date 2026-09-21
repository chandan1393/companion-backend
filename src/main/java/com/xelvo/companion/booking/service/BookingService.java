package com.xelvo.companion.booking.service;

import com.xelvo.companion.availability.entity.AvailabilityStatus;
import com.xelvo.companion.availability.repository.AvailabilityRepository;
import com.xelvo.companion.booking.dto.*;
import com.xelvo.companion.booking.entity.*;
import com.xelvo.companion.booking.repository.BookingRepository;
import com.xelvo.companion.common.api.PageResponse;
import com.xelvo.companion.common.exception.BusinessException;
import com.xelvo.companion.common.exception.ResourceNotFoundException;
import com.xelvo.companion.companion.entity.CompanionProfile;
import com.xelvo.companion.companion.service.CompanionService;
import com.xelvo.companion.experience.entity.Experience;
import com.xelvo.companion.experience.service.ExperienceService;
import com.xelvo.companion.payment.entity.Payment;
import com.xelvo.companion.payment.repository.PaymentRepository;
import com.xelvo.companion.security.CurrentUserService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Service
public class BookingService {

    private static final BigDecimal PLATFORM_FEE_RATE = new BigDecimal("0.10");

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final CompanionService companionService;
    private final ExperienceService experienceService;
    private final AvailabilityRepository availabilityRepository;
    private final CurrentUserService currentUserService;

    public BookingService(BookingRepository bookingRepository,
                          PaymentRepository paymentRepository,
                          CompanionService companionService,
                          ExperienceService experienceService,
                          AvailabilityRepository availabilityRepository,
                          CurrentUserService currentUserService) {
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.companionService = companionService;
        this.experienceService = experienceService;
        this.availabilityRepository = availabilityRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public BookingResponse create(CreateBookingRequest request) {
        CompanionProfile companion = companionService.getById(request.companionId());
        Experience experience = experienceService.get(request.experienceId());

        if (!experience.getCompanion().getId().equals(companion.getId())) {
            throw new BusinessException("Experience does not belong to selected companion");
        }

        if (!experience.isActive()) {
            throw new BusinessException("Selected experience is not active");
        }

        LocalTime endTime = request.startTime().plusMinutes(experience.getDurationMinutes());

        boolean booked = bookingRepository
                .existsByCompanionIdAndBookingDateAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(
                        companion.getId(),
                        request.bookingDate(),
                        List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED),
                        endTime,
                        request.startTime()
                );

        if (booked) {
            throw new BusinessException("Selected time is already booked");
        }

        boolean available = availabilityRepository
                .existsByCompanionIdAndAvailableDateAndStartTimeLessThanAndEndTimeGreaterThanAndStatus(
                        companion.getId(),
                        request.bookingDate(),
                        endTime,
                        request.startTime(),
                        AvailabilityStatus.AVAILABLE
                );

        if (!available) {
            throw new BusinessException("Companion is not available for selected date and time");
        }

        BigDecimal baseAmount = experience.getPrice();
        BigDecimal platformFee = baseAmount.multiply(PLATFORM_FEE_RATE);

        Booking booking = new Booking(
                currentUserService.get(),
                companion,
                experience,
                request.bookingDate(),
                request.startTime(),
                endTime,
                baseAmount,
                platformFee,
                request.location(),
                request.customerNote()
        );

        booking = bookingRepository.save(booking);
        paymentRepository.save(new Payment(booking));

        return toResponse(booking);
    }

    @Transactional(readOnly = true)
    public PageResponse<BookingResponse> customerBookings(Pageable pageable) {
        Long userId = currentUserService.get().getId();
        return PageResponse.from(
                bookingRepository.findByCustomerIdOrderByBookingDateDescStartTimeDesc(userId, pageable)
                        .map(this::toResponse)
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<BookingResponse> companionBookings(Pageable pageable) {
        Long userId = currentUserService.get().getId();
        CompanionProfile companion = companionService.getByIdForUser(userId);
        return PageResponse.from(
                bookingRepository.findByCompanionIdOrderByBookingDateDescStartTimeDesc(companion.getId(), pageable)
                        .map(this::toResponse)
        );
    }

    @Transactional
    public BookingResponse changeStatus(Long id, BookingStatus newStatus) {
        Booking booking = get(id);
        UserAccess access = verifyAccess(booking);

        validateTransition(booking.getStatus(), newStatus, access.companion());
        booking.changeStatus(newStatus);

        if (newStatus == BookingStatus.COMPLETED) {
            booking.getCompanion().incrementCompletedBookings();
        }

        return toResponse(bookingRepository.save(booking));
    }

    @Transactional(readOnly = true)
    public BookingResponse getForCurrentUser(Long id) {
        Booking booking = get(id);
        verifyAccess(booking);
        return toResponse(booking);
    }

    private UserAccess verifyAccess(Booking booking) {
        Long userId = currentUserService.get().getId();

        boolean customer = booking.getCustomer().getId().equals(userId);
        boolean companion = booking.getCompanion().getUser().getId().equals(userId);
        boolean admin = currentUserService.get().getRole().name().equals("ADMIN");

        if (!customer && !companion && !admin) {
            throw new ResourceNotFoundException("Booking not found");
        }

        return new UserAccess(customer, companion, admin);
    }

    private void validateTransition(BookingStatus current, BookingStatus target, boolean companion) {
        if (current == BookingStatus.COMPLETED || current == BookingStatus.CANCELLED) {
            throw new BusinessException("Booking is already closed");
        }

        if (target == BookingStatus.CONFIRMED && !companion) {
            throw new BusinessException("Only companion can confirm a booking");
        }

        if (target == BookingStatus.COMPLETED && !companion) {
            throw new BusinessException("Only companion can complete a booking");
        }

        if (target == BookingStatus.CANCELLED && current == BookingStatus.COMPLETED) {
            throw new BusinessException("Completed booking cannot be cancelled");
        }
    }

    private Booking get(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + id));
    }

    private BookingResponse toResponse(Booking b) {
        return new BookingResponse(
                b.getId(),
                b.getCustomer().getId(),
                b.getCustomer().getFullName(),
                b.getCompanion().getId(),
                b.getCompanion().getDisplayName(),
                b.getExperience().getId(),
                b.getExperience().getTitle(),
                b.getBookingDate(),
                b.getStartTime(),
                b.getEndTime(),
                b.getBaseAmount(),
                b.getPlatformFee(),
                b.getTotalAmount(),
                b.getLocation(),
                b.getStatus().name(),
                b.getCustomerNote()
        );
    }

    private record UserAccess(boolean customer, boolean companion, boolean admin) {}
}
