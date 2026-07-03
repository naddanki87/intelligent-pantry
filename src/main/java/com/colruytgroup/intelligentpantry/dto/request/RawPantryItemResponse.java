package com.colruytgroup.intelligentpantry.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record RawPantryItemResponse(
        String itemName,
        Double quantity,
        String unit,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate expiryDate
) {
}
