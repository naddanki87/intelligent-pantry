package com.colruytgroup.intelligentpantry.dto;

import com.colruytgroup.intelligentpantry.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PantryItemRequest(

        @NotBlank
        String itemName,

        @NotNull
        Double quantity,

        @NotBlank
        String unit,

        LocalDate purchaseDate,

        LocalDate expiryDate,

        Category category
) {
}
