package com.xelvo.companion.favorite.entity;

import com.xelvo.companion.companion.entity.CompanionProfile;
import com.xelvo.companion.user.entity.User;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "favorites",
        uniqueConstraints = @UniqueConstraint(name = "uk_favorite_user_companion",
                columnNames = {"user_id", "companion_id"}),
        indexes = @Index(name = "idx_favorite_user", columnList = "user_id"))
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "companion_id", nullable = false)
    private CompanionProfile companion;

    @Column(nullable = false)
    private Instant createdAt;

    protected Favorite() {}

    public Favorite(User user, CompanionProfile companion) {
        this.user = user;
        this.companion = companion;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public CompanionProfile getCompanion() { return companion; }
    public Instant getCreatedAt() { return createdAt; }
}
