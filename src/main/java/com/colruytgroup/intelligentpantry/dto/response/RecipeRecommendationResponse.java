package com.colruytgroup.intelligentpantry.dto.response;

import java.util.List;

public record RecipeRecommendationResponse(
        List<RecipeResponse> recipes
) {
}
