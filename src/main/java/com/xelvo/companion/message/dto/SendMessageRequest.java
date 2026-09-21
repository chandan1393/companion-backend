package com.xelvo.companion.message.dto;

import jakarta.validation.constraints.*;

public record SendMessageRequest(
        @NotNull Long recipientId,
        @NotBlank @Size(max = 2000) String content
) {}
