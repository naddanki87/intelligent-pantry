package com.colruytgroup.intelligentpantry.dto.response;

import java.util.List;

public record RecipeResponse(
        String name,
        String prepTime,
        List<String> ingredients,
        List<String> instructions
) {
}
