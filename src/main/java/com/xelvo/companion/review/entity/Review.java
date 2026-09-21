package com.xelvo.companion.review.entity;

import com.xelvo.companion.booking.entity.Booking;
import com.xelvo.companion.companion.entity.CompanionProfile;
import com.xelvo.companion.user.entity.User;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "reviews", indexes = {
        @Index(name = "idx_review_companion", columnList = "companion_id"),
        @Index(name = "idx_review_booking", columnList = "booking_id", unique = true)
})
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "companion_id", nullable = false)
    private CompanionProfile companion;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, length = 1000)
    private String comment;

    @Column(nullable = false)
    private Instant createdAt;

    protected Review() {}

    public Review(Booking booking, User customer, CompanionProfile companion, Integer rating, String comment) {
        this.booking = booking;
        this.customer = customer;
        this.companion = companion;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public Booking getBooking() { return booking; }
    public User getCustomer() { return customer; }
    public CompanionProfile getCompanion() { return companion; }
    public Integer getRating() { return rating; }
    public String getComment() { return comment; }
    public Instant getCreatedAt() { return createdAt; }
}
