package com.colruytgroup.intelligentpantry.controller;

import com.colruytgroup.intelligentpantry.ai.RecipeAiService;
import com.colruytgroup.intelligentpantry.dto.response.RecipeRecommendationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class RecipeAiController {

    private final RecipeAiService recipeAiService;

    @GetMapping("/recipes")
    public RecipeRecommendationResponse recipes() {

        return recipeAiService.recommendRecipes();
    }
}
