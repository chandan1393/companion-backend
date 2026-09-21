package com.xelvo.companion.auth.dto;

public record MeResponse(
        Long id,
        String fullName,
        String email,
        String role,
        String status,
        String city
) {}
