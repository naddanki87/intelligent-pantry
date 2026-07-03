package com.colruytgroup.intelligentpantry.ai;

import com.colruytgroup.intelligentpantry.dto.request.DietPlannerRequest;
import com.colruytgroup.intelligentpantry.dto.response.DietPlannerResponse;

public interface DietPlannerAiService {

    DietPlannerResponse generatePlan(DietPlannerRequest request);
}
