package com.colruytgroup.intelligentpantry.controller;

import com.colruytgroup.intelligentpantry.ai.DietPlannerAiService;
import com.colruytgroup.intelligentpantry.dto.request.DietPlannerRequest;
import com.colruytgroup.intelligentpantry.dto.response.DietPlannerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class DietPlannerController {

    private final DietPlannerAiService service;

    @PostMapping("/diet-planner")
    public DietPlannerResponse plan(
            @RequestBody
            @Valid
            DietPlannerRequest request) {

        return service.generatePlan(request);
    }
}
