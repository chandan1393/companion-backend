package com.xelvo.companion.admin.controller;

import com.xelvo.companion.admin.service.AdminService;
import com.xelvo.companion.common.api.*;
import com.xelvo.companion.user.entity.UserStatus;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public ApiResponse<java.util.Map<String, Object>> dashboard() {
        return ApiResponse.success(adminService.dashboard());
    }

    @GetMapping("/users")
    public ApiResponse<PageResponse<AdminService.UserSummary>> users(
            @PageableDefault(size = 25, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.success(adminService.users(pageable));
    }

    @PatchMapping("/users/{id}/status")
    public ApiResponse<AdminService.UserSummary> changeUserStatus(
            @PathVariable Long id,
            @RequestParam UserStatus status) {
        return ApiResponse.success("User status updated", adminService.changeUserStatus(id, status));
    }

    @GetMapping("/companions")
    public ApiResponse<PageResponse<AdminService.CompanionSummary>> companions(
            @PageableDefault(size = 25, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.success(adminService.companions(pageable));
    }

    @GetMapping("/bookings")
    public ApiResponse<PageResponse<AdminService.BookingSummary>> bookings(
            @PageableDefault(size = 25, sort = "bookingDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.success(adminService.bookings(pageable));
    }
}
