package com.krazytop.exception;

import com.krazytop.api_gateway.model.generated.ApiError;
import com.krazytop.api_gateway.model.generated.ApiErrorDetailsInner;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.krazytop.exception.CustomApiError.buildApiError;
import static com.krazytop.exception.ApiErrorEnum.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNoController() {
        return new ResponseEntity<>(buildApiError(NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleNoController(HttpRequestMethodNotSupportedException ex) {
        LOGGER.warn(ex.getMessage());
        return new ResponseEntity<>(buildApiError(METHOD_NOT_ALLOWED), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiError> handleCustomException(CustomException ex) {
        ApiError apiError = buildApiError(ex.getErrorEnum());
        LOGGER.error(apiError.getMessage());
        if (ex.getCause() != null) LOGGER.error(ex.getCause().toString());
        return new ResponseEntity<>(apiError, ex.getErrorEnum().getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        LOGGER.error("Validation failed: {}", ex.getMessage(), ex);
        ApiError apiError = buildApiError(VALIDATION_FAILED);
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            ApiErrorDetailsInner detail = new ApiErrorDetailsInner();
            detail.setField(error.getField());
            detail.setIssue(error.getDefaultMessage());
            apiError.getDetails().add(detail);
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
            apiError.getDetails().add(detail);
        });
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllUncaughtException(Exception ex) {
        LOGGER.error("An unexpected server error occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(buildApiError(UNEXPECTED_RUNTIME_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
