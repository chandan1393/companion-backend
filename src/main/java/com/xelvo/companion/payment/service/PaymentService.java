package com.xelvo.companion.payment.service;

import com.xelvo.companion.booking.entity.Booking;
import com.xelvo.companion.booking.entity.BookingStatus;
import com.xelvo.companion.booking.repository.BookingRepository;
import com.xelvo.companion.common.exception.BusinessException;
import com.xelvo.companion.common.exception.ResourceNotFoundException;
import com.xelvo.companion.payment.dto.*;
import com.xelvo.companion.payment.entity.Payment;
import com.xelvo.companion.payment.repository.PaymentRepository;
import com.xelvo.companion.security.CurrentUserService;
import com.xelvo.companion.common.api.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final CurrentUserService currentUserService;

    public PaymentService(PaymentRepository paymentRepository,
                          BookingRepository bookingRepository,
                          CurrentUserService currentUserService) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public PageResponse<PaymentResponse> mine(Pageable pageable) {
        return PageResponse.from(
                paymentRepository.findByCustomerIdOrderByCreatedAtDesc(
                        currentUserService.get().getId(), pageable
                ).map(this::toResponse)
        );
    }

    @Transactional(readOnly = true)
    public PaymentResponse getByBooking(Long bookingId) {
        Payment payment = get(bookingId);
        verifyOwner(payment);
        return toResponse(payment);
    }

    @Transactional
    public PaymentResponse createProviderOrder(Long bookingId) {
        Payment payment = get(bookingId);
        verifyOwner(payment);

        if (payment.getStatus().name().equals("SUCCESS")) {
            throw new BusinessException("Payment is already completed");
        }

        String orderId = "COMP_ORDER_" + bookingId + "_" + System.currentTimeMillis();
        payment.setProviderOrderId(orderId);
        // Demo implementation: real provider order creation should be done here.
        // Keep provider integration behind this service so controllers remain unchanged.

        return toResponse(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentResponse confirmMockPayment(Long bookingId, MockPaymentRequest request) {
        Payment payment = get(bookingId);
        verifyOwner(payment);

        if (payment.getStatus().name().equals("SUCCESS")) {
            return toResponse(payment);
        }

        String orderId = payment.getProviderOrderId();
        if (orderId == null) {
            orderId = "COMP_ORDER_" + bookingId + "_" + System.currentTimeMillis();
        }

        payment.markSuccess(orderId, request.providerPaymentId());
        Payment saved = paymentRepository.save(payment);

        Booking booking = payment.getBooking();
        if (booking.getStatus() == BookingStatus.PENDING) {
            booking.changeStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);
        }

        return toResponse(saved);
    }

    private Payment get(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for booking: " + bookingId));
    }

    private void verifyOwner(Payment payment) {
        if (!payment.getCustomerId().equals(currentUserService.get().getId())) {
            throw new ResourceNotFoundException("Payment not found");
        }
    }

    private PaymentResponse toResponse(Payment p) {
        return new PaymentResponse(
                p.getId(), p.getBooking().getId(), p.getAmount(),
                p.getProviderOrderId(), p.getProviderPaymentId(), p.getStatus().name()
        );
    }
}
