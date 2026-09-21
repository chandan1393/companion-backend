package com.xelvo.companion.dashboard.controller;

import com.xelvo.companion.common.api.ApiResponse;
import com.xelvo.companion.dashboard.service.DashboardService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/customer")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<Map<String, Object>> customer() {
        return ApiResponse.success(dashboardService.customerDashboard());
    }

    @GetMapping("/companion")
    @PreAuthorize("hasRole('COMPANION')")
    public ApiResponse<Map<String, Object>> companion() {
        return ApiResponse.success(dashboardService.companionDashboard());
    }
}
