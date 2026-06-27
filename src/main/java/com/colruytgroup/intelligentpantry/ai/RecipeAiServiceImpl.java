package com.colruytgroup.intelligentpantry.ai;

import com.colruytgroup.intelligentpantry.dto.response.RecipeRecommendationResponse;
import com.colruytgroup.intelligentpantry.entity.PantryItem;
import com.colruytgroup.intelligentpantry.repository.PantryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeAiServiceImpl implements RecipeAiService{

    private final ChatClient chatClient;
    private final PantryItemRepository pantryItemRepository;

    @Override
    public RecipeRecommendationResponse recommendRecipes() {

        List<PantryItem> items =
                pantryItemRepository.findAll();

        String pantryItems =
                items.stream()
                        .map(item ->
                                item.getItemName()
                                        + " : "
                                        + item.getQuantity()
                                        + " "
                                        + item.getUnit())
                        .collect(Collectors.joining("\n"));

        String prompt = """
                You are a professional chef.

                Create exactly 5 recipes.

                Use the pantry items below as much as possible.

                Pantry Items:
                %s

                Return valid JSON only.

                {
                  "recipes":[
                    {
                      "name":"",
                      "prepTime":"",
                      "ingredients":[],
                      "instructions":[]
                    }
                  ]
                }
                """.formatted(pantryItems);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .entity(
                        RecipeRecommendationResponse.class);
    }
}
