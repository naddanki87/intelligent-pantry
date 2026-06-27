package com.colruytgroup.intelligentpantry.dto.response;

import com.colruytgroup.intelligentpantry.enums.Category;

public record PantryItemAiResponse(

        String itemName,
        Double quantity,
        String unit,
        Category category
) {
}
