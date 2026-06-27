package com.colruytgroup.intelligentpantry.service.impl;

import com.colruytgroup.intelligentpantry.dto.response.WasteAnalysis;
import com.colruytgroup.intelligentpantry.dto.response.WasteDashboardResponse;
import com.colruytgroup.intelligentpantry.enums.WasteRisk;
import com.colruytgroup.intelligentpantry.repository.PantryItemRepository;
import com.colruytgroup.intelligentpantry.service.WasteAnalysisService;
import com.colruytgroup.intelligentpantry.service.WasteDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WasteDashboardServiceImpl implements WasteDashboardService {

    private final PantryItemRepository repository;

    private final WasteAnalysisService wasteAnalysisService;

    @Override
    public WasteDashboardResponse getDashboard() {

        List<WasteAnalysis> analyses =

        repository.findAll()
                .stream()
                .map(wasteAnalysisService::analyze)
                .sorted(
                        Comparator.comparing(
                                        WasteAnalysis::wasteScore)
                                .reversed())
                .toList();

        int highRisk =
                (int) analyses.stream()
                        .filter(wasteAnalyzeReport -> wasteAnalyzeReport.risk() == WasteRisk.HIGH)
                        .count();

        int mediumRisk =
                (int) analyses.stream()
                        .filter(wasteAnalyzeReport -> wasteAnalyzeReport.risk() == WasteRisk.MEDIUM)
                        .count();


        int lowRisk =
                (int) analyses.stream()
                        .filter(wasteAnalyzeReport -> wasteAnalyzeReport.risk() == WasteRisk.LOW)
                        .count();


        return new WasteDashboardResponse(
                analyses.size(),
                highRisk,
                mediumRisk,
                lowRisk,
                analyses
        );
    }
}
