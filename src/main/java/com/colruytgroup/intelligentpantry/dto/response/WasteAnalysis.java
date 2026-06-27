package com.colruytgroup.intelligentpantry.dto.response;

import com.colruytgroup.intelligentpantry.enums.WasteRisk;

public record WasteAnalysis(

        String itemName,
        Long quantity,
        long daysRemaining,
        WasteRisk risk,
        Integer wasteScore
) {
}
