package com.colruytgroup.intelligentpantry.dto.response;

import java.util.List;

public record PantryExtractionResponse(

        List<PantryItemAiResponse> items
) {
}
