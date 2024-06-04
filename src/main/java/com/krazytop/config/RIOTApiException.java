package com.krazytop.config;

public class RIOTApiException extends RuntimeException {

    private static final String ERROR_MESSAGE = "An error occurred while calling the RIOT API : ";

    public RIOTApiException(String apiUrl, Throwable cause) {
        super(ERROR_MESSAGE + apiUrl, cause);
    }

}
