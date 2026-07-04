package com.colruytgroup.intelligentpantry.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.colruytgroup.intelligentpantry.dto.PreferenceRequest;
import com.colruytgroup.intelligentpantry.dto.PreferenceResponse;
import com.colruytgroup.intelligentpantry.service.PreferenceService;

import lombok.RequiredArgsConstructor;

/**
 * Preferences CRUD. Read/update/delete accept any combination of {@code userId}
 * and {@code id} as query parameters: pass both for a precise point operation,
 * {@code id} alone to locate a document across partitions, or {@code userId}
 * alone to act on that user's preferences.
 */
@RestController
@RequestMapping("/api/preferences")
@RequiredArgsConstructor
public class PreferenceController {

    private final PreferenceService service;

    @PostMapping
    public PreferenceResponse create(@RequestBody PreferenceRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<PreferenceResponse> find(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String id) {
        return service.find(userId, id);
    }

    @PutMapping
    public PreferenceResponse update(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String id,
            @RequestBody PreferenceRequest request) {
        return service.update(userId, id, request);
    }

    @DeleteMapping
    public void delete(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String id) {
        service.delete(userId, id);
    }
}
