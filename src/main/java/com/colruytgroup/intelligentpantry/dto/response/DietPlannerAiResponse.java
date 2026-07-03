package com.colruytgroup.intelligentpantry.dto.response;

import java.util.List;

public record DietPlannerAiResponse(

        String summary,

        List<String> availableIngredients,

        List<String> missingIngredients,

        List<String> substitutions,

        List<String> shoppingList,

        List<RecipeResponse> recipes
) {
}
