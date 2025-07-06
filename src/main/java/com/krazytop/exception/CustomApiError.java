package com.krazytop.exception;

import com.krazytop.api_gateway.model.generated.ApiError;

public class CustomApiError extends ApiError {

    public static ApiError buildApiError(ApiErrorEnum errorEnum) {
        ApiError apiError = new ApiError();
        apiError.setCode(errorEnum.getCode());
        apiError.setMessage(errorEnum.getMessage());
        return apiError;
    }

}
