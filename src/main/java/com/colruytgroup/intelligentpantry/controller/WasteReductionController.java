package com.colruytgroup.intelligentpantry.controller;

import com.colruytgroup.intelligentpantry.ai.WasteReductionAiService;
import com.colruytgroup.intelligentpantry.dto.response.WasteAnalysis;
import com.colruytgroup.intelligentpantry.dto.response.WasteReductionResponse;
import com.colruytgroup.intelligentpantry.service.WasteAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class WasteReductionController {

    private final WasteReductionAiService service;
    private final WasteAnalysisService wasteAnalysisService;

    @GetMapping("/waste-reduction")
    public WasteReductionResponse suggestions() {

        return service.generateSuggestions();
    }

    @GetMapping("/waste-analysis")
    public List<WasteAnalysis> wasteAnalysis() {

      return  service.getAllPantryItems() .stream()
                .map(wasteAnalysisService::analyze)
                .sorted(
                        Comparator.comparing(
                                        WasteAnalysis::wasteScore)
                                .reversed())
                .toList();

    }
}
