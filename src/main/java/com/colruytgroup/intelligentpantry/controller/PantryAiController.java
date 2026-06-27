package com.colruytgroup.intelligentpantry.controller;

import com.colruytgroup.intelligentpantry.dto.request.PantryExtractionRequest;
import com.colruytgroup.intelligentpantry.dto.response.PantryExtractionResponse;
import com.colruytgroup.intelligentpantry.service.PantryPersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.colruytgroup.intelligentpantry.ai.PantryAiService;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class PantryAiController {

    private final PantryAiService pantryAiService;
    private final PantryPersistenceService persistenceService;

    @PostMapping("/extract")
    public PantryExtractionResponse extract(
            @RequestBody PantryExtractionRequest request) {

        return pantryAiService.extractInventory(
                request.text());
    }

    @PostMapping("/extract-and-save")
    public PantryExtractionResponse extractAndSave(
            @RequestBody PantryExtractionRequest request) {

        PantryExtractionResponse response =
                pantryAiService.extractInventory(
                        request.text());

        persistenceService.saveInventory(response);

        return response;
    }
}
