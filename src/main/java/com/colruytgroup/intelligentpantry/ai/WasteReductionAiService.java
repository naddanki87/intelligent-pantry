package com.colruytgroup.intelligentpantry.ai;

import com.colruytgroup.intelligentpantry.dto.response.WasteReductionResponse;
import com.colruytgroup.intelligentpantry.entity.PantryItem;

import java.util.List;

public interface WasteReductionAiService {

    WasteReductionResponse generateSuggestions();

    List<PantryItem> getAllPantryItems();
}
