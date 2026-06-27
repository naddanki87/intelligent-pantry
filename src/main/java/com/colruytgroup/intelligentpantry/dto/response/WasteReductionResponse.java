package com.colruytgroup.intelligentpantry.dto.response;

import java.util.List;

public record WasteReductionResponse(

        List<String> expiringItems,
        List<WasteReductionRecipeResponse> recipes
) {
}
