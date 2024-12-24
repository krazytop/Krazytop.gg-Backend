package com.krazytop.entity;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class HTTPResponse {

    private String message;
    private Integer statusCode;

    public HTTPResponse(String message, HttpStatus status) {
        this.message = message;
        this.statusCode = status.value();
    }
}
