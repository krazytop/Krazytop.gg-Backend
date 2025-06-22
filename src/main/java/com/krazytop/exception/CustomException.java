package com.krazytop.exception;

import com.krazytop.http_responses.ApiErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ApiErrorEnum errorEnum;

    public CustomException(ApiErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.errorEnum = errorEnum;
    }

    public CustomException(ApiErrorEnum errorEnum,  Throwable cause) {
        super(errorEnum.getMessage(), cause);
        this.errorEnum = errorEnum;
    }
}