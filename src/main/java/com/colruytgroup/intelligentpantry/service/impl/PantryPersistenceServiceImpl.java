package com.colruytgroup.intelligentpantry.service.impl;

import com.colruytgroup.intelligentpantry.dto.response.PantryExtractionResponse;
import com.colruytgroup.intelligentpantry.dto.response.PantryItemAiResponse;
import com.colruytgroup.intelligentpantry.entity.PantryItem;
import com.colruytgroup.intelligentpantry.repository.PantryItemRepository;
import com.colruytgroup.intelligentpantry.service.PantryPersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PantryPersistenceServiceImpl  implements PantryPersistenceService {

    private final PantryItemRepository repository;

    @Override
    public void saveInventory(PantryExtractionResponse response) {

        for (PantryItemAiResponse item : response.items()) {

            Optional<PantryItem> existingItem =
                    repository.findByItemNameIgnoreCase(
                            item.itemName());

            if (existingItem.isPresent()) {

                PantryItem existing =
                        existingItem.get();

                existing.setQuantity(
                        existing.getQuantity()
                                + item.quantity());
                existing.setExpiryDate(item.expiryDate());

                repository.save(existing);

            } else {

                PantryItem entity =
                        PantryItem.builder()
                                .itemName(item.itemName())
                                .quantity(item.quantity())
                                .unit(item.unit())
                                .expiryDate(item.expiryDate())
                                .category(item.category())
                                .purchaseDate(java.time.LocalDate.now())
                                .build();

                repository.save(entity);
            }
        }

    }
}
