package com.colruytgroup.intelligentpantry.service.impl;

import com.colruytgroup.intelligentpantry.dto.PantryItemRequest;
import com.colruytgroup.intelligentpantry.dto.PantryItemResponse;
import com.colruytgroup.intelligentpantry.entity.PantryItem;
import com.colruytgroup.intelligentpantry.repository.PantryItemRepository;
import com.colruytgroup.intelligentpantry.service.PantryItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PantryItemServiceImpl   implements PantryItemService {

    private final PantryItemRepository repository;

    @Override
    public PantryItemResponse create(
            PantryItemRequest request) {

        PantryItem item =
                PantryItem.builder()
                        .itemName(request.itemName())
                        .quantity(request.quantity())
                        .unit(request.unit())
                        .purchaseDate(request.purchaseDate())
                        .expiryDate(request.expiryDate())
                        .category(request.category())
                        .build();

        item = repository.save(item);

        return map(item);
    }

    @Override
    public List<PantryItemResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    private PantryItemResponse map(
            PantryItem item) {

        return new PantryItemResponse(
                item.getId(),
                item.getItemName(),
                item.getQuantity(),
                item.getUnit(),
                item.getPurchaseDate(),
                item.getExpiryDate(),
                item.getCategory());
    }
}