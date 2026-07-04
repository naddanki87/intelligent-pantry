package com.colruytgroup.intelligentpantry.controller;

import com.colruytgroup.intelligentpantry.dto.request.UserPreferenceRequest;
import com.colruytgroup.intelligentpantry.entity.UserPreference;
import com.colruytgroup.intelligentpantry.service.UserPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-preferences")
@RequiredArgsConstructor
public class UserPreferenceController {

    private final UserPreferenceService service;

    @PostMapping
    public ResponseEntity<UserPreference> save(
            @RequestBody UserPreferenceRequest request) {

        return ResponseEntity.ok(
                service.save(request));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserPreference> get(
            @PathVariable String username) {

        return ResponseEntity.ok(
                service.get(username));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> delete(
            @PathVariable String username) {

        service.delete(username);

        return ResponseEntity.noContent().build();
    }
}
