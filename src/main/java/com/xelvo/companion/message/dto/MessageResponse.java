package com.xelvo.companion.message.dto;

import java.time.Instant;

public record MessageResponse(
        Long id,
        Long senderId,
        String senderName,
        Long recipientId,
        String recipientName,
        String content,
        Instant createdAt,
        Instant readAt
) {}
