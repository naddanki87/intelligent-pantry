package com.colruytgroup.intelligentpantry.service;

import com.colruytgroup.intelligentpantry.enums.Category;
import org.springframework.stereotype.Service;

@Service
public class PantryItemNormalizerService {

    public Category determineCategory(String itemName) {

        String item = itemName.toLowerCase();

        return switch (item) {

            case "milk",
                 "butter",
                 "cheese",
                 "yogurt" -> Category.DAIRY;

            case "apple",
                 "apples",
                 "Apples",
                 "banana",
                 "bananas",
                 "orange",
                 "oranges",
                 "grapes" -> Category.FRUIT;

            case "carrot",
                 "potato",
                 "onion",
                 "tomato" -> Category.VEGETABLE;

            case "bread",
                 "bun",
                 "cake" -> Category.BAKERY;

            case "egg",
                 "eggs" -> Category.OTHER;

            default -> Category.OTHER;
        };
    }

    public String determineUnit(
            String itemName,
            String extractedUnit) {

        if (extractedUnit != null &&
                !extractedUnit.isBlank()) {

            return capitalize(extractedUnit);
        }

        String item = itemName.toLowerCase();

        return switch (item) {

            case "egg",
                 "eggs" -> "Pieces";

            case "apple",
                 "banana",
                 "orange" -> "Pieces";

            case "milk" -> "Packets";

            default -> "Units";
        };
    }
    private String capitalize(String value) {

        return value.substring(0,1).toUpperCase()
                + value.substring(1).toLowerCase();
    }
}
