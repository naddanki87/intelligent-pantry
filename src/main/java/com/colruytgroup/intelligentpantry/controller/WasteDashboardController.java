package com.colruytgroup.intelligentpantry.controller;


import com.colruytgroup.intelligentpantry.dto.response.WasteAnalysis;
import com.colruytgroup.intelligentpantry.dto.response.WasteDashboardResponse;
import com.colruytgroup.intelligentpantry.service.WasteDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class WasteDashboardController {

    private final WasteDashboardService service;

    @GetMapping("/waste")
    public WasteDashboardResponse getWasteDashboard() {
        return service.getDashboard();
    }

    @GetMapping("/waste/top-risk")
    public List<WasteAnalysis> getTopRiskItems() {

        return service.getDashboard()
                .items()
                .stream()
                .limit(5)
                .toList();
    }
}
