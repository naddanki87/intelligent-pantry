package com.colruytgroup.intelligentpantry.dto.response;

import com.colruytgroup.intelligentpantry.dto.request.RawPantryItemResponse;

import java.util.List;

public record RawPantryExtractionResponse(
        List<RawPantryItemResponse> items
) {
}
