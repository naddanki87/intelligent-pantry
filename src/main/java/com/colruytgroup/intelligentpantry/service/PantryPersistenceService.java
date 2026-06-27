package com.colruytgroup.intelligentpantry.service;

import com.colruytgroup.intelligentpantry.dto.response.PantryExtractionResponse;

public interface PantryPersistenceService {

    void saveInventory(
            PantryExtractionResponse response);
}
