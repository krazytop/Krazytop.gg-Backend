package com.krazytop.http_responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RIOTHTTPErrorResponsesEnum {
    MATCHES_CANT_BE_UPDATED(10, "RIOT matches can't be updated", HttpStatus.NOT_FOUND);

    private final int errorCode;
    private final String message;
    @JsonIgnore private final HttpStatus httpResponseCode;
}
