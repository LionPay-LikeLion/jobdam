package com.jobdam.common.exception;

import io.swagger.v3.oas.annotations.Hidden;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LimitExceededException.class)
    public ResponseEntity<Map<String, String>> handleLimitExceeded(LimitExceededException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
    }

}