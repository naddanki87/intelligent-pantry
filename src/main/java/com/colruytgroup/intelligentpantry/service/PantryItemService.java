package com.colruytgroup.intelligentpantry.service;

import com.colruytgroup.intelligentpantry.dto.PantryItemRequest;
import com.colruytgroup.intelligentpantry.dto.PantryItemResponse;

import java.util.List;

public interface PantryItemService {

    PantryItemResponse create(
            String username,
            PantryItemRequest request);

    List<PantryItemResponse> getAll(
            String username);
}
