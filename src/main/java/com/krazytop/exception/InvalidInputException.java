package com.krazytop.exception;

import com.krazytop.http_responses.ApiErrorEnum;

public class InvalidInputException extends CustomException {

    public InvalidInputException(ApiErrorEnum errorEnum) {
        super(errorEnum);
    }

    public InvalidInputException(ApiErrorEnum errorEnum, Throwable cause) {
        super(errorEnum, cause);
    }
}