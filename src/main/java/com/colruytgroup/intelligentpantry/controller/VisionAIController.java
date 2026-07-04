package com.colruytgroup.intelligentpantry.controller;

import com.colruytgroup.intelligentpantry.ai.PantryAiService;
import com.colruytgroup.intelligentpantry.ai.VisionAIService;
import com.colruytgroup.intelligentpantry.dto.response.PantryExtractionResponse;
import com.colruytgroup.intelligentpantry.service.PantryPersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/ai/vision")
@RequiredArgsConstructor
public class VisionAIController {

    private final VisionAIService visionAiService;
    private final PantryAiService pantryAiService;
    private final PantryPersistenceService persistenceService;

    @PostMapping(
            value = "/ask-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String askImage(
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        String question = """
                You are a data extraction specialist. Your task is to analyze the uploaded image and extract product information according to these rules:
                
                Extraction Rules:
                
                Expiry Date Selection Logic: 
                
               1. Find all manufacturing, packing, best-before, use-by, and expiry dates.
                2. If two or more dates are found:
                   - Treat the second date as the expiry date.
                3. If only one date is found:
                   - Treat that date as the expiry date.
                4. If a "USE BY DATE" label exists, always prioritize the date associated with that label.
                5. Convert the result to DD/MM/YYYY format.
                6. If no valid date is found, use tomorrow's date.
                
                Product Name: Extract the exact product name from the packaging.
                
                Quantity & Count: If multiple items of the same product are present in the image, count them and provide the total count. If only one item is shown, the count is 1.
                
                Other Details: Also extract price/MRP if visible on the packaging.
                
                Output Format:
                
                Return the extracted data as a JSON object with the following structure:
                 Format:
                                          {
                                            "items": [
                                              {
                                                "itemName": "",
                                                "expiry_date":"",
                                                "unit": 0,
                                              }
                                            ]
                                          }
                
                If multiple identical products appear in the image, combine them into a single entry with an updated unit field showing the aggregate count.
                if the expiry date is not found then consider the 1 day from current date as expiry date update with expiry_date field.
                Analyze the image(s) I've uploaded and provide the extracted information in the JSON format specified above.
                """;
        return visionAiService.askImage(file, question);
    }

    @PostMapping(
            value = "/extract-save",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public PantryExtractionResponse extractSave( @RequestPart("file") MultipartFile file) throws IOException {
        PantryExtractionResponse pantryExtractionResponse = pantryAiService.extractInventoryFromImage(file);
        persistenceService.saveInventory(pantryExtractionResponse);
        return pantryExtractionResponse;
    }

    @PostMapping(
            value = "/v2/extract-save",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public PantryExtractionResponse extractSaveV2(
            @RequestHeader("X-user_id") String username,
            @RequestPart("file") MultipartFile file) throws IOException {
        PantryExtractionResponse pantryExtractionResponse = pantryAiService.extractInventoryFromImage(file);
        persistenceService.saveInventory(username, pantryExtractionResponse);
        return pantryExtractionResponse;
    }


}
