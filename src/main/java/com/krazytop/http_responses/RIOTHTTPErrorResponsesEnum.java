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
    MASTERIES_NOT_FOUND(8, "RIOT Masteries not found", HttpStatus.NOT_FOUND),
    MASTERIES_CANT_BE_UPDATED(9, "RIOT Masteries can't be updated", HttpStatus.NOT_FOUND),
    MATCHES_CANT_BE_UPDATED(10, "RIOT matches can't be updated", HttpStatus.NOT_FOUND);

    private final int errorCode;
    private final String message;
    @JsonIgnore private final HttpStatus httpResponseCode;
}
