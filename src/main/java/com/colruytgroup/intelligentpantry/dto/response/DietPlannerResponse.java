package com.colruytgroup.intelligentpantry.dto.response;

import java.util.List;

public record DietPlannerResponse(

        String summary,

        List<String> availableIngredients,

        List<String> missingIngredients,

        List<RecipeResponse> recipes,

        List<String> substitutions,

        List<String> shoppingList
) {
}
