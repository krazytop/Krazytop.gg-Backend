package com.krazytop.controller;

import com.krazytop.api_gateway.model.generated.ApiError;
import com.krazytop.api_gateway.model.generated.ApiErrorDetailsInner;
import com.krazytop.config.CustomHTTPException;
import com.krazytop.exception.ExternalServiceException;
import com.krazytop.exception.InternalServiceException;
import com.krazytop.exception.InvalidInputException;
import com.krazytop.http_responses.RIOTHTTPErrorResponsesEnum;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static com.krazytop.exception.CustomApiError.buildApiError;
import static com.krazytop.http_responses.ApiErrorEnum.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomHTTPException.class)//TODO supprimer
    public ResponseEntity<RIOTHTTPErrorResponsesEnum> handleCustomHTTPException(CustomHTTPException e) {
        RIOTHTTPErrorResponsesEnum response = e.getResponse();
        LOGGER.error(response.getMessage());
        return new ResponseEntity<>(response, response.getHttpResponseCode());
    }

    /* TODO voir si il faut modifier
    spring:
  mvc:
    throw-exception-if-no-handler-found: true
  web:
    resources:
      add-mappings: false # Important pour éviter que Spring ne serve des ressources statiques et masque l'exception
      => NoHandlerFoundException.class ?
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNoController(NoResourceFoundException ex) {
        LOGGER.error(ex.getMessage(), ex);
        return new ResponseEntity<>(buildApiError(NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ApiError> handleInvalidInputException(InvalidInputException ex) {
        LOGGER.error("Invalid input error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(buildApiError(ex.getErrorEnum()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ApiError> handleExternalServiceException(ExternalServiceException ex) {
        LOGGER.error("External service error occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(buildApiError(ex.getErrorEnum()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InternalServiceException.class)
    public ResponseEntity<ApiError> handleInternalServiceException(InternalServiceException ex) {
        LOGGER.error("Internal service error occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(buildApiError(ex.getErrorEnum()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        LOGGER.error("Validation failed: {}", ex.getMessage(), ex);
        ApiError apiError = buildApiError(VALIDATION_FAILED);
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            ApiErrorDetailsInner detail = new ApiErrorDetailsInner();
            detail.setField(error.getField());
            detail.setIssue(error.getDefaultMessage());
            apiError.addDetailsItem(detail);
        });
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException ex) {
        LOGGER.error("Constraint violation: {}", ex.getMessage(), ex);
        ApiError apiError = buildApiError(CONSTRAINT_VIOLATION);
        ex.getConstraintViolations().forEach(violation -> {
            ApiErrorDetailsInner detail = new ApiErrorDetailsInner();
            String fieldName = violation.getPropertyPath() != null ? violation.getPropertyPath().toString() : "unknown";
            detail.setField(fieldName);
            detail.setIssue(violation.getMessage());
            apiError.addDetailsItem(detail);
        });
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllUncaughtException(Exception ex) {
        LOGGER.error("An unexpected server error occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(buildApiError(UNEXPECTED_RUNTIME_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
