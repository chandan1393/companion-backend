package com.xelvo.companion.user.dto;

public record UserResponse(
        Long id, String fullName, String email, String phone,
        String city, String role, String status
) {}
