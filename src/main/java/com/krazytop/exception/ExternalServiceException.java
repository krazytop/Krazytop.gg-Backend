package com.krazytop.exception;

import com.krazytop.http_responses.ApiErrorEnum;

public class ExternalServiceException extends CustomException {

    public ExternalServiceException(ApiErrorEnum errorEnum) {
        super(errorEnum);
    }

    public ExternalServiceException(ApiErrorEnum errorEnum, Throwable cause) {
        super(errorEnum, cause);
    }
}