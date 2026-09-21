package com.xelvo.companion.experience.entity;

import com.xelvo.companion.companion.entity.CompanionProfile;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "experiences", indexes = {
        @Index(name = "idx_experience_companion_active", columnList = "companion_id,active")
})
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "companion_id", nullable = false)
    private CompanionProfile companion;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private boolean active;

    @Column(length = 50)
    private String category;

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected Experience() {
    }

    public Experience(CompanionProfile companion, String title, String description,
                      BigDecimal price, Integer durationMinutes, String category) {
        this.companion = companion;
        this.title = title;
        this.description = description;
        this.price = price;
        this.durationMinutes = durationMinutes;
        this.category = category;
        this.active = true;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    void updateTimestamp() { this.updatedAt = Instant.now(); }

    public Long getId() { return id; }
    public CompanionProfile getCompanion() { return companion; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public boolean isActive() { return active; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }

    public void update(String title, String description, BigDecimal price,
                       Integer durationMinutes, String category) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.durationMinutes = durationMinutes;
        this.category = category;
    }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setActive(boolean active) { this.active = active; }
}
