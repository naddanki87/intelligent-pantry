package com.colruytgroup.intelligentpantry.dto.request;

public record UserPreferenceRequest(
        String username,
        String preferences
) {
}
