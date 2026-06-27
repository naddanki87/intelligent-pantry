package com.colruytgroup.intelligentpantry.dto.response;

import java.util.List;

public record WasteDashboardResponse(

        int totalItems,

        int highRiskItems,

        int mediumRiskItems,

        int lowRiskItems,

        List<WasteAnalysis> items
) {
}
