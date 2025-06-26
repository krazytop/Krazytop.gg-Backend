package com.krazytop.http_responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApiErrorEnum {

    // Config
    CONSTRAINT_VIOLATION("CONSTRAINT_VIOLATION", "One or more constraints were violated.", HttpStatus.BAD_REQUEST),
    VALIDATION_FAILED("VALIDATION_FAILED", "Validation failed for one or more fields.", HttpStatus.BAD_REQUEST),
    METHOD_NOT_ALLOWED("METHOD_NOT_ALLOWED", "Method is not supported", HttpStatus.METHOD_NOT_ALLOWED),
    UNEXPECTED_RUNTIME_ERROR("UNEXPECTED_RUNTIME_ERROR", "An unexpected error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_FOUND("NOT_FOUND", "The requested resource was not found.", HttpStatus.NOT_FOUND),


    // Common
    METADATA_NOT_FOUND("METADATA_NOT_FOUND", "Game metadata were not found", HttpStatus.NOT_FOUND),
    PATCH_NOT_FOUND("PATCH_NOT_FOUND", "Game patch was not found", HttpStatus.NOT_FOUND),
    PATCH_UPDATE_ERROR("PATCH_UPDATE_ERROR", "An unexpected error occurred while updating patches", HttpStatus.INTERNAL_SERVER_ERROR),
    // Destiny
    BUNGIE_AUTH_ERROR("BUNGIE_AUTH_ERROR", "An unexpected error occurred while authenticating to Bungie. Please try again later or log out before trying again.", HttpStatus.INTERNAL_SERVER_ERROR),
    // League of Legends
    // Teamfight Tactics
    // Clash Royal
    ;
    private final String code;
    private final String message;
    private final HttpStatus status;

}