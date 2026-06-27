package com.colruytgroup.intelligentpantry.ai;

import com.colruytgroup.intelligentpantry.dto.response.PantryItemAiResponse;
import com.colruytgroup.intelligentpantry.dto.response.RawPantryExtractionResponse;
import com.colruytgroup.intelligentpantry.service.PantryItemNormalizerService;
import com.colruytgroup.intelligentpantry.service.PantryPersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import com.colruytgroup.intelligentpantry.dto.response.PantryExtractionResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PantryAiServiceImpl implements PantryAiService {

    private final ChatClient chatClient;
    private final PantryItemNormalizerService normalizer;


        @Override
        public PantryExtractionResponse extractInventory(
                String text) {

            String prompt = """
                Extract pantry items from the text.

                Return valid JSON only.
                
                Do not infer category.
                Do not invent information.
                
                Format:

                {
                  "items":[
                    {
                      "itemName":"",
                      "quantity":0,
                      "unit":""
                    }
                  ]
                }

                Text:
                %s
                """.formatted(text);

            RawPantryExtractionResponse rawResponse =
                    chatClient.prompt()
                            .user(prompt)
                            .call()
                            .entity(RawPantryExtractionResponse.class);

            List<PantryItemAiResponse> normalizedItems =
                    rawResponse.items()
                            .stream()
                            .map(item -> new PantryItemAiResponse(
                                    capitalize(item.itemName()),
                                    item.quantity(),
                                    normalizer.determineUnit(
                                            item.itemName(),
                                            item.unit()),
                                    normalizer.determineCategory(
                                            item.itemName())
                            ))
                            .toList();


                 return   new PantryExtractionResponse(
                            normalizedItems);

        }
    private String capitalize(String value) {

        return value.substring(0,1).toUpperCase()
                + value.substring(1).toLowerCase();
    }
}
