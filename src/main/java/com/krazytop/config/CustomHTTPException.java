package com.krazytop.config;

import com.krazytop.http_responses.RIOTHTTPErrorResponsesEnum;
import lombok.Getter;

@Getter
public class CustomHTTPException extends RuntimeException {

    private final RIOTHTTPErrorResponsesEnum response;

    public CustomHTTPException(RIOTHTTPErrorResponsesEnum response) {
        this.response = response;
    }

}
