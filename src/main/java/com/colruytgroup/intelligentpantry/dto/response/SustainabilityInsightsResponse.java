package com.colruytgroup.intelligentpantry.dto.response;

import java.util.List;

public record SustainabilityInsightsResponse(

        String summary,

        List<String> risks,

        List<String> recommendations,

        List<String> immediateActions
) {
}
