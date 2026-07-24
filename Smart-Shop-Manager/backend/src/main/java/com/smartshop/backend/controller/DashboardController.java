package com.smartshop.backend.controller;

import com.smartshop.backend.dto.DashboardDTO;
import com.smartshop.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardDTO getDashboard() {
        return dashboardService.getDashboardData();
    }
}
