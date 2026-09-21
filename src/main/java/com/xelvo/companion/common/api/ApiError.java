package com.xelvo.companion.common.api;

import java.time.Instant;
import java.util.Map;

public record ApiError(
        boolean success,
        String message,
        String path,
        Instant timestamp,
        Map<String, Object> details
) {}
