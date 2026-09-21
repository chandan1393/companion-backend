package com.xelvo.companion.payment.entity;

import com.xelvo.companion.booking.entity.Booking;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments", indexes = {
        @Index(name = "idx_payment_booking", columnList = "booking_id", unique = true),
        @Index(name = "idx_payment_customer", columnList = "customer_id")
})
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(length = 120)
    private String providerOrderId;

    @Column(length = 120)
    private String providerPaymentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected Payment() {}

    public Payment(Booking booking) {
        this.booking = booking;
        this.customerId = booking.getCustomer().getId();
        this.amount = booking.getTotalAmount();
        this.status = PaymentStatus.CREATED;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    void updateTimestamp() { this.updatedAt = Instant.now(); }

    public Long getId() { return id; }
    public Booking getBooking() { return booking; }
    public Long getCustomerId() { return customerId; }
    public BigDecimal getAmount() { return amount; }
    public String getProviderOrderId() { return providerOrderId; }
    public String getProviderPaymentId() { return providerPaymentId; }
    public PaymentStatus getStatus() { return status; }

    public void setProviderOrderId(String providerOrderId) {
        this.providerOrderId = providerOrderId;
    }

    public void markSuccess(String providerOrderId, String providerPaymentId) {
        this.providerOrderId = providerOrderId;
        this.providerPaymentId = providerPaymentId;
        this.status = PaymentStatus.SUCCESS;
    }

    public void markFailed() { this.status = PaymentStatus.FAILED; }
}
