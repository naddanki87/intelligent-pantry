package com.colruytgroup.intelligentpantry.controller;

import com.colruytgroup.intelligentpantry.config.AzureStorageHealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AzureStorageHealthController {

    private final AzureStorageHealthService service;

    @GetMapping("/azure-storage/health")
    public ResponseEntity<String> health() {

        return ResponseEntity.ok(
                service.validateConnection());
    }
}
