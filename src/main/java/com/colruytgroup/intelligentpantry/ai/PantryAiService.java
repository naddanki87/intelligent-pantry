package com.colruytgroup.intelligentpantry.ai;

import com.colruytgroup.intelligentpantry.dto.response.PantryExtractionResponse;

public interface PantryAiService {

    PantryExtractionResponse extractInventory(
            String text);
}
