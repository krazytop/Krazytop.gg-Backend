package com.krazytop.http_responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiErrorEnum {

    // Generic 4xx errors
    CONSTRAINT_VIOLATION("CONSTRAINT_VIOLATION", "One or more constraints were violated."),
    VALIDATION_FAILED("VALIDATION_FAILED", "Validation failed for one or more fields."),
    NOT_FOUND("NOT_FOUND", "The requested resource was not found."),
    // Generic 5xx errors
    UNEXPECTED_RUNTIME_ERROR("UNEXPECTED_RUNTIME_ERROR", "An unexpected error occurred. Please try again later."),

    // Destiny
    BUNGIE_AUTH_ERROR("BUNGIE_AUTH_ERROR", "An unexpected error occurred while authenticating to Bungie. Please try again later or log out before trying again.");


    private final String code;
    private final String message;

}