package com.xelvo.companion.companion.entity;

import com.xelvo.companion.user.entity.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companion_profiles", indexes = {
        @Index(name = "idx_companion_status_city", columnList = "status,city"),
        @Index(name = "idx_companion_user", columnList = "user_id", unique = true)
})
public class CompanionProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 100)
    private String displayName;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 20)
    private String gender;

    @Column(length = 1200)
    private String bio;

    @Column(length = 500)
    private String tagline;

    @Column(length = 500)
    private String coverPhotoUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CompanionStatus status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal rating;

    @Column(nullable = false)
    private Integer reviewCount;

    @Column(nullable = false)
    private Integer completedBookings;

    @Column(nullable = false)
    private Integer profileCompletion;

    @ElementCollection
    @CollectionTable(name = "companion_interests", joinColumns = @JoinColumn(name = "companion_id"))
    @Column(name = "interest", nullable = false, length = 60)
    private List<String> interests = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "companion_photos", joinColumns = @JoinColumn(name = "companion_id"))
    @Column(name = "photo_url", nullable = false, length = 500)
    private List<String> photoUrls = new ArrayList<>();

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected CompanionProfile() {
    }

    public CompanionProfile(User user, String displayName, Integer age, String city,
                            String gender, String bio, BigDecimal hourlyRate) {
        this.user = user;
        this.displayName = displayName;
        this.age = age;
        this.city = city;
        this.gender = gender;
        this.bio = bio;
        this.hourlyRate = hourlyRate;
        this.status = CompanionStatus.PENDING;
        this.rating = BigDecimal.ZERO;
        this.reviewCount = 0;
        this.completedBookings = 0;
        this.profileCompletion = 20;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    void updateTimestamp() {
        this.updatedAt = Instant.now();
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getDisplayName() { return displayName; }
    public Integer getAge() { return age; }
    public String getCity() { return city; }
    public String getGender() { return gender; }
    public String getBio() { return bio; }
    public String getTagline() { return tagline; }
    public String getCoverPhotoUrl() { return coverPhotoUrl; }
    public CompanionStatus getStatus() { return status; }
    public BigDecimal getHourlyRate() { return hourlyRate; }
    public BigDecimal getRating() { return rating; }
    public Integer getReviewCount() { return reviewCount; }
    public Integer getCompletedBookings() { return completedBookings; }
    public Integer getProfileCompletion() { return profileCompletion; }
    public List<String> getInterests() { return List.copyOf(interests); }
    public List<String> getPhotoUrls() { return List.copyOf(photoUrls); }

    public void updateInterests(List<String> values) {
        interests.clear();
        if (values != null) {
            interests.addAll(values.stream().distinct().limit(20).toList());
        }
    }

    public void addPhoto(String url) {
        if (photoUrls.size() >= 6) {
            throw new com.xelvo.companion.common.exception.BusinessException("Maximum 6 profile photos allowed");
        }
        photoUrls.add(url);
    }

    public void removePhoto(String url) {
        photoUrls.remove(url);
    }

    public void updateProfile(String displayName, Integer age, String city, String gender,
                              String bio, String tagline, BigDecimal hourlyRate) {
        this.displayName = displayName;
        this.age = age;
        this.city = city;
        this.gender = gender;
        this.bio = bio;
        this.tagline = tagline;
        this.hourlyRate = hourlyRate;
    }

    public void setCoverPhotoUrl(String url) { this.coverPhotoUrl = url; }
    public void changeStatus(CompanionStatus status) { this.status = status; }

    public void addReview(int newReviewCount, BigDecimal newRating) {
        this.reviewCount = newReviewCount;
        this.rating = newRating;
    }

    public void incrementCompletedBookings() {
        this.completedBookings++;
    }
}
