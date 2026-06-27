package com.colruytgroup.intelligentpantry.controller;

import com.colruytgroup.intelligentpantry.ai.SustainabilityInsightsAiService;
import com.colruytgroup.intelligentpantry.dto.response.SustainabilityInsightsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class SustainabilityInsightsController {

    private final SustainabilityInsightsAiService service;

    @GetMapping("/sustainability-insights")
    public SustainabilityInsightsResponse insights() {

        return service.generateInsights();
    }
}
