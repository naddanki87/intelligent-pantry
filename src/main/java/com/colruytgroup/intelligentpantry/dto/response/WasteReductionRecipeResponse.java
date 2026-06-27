package com.colruytgroup.intelligentpantry.dto.response;

import java.util.List;

public record WasteReductionRecipeResponse(

        String recipeName,
        String reason,
        String prepTime,
        List<String> ingredients,
        List<String> instructions
) {
}
