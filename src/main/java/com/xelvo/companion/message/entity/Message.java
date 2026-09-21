package com.xelvo.companion.message.entity;

import com.xelvo.companion.user.entity.User;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "messages", indexes = {
        @Index(name = "idx_message_recipient_read", columnList = "recipient_id,read_at"),
        @Index(name = "idx_message_conversation", columnList = "sender_id,recipient_id,created_at")
})
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant readAt;

    protected Message() {}

    public Message(User sender, User recipient, String content) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public User getSender() { return sender; }
    public User getRecipient() { return recipient; }
    public String getContent() { return content; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getReadAt() { return readAt; }

    public void markRead() { this.readAt = Instant.now(); }
}
