package com.colruytgroup.intelligentpantry.dto;

import com.colruytgroup.intelligentpantry.enums.Category;

import java.time.LocalDate;

public record PantryItemResponse(

        Long id,
        String itemName,
        Double quantity,
        String unit,
        LocalDate purchaseDate,
        LocalDate expiryDate,
        Category category
) {
}
