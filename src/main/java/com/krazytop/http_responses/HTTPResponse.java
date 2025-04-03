package com.krazytop.http_responses;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class HTTPResponse {//TODO supprimer au profit des enums HTTPResponses

    private String message;
    private Integer statusCode;

    public HTTPResponse(String message, HttpStatus status) {
        this.message = message;
        this.statusCode = status.value();
    }
}
