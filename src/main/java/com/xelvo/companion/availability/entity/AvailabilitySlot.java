package com.xelvo.companion.availability.entity;

import com.xelvo.companion.companion.entity.CompanionProfile;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "availability_slots", indexes = {
        @Index(name = "idx_availability_companion_date", columnList = "companion_id,available_date")
})
public class AvailabilitySlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "companion_id", nullable = false)
    private CompanionProfile companion;

    @Column(nullable = false)
    private LocalDate availableDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AvailabilityStatus status;

    protected AvailabilitySlot() {}

    public AvailabilitySlot(CompanionProfile companion, LocalDate availableDate,
                            LocalTime startTime, LocalTime endTime) {
        this.companion = companion;
        this.availableDate = availableDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = AvailabilityStatus.AVAILABLE;
    }

    public Long getId() { return id; }
    public CompanionProfile getCompanion() { return companion; }
    public LocalDate getAvailableDate() { return availableDate; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public AvailabilityStatus getStatus() { return status; }

    public void changeStatus(AvailabilityStatus status) { this.status = status; }
}
