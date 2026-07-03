package com.colruytgroup.intelligentpantry.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DietPlannerRequest(

        @NotBlank
        String dietText
) {
}
