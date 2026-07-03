package com.colruytgroup.intelligentpantry.dto.response;

import com.colruytgroup.intelligentpantry.enums.Category;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record PantryItemAiResponse(

        String itemName,
        Double quantity,
        String unit,
        Category category,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate expiryDate
) {
}
