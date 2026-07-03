package com.colruytgroup.intelligentpantry.controller;

import com.colruytgroup.intelligentpantry.ai.CookingCopilotService;
import com.colruytgroup.intelligentpantry.dto.request.CookingCopilotRequest;
import com.colruytgroup.intelligentpantry.dto.response.CookingCopilotResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class CookingCopilotController {

    private final CookingCopilotService service;

    @PostMapping("/cooking-copilot")
    public CookingCopilotResponse ask(
            @RequestBody
            @Valid
            CookingCopilotRequest request) {

        return service.ask(request);
    }
}
