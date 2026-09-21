package com.xelvo.companion.user.service;

import com.xelvo.companion.security.CurrentUserService;
import com.xelvo.companion.user.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final CurrentUserService currentUserService;

    public UserService(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public UserResponse me() {
        return toResponse(currentUserService.get());
    }

    @Transactional
    public UserResponse update(UpdateUserProfileRequest request) {
        var user = currentUserService.get();
        user.updateProfile(request.fullName().trim(), request.phone(), request.city());
        return toResponse(user);
    }

    private UserResponse toResponse(com.xelvo.companion.user.entity.User user) {
        return new UserResponse(
                user.getId(), user.getFullName(), user.getEmail(), user.getPhone(),
                user.getCity(), user.getRole().name(), user.getStatus().name()
        );
    }
}
