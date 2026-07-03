package com.colruytgroup.intelligentpantry.ai;

import com.colruytgroup.intelligentpantry.dto.request.DietPlannerRequest;
import com.colruytgroup.intelligentpantry.dto.response.DietPlannerAiResponse;
import com.colruytgroup.intelligentpantry.dto.response.DietPlannerResponse;
import com.colruytgroup.intelligentpantry.repository.PantryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DietPlannerAiServiceImpl implements DietPlannerAiService {

    private final PantryItemRepository repository;

    private final ChatClient chatClient;

    @Override
    public DietPlannerResponse generatePlan(DietPlannerRequest request) {
        String inventoryContext =
                buildInventoryContext();
        String prompt = """
                You are an expert nutritionist.

                Dietician Prescription:

                %s

                Pantry Inventory:

                %s

                Tasks:

                1. Identify available ingredients.
                2. Identify missing ingredients.
                3. Suggest substitutions.
                4. Generate recipes using pantry items first.
                5. Generate shopping list only for ingredients
                   that cannot be substituted.

                Return JSON only.

                {
                  "summary":"",
                  "availableIngredients":[],
                  "missingIngredients":[],
                  "substitutions":[],
                  "shoppingList":[],
                  "recipes":[]
                }
                """
                .formatted(
                        request.dietText(),
                        inventoryContext);

        DietPlannerAiResponse response =
                chatClient.prompt()
                        .user(prompt)
                        .call()
                        .entity(
                                DietPlannerAiResponse.class);

        return new DietPlannerResponse(
                response.summary(),
                response.availableIngredients(),
                response.missingIngredients(),
                response.recipes(),
                response.substitutions(),
                response.shoppingList()
        );
    }
    private String buildInventoryContext() {

        return repository.findAll()
                .stream()
                .map(item ->
                        item.getItemName()
                                + " : "
                                + item.getQuantity()
                                + " "
                                + item.getUnit())
                .collect(Collectors.joining("\n"));
    }
}
