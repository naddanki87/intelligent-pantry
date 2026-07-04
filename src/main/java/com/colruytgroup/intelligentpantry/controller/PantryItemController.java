package com.colruytgroup.intelligentpantry.controller;

import com.colruytgroup.intelligentpantry.dto.PantryItemRequest;
import com.colruytgroup.intelligentpantry.dto.PantryItemResponse;
import com.colruytgroup.intelligentpantry.enums.Category;
import com.colruytgroup.intelligentpantry.service.PantryItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pantry")
@RequiredArgsConstructor
public class PantryItemController {

    private final PantryItemService service;

    @PostMapping
    public PantryItemResponse create(
            @RequestHeader(value = "X-user_id", required = false) String username,
            @Valid
            @RequestBody PantryItemRequest request) {

        if (username == null || username.isBlank()) {
            return new PantryItemResponse(null, "Milk", 1.0, "1",  LocalDate.now(), LocalDate.now().plusDays(1),
                    Category.BAKERY);
        }

        return service.create(username, request);
    }

    @GetMapping
    public List<PantryItemResponse> getAll(
            @RequestHeader(value = "X-user_id", required = false) String username) {

        if (username == null || username.isBlank()) {
            return List.of(
                    new PantryItemResponse(null, "Milk", 1.0, "1",  LocalDate.now(), LocalDate.now().plusDays(1),
                            Category.BAKERY),
                    new PantryItemResponse(null, "Egg", 2.0, "1",  LocalDate.now(), LocalDate.now().plusDays(1),
                            Category.OTHER)
            );
        }

        return service.getAll(username);
    }
}
