package com.colruytgroup.intelligentpantry.ai;

import com.colruytgroup.intelligentpantry.dto.response.PantryItemAiResponse;
import com.colruytgroup.intelligentpantry.dto.response.RawPantryExtractionResponse;
import com.colruytgroup.intelligentpantry.service.PantryItemNormalizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import com.colruytgroup.intelligentpantry.dto.response.PantryExtractionResponse;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
                                            item.itemName()),
                                    item.expiryDate()
                            ))
                            .toList();


                 return   new PantryExtractionResponse(
                            normalizedItems);

        }
    private String capitalize(String value) {

        return value.substring(0,1).toUpperCase()
                + value.substring(1).toLowerCase();
    }

    @Override
    public PantryExtractionResponse extractInventoryFromImage(MultipartFile file) throws IOException {
        ByteArrayResource imageResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        Media imageMedia = Media.builder()
                .mimeType(MimeTypeUtils.parseMimeType(file.getContentType()))
                .data(imageResource)
                .build();

        String prompt = """
                
                 You are a data extraction specialist. Your task is to analyze the uploaded image and extract product information according to these rules:
                
                                Extraction Rules:
                
                                Expiry Date Selection Logic:
                
                                 1. Find all manufacturing, packing, best-before, use-by, and expiry dates.
                                 2. If two or more dates are found:
                                       - Treat the second date as the expiry date.
                                  3. If only one date is found:
                                         - Treat that date as the expiry date.
                                  4.If a "Use By Date", "Best Before", or "Expiry Date" label exists:
                
                                      - Locate the label.
                                      - Search the surrounding area (same row, right side, below, or nearby printed values) for the associated date.
                                      - The date does not need to appear on the same line as the label.
                                      - Match labels to nearby values even when printed in separate columns.
                                      - Dot-matrix printed dates are valid dates and should be extracted.
                                     * When both Packed Date and Use By Date are present:
                                        - Packed Date = manufacturing/packing date
                                        - Use By Date = expiry date
                                        - Always choose Use By Date as expiryDate.
                                  5. Dates visible in the image may appear in formats such as dd/MM/yyyy or dd/MM/yy.
                                         Parse those dates correctly and always convert the final expiry date to ISO format yyyy-MM-dd in the JSON response.
                                            Examples:
                                            - 07/07/2026 must become 2026-07-07
                                            - 07/07/26 must become 2026-07-07
                                  6. If no valid date is found, use tomorrow's date, 2026-07-02, in yyyy-MM-dd format.
                                
                                Unit Determination Rules:
                
                                1. Determine the unit based on the product type.
                
                                2. Eggs:
                                   - Single egg -> quantity = 1, unit = "piece"
                                   - Multiple eggs -> quantity = total count, unit = "pieces"
                
                                3. Packaged food products (bread, biscuits, chips, snacks, flour, rice, sugar, etc.):
                                   - Single package -> quantity = 1, unit = "packet"
                                   - Multiple packages -> quantity = total count, unit = "packets"
                
                                4. Bottled products (water, milk, juice, oil, soft drinks):
                                   - Single bottle -> quantity = 1, unit = "bottle"
                                   - Multiple bottles -> quantity = total count, unit = "bottles"
                
                                5. Canned products:
                                   - Single can -> quantity = 1, unit = "can"
                                   - Multiple cans -> quantity = total count, unit = "cans"
                
                                6. Boxed products:
                                   - Single box -> quantity = 1, unit = "box"
                                   - Multiple boxes -> quantity = total count, unit = "boxes"
                
                                7. Fruits and vegetables:
                                   - Countable items -> unit = "pieces"
                                   - Loose produce sold by weight -> unit = "kg" only if weight is clearly visible.
                
                                8. Never use weight units (g, kg, mg, ml, l) as the unit unless the product is being sold solely by weight and no package count can be determined.
                
                                Product Name: Extract the exact product name from the packaging.
                
                                Quantity & Count: If multiple items of the same product are present in the image, count them and provide the total count. If only one item is shown, the count is 1.
                
                                Other Details: Also extract price/MRP if visible on the packaging.
                
                                Output Format:
                
                                Return the extracted data as a JSON object with the following structure:
                                                          {
                                                            "items": [
                                                              {
                                                                "itemName": "",
                                                                "quantity":0,
                                                                "expiryDate":"",
                                                                 "unit":""
                                                              }
                                                            ]
                                                          }
                
                                If multiple identical products appear in the image, combine them into a single entry with an updated unit field showing the aggregate count.
                                if the expiry date is not found then consider the 1 day from current date as expiry date update with expiry_date field.
                                Analyze the image(s) I've uploaded and provide the extracted information in the JSON format specified above.
                                 Important:
                                        - Always include "expiryDate" for every item.
                                        - Always return "items" as an array.
                                        - If the image contains multiple products, return multiple objects inside "items".
                                        - Use camelCase field names exactly: itemName, quantity, unit, expiryDate.
                               
                """;

        RawPantryExtractionResponse rawResponse =
                chatClient.prompt()
                        .user(user -> user
                                .text(prompt)
                                .media(imageMedia)
                        )
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
                                        item.itemName()),
                                item.expiryDate()
                        ))
                        .toList();

        return new PantryExtractionResponse(
                normalizedItems);
    }

}
