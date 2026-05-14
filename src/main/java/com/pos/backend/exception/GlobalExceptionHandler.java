package com.pos.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice  // Makes this class handle exceptions for ALL controllers
public class GlobalExceptionHandler {

    // ┌─────────────────────────────────────────────────────────┐
    // │  Handle Resource Not Found (404)                        │
    // │  Example: "No account found with this email"            │
    // └─────────────────────────────────────────────────────────┘
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(
            ResourceNotFoundException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)     // 404 status code
                .body(error);
    }

    // ┌─────────────────────────────────────────────────────────┐
    // │  Handle Runtime Exceptions (400)                        │
    // │  Example: "Email already registered"                    │
    // │  Example: "Invalid password"                            │
    // │  Example: "Account is deactivated"                      │
    // └─────────────────────────────────────────────────────────┘
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(
            RuntimeException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)   // 400 status code
                .body(error);
    }

    // ┌─────────────────────────────────────────────────────────┐
    // │  Handle Validation Errors (400)                         │
    // │  Triggered by @Valid + @NotBlank, @Email, @Size, etc.   │
    // │  Example: "Password must be at least 6 characters"      │
    // │  Example: "Invalid email format"                        │
    // │  Example: "Full name is required"                       │
    // └─────────────────────────────────────────────────────────┘
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)   // 400 status code
                .body(errors);
    }
}