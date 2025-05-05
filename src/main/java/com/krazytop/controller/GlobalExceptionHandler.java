package com.krazytop.controller;

import com.krazytop.config.CustomHTTPException;
import com.krazytop.http_responses.HTTPResponse;
import com.krazytop.http_responses.RIOTHTTPErrorResponsesEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomHTTPException.class)
    public ResponseEntity<RIOTHTTPErrorResponsesEnum> handleCustomHTTPException(CustomHTTPException e) {
        RIOTHTTPErrorResponsesEnum response = e.getResponse();
        LOGGER.error(response.getMessage());
        return new ResponseEntity<>(response, response.getHttpResponseCode());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<HTTPResponse> handleNoController() {
        return new ResponseEntity<>(new HTTPResponse("The requested page or resource does not exist.", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HTTPResponse> handleException(Exception e) {
        LOGGER.error("An error occurred : {}", e.getMessage());
        return new ResponseEntity<>(new HTTPResponse("An internal server error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
