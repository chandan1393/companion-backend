package com.xelvo.companion.user.controller;

import com.xelvo.companion.common.api.ApiResponse;
import com.xelvo.companion.user.dto.*;
import com.xelvo.companion.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/me")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ApiResponse<UserResponse> me() {
        return ApiResponse.success(userService.me());
    }

    @PutMapping
    public ApiResponse<UserResponse> update(@Valid @RequestBody UpdateUserProfileRequest request) {
        return ApiResponse.success("Profile updated", userService.update(request));
    }
}
