package com.colruytgroup.intelligentpantry.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle(
            MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        Map.of(
                                "timestamp",
                                LocalDateTime.now(),
                                "message",
                                "Validation failed",
                                "errors",
                                fieldErrors));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handle(
            ResponseStatusException ex) {

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(
                        Map.of(
                                "timestamp",
                                LocalDateTime.now(),
                                "message",
                                ex.getReason() != null ? ex.getReason() : ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle(
            Exception ex) {

        return ResponseEntity
                .internalServerError()
                .body(
                        Map.of(
                                "timestamp",
                                LocalDateTime.now(),
                                "message",
                                ex.getMessage()));
    }

}
