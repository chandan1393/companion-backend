package com.xelvo.companion.booking.entity;

import com.xelvo.companion.companion.entity.CompanionProfile;
import com.xelvo.companion.experience.entity.Experience;
import com.xelvo.companion.user.entity.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "bookings", indexes = {
        @Index(name = "idx_booking_customer", columnList = "customer_id"),
        @Index(name = "idx_booking_companion_date", columnList = "companion_id,booking_date"),
        @Index(name = "idx_booking_status", columnList = "status")
})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "companion_id", nullable = false)
    private CompanionProfile companion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "experience_id", nullable = false)
    private Experience experience;

    @Column(nullable = false)
    private LocalDate bookingDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal baseAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal platformFee;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(length = 300)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;

    @Column(length = 1000)
    private String customerNote;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected Booking() {}

    public Booking(User customer, CompanionProfile companion, Experience experience,
                   LocalDate bookingDate, LocalTime startTime, LocalTime endTime,
                   BigDecimal baseAmount, BigDecimal platformFee, String location, String customerNote) {
        this.customer = customer;
        this.companion = companion;
        this.experience = experience;
        this.bookingDate = bookingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.baseAmount = baseAmount;
        this.platformFee = platformFee;
        this.totalAmount = baseAmount.add(platformFee);
        this.location = location;
        this.customerNote = customerNote;
        this.status = BookingStatus.PENDING;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    void updateTimestamp() { this.updatedAt = Instant.now(); }

    public Long getId() { return id; }
    public User getCustomer() { return customer; }
    public CompanionProfile getCompanion() { return companion; }
    public Experience getExperience() { return experience; }
    public LocalDate getBookingDate() { return bookingDate; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public BigDecimal getBaseAmount() { return baseAmount; }
    public BigDecimal getPlatformFee() { return platformFee; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getLocation() { return location; }
    public BookingStatus getStatus() { return status; }
    public String getCustomerNote() { return customerNote; }

    public void changeStatus(BookingStatus status) { this.status = status; }
}
