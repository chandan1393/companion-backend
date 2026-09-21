package com.xelvo.companion.payment.repository;

import com.xelvo.companion.payment.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBookingId(Long bookingId);
    Page<Payment> findByCustomerIdOrderByCreatedAtDesc(Long customerId, Pageable pageable);
}
