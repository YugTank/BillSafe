package com.billsafe.billsafe.dashboard.controller;

import com.billsafe.billsafe.dashboard.dto.DashboardResponse;
import com.billsafe.billsafe.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(){
        DashboardResponse dashboardResponse=dashboardService.getDashboard();
        return ResponseEntity.ok(dashboardResponse);
    }
}
