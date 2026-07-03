package com.colruytgroup.intelligentpantry.ai;

import com.colruytgroup.intelligentpantry.dto.request.CookingCopilotRequest;
import com.colruytgroup.intelligentpantry.dto.response.CookingCopilotResponse;

public interface CookingCopilotService {

    CookingCopilotResponse ask(
            CookingCopilotRequest request);
}
