package com.colruytgroup.intelligentpantry.ai;


import com.colruytgroup.intelligentpantry.dto.response.WasteAnalysis;
import com.colruytgroup.intelligentpantry.dto.response.WasteReductionResponse;
import com.colruytgroup.intelligentpantry.entity.PantryItem;
import com.colruytgroup.intelligentpantry.repository.PantryItemRepository;
import com.colruytgroup.intelligentpantry.service.WasteAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WasteReductionAiServiceImpl implements WasteReductionAiService {

    private final ChatClient chatClient;
    private final PantryItemRepository repository;
    private final WasteAnalysisService wasteAnalysisService;

    @Override
    public WasteReductionResponse generateSuggestions() {

        List<PantryItem> expiringItems =
                repository.findByExpiryDateBetween(
                        LocalDate.now(),
                        LocalDate.now().plusDays(3));

        List<WasteAnalysis> analyses =
                expiringItems.stream()
                        .map(wasteAnalysisService::analyze)
                        .toList();

        String wasteContext =
                analyses.stream()
                        .map(wastAnalysisReport ->
                                wastAnalysisReport.itemName()
                                        + " | Days Left: "
                                        + wastAnalysisReport.daysRemaining()
                                        + " | Risk: "
                                        + wastAnalysisReport.risk()
                                        + " | Waste Score: "
                                        + wastAnalysisReport.wasteScore())
                        .collect(Collectors.joining("\n"));

        String prompt = """
                    You are a sustainability and food waste reduction expert.
                    
                    Inventory Analysis:
                    
                    %s
                    
                    Generate 5 recipes.
                    
                    Rules:
                    1. Prioritize HIGH risk items.
                    2. Prioritize higher waste scores.
                    3. Minimize food waste.
                    4. Explain why each recipe is recommended.
                    
                    Return JSON only.
                    
                    {
                      "recipes":[
                        {
                          "recipeName":"",
                          "reason":"",
                          "prepTime":"",
                          "ingredients":[],
                          "instructions":[]
                        }
                      ]
                    }
                    """.formatted(wasteContext);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .entity(WasteReductionResponse.class);
    }

    @Override
    public List<PantryItem> getAllPantryItems() {
        return repository.findAll();
    }
}
