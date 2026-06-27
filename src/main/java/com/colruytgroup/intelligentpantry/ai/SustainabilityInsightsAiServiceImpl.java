package com.colruytgroup.intelligentpantry.ai;

import com.colruytgroup.intelligentpantry.dto.response.SustainabilityInsightsResponse;
import com.colruytgroup.intelligentpantry.dto.response.WasteAnalysis;
import com.colruytgroup.intelligentpantry.repository.PantryItemRepository;
import com.colruytgroup.intelligentpantry.service.WasteAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SustainabilityInsightsAiServiceImpl implements SustainabilityInsightsAiService {

    private final PantryItemRepository repository;
    private final WasteAnalysisService wasteAnalysisService;
    private final ChatClient chatClient;

    @Override
    public SustainabilityInsightsResponse generateInsights() {

        String inventoryAnalysis = repository.findAll()
                .stream()
                .map(wasteAnalysisService::analyze)
                .map(this::formatAnalysis)
                .collect(Collectors.joining("\n"));
        String prompt = """
                You are a sustainability and food waste advisor.

                Pantry Analysis:

                %s

                Generate:

                1. Executive summary
                2. Waste risks
                3. Sustainability recommendations
                4. Immediate actions

                Return JSON only.

                {
                  "summary":"",
                  "risks":[],
                  "recommendations":[],
                  "immediateActions":[]
                }
                """
                .formatted(inventoryAnalysis);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .entity(
                        SustainabilityInsightsResponse.class);
    }

    private String formatAnalysis(
            WasteAnalysis analysis) {

        return analysis.itemName()
                + " | Days Remaining: "
                + analysis.daysRemaining()
                + " | Risk: "
                + analysis.risk();
    }
}
