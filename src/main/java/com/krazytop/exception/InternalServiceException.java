package com.krazytop.exception;

import com.krazytop.http_responses.ApiErrorEnum;

public class InternalServiceException extends CustomException {

    public InternalServiceException(ApiErrorEnum errorEnum) {
        super(errorEnum);
    }

    public InternalServiceException(ApiErrorEnum errorEnum, Throwable cause) {
        super(errorEnum, cause);
    }
}