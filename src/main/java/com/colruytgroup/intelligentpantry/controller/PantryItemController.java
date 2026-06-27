package com.colruytgroup.intelligentpantry.controller;

import com.colruytgroup.intelligentpantry.dto.PantryItemRequest;
import com.colruytgroup.intelligentpantry.dto.PantryItemResponse;
import com.colruytgroup.intelligentpantry.service.PantryItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/pantry")
@RequiredArgsConstructor
public class PantryItemController {

    private final PantryItemService service;

    @PostMapping
    public PantryItemResponse create(
            @Valid
            @RequestBody PantryItemRequest request) {

        return service.create(request);
    }

    @GetMapping
    public List<PantryItemResponse> getAll() {
        return service.getAll();
    }
}
