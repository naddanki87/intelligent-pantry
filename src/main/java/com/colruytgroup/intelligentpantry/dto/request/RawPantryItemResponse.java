package com.colruytgroup.intelligentpantry.dto.request;

public record RawPantryItemResponse(
        String itemName,
        Double quantity,
        String unit
) {
}
