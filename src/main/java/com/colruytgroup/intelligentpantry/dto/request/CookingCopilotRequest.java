package com.colruytgroup.intelligentpantry.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CookingCopilotRequest(
        @NotBlank
        String recipeName,

        @NotBlank
        String question
) {
}
