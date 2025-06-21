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
    BOARD_NOT_FOUND(3, "RIOT Board not found", HttpStatus.NOT_FOUND),
    SUMMONER_NOT_FOUND(4, "RIOT Summoner not found", HttpStatus.NOT_FOUND),
    ACCOUNT_NOT_FOUND(5, "RIOT Account not found", HttpStatus.NOT_FOUND),
    RANKS_NOT_FOUND(6, "RIOT Ranks not found", HttpStatus.NOT_FOUND),
    RANKS_CANT_BE_UPDATED(7, "RIOT Ranks can't be updated", HttpStatus.NOT_FOUND),
    MASTERIES_NOT_FOUND(8, "RIOT Masteries not found", HttpStatus.NOT_FOUND),
    MASTERIES_CANT_BE_UPDATED(9, "RIOT Masteries can't be updated", HttpStatus.NOT_FOUND),
    MATCHES_CANT_BE_UPDATED(10, "RIOT matches can't be updated", HttpStatus.NOT_FOUND),
    SUMMONER_ALREADY_ADDED_TO_BOARD(11, "RIOT Summoner already added to board", HttpStatus.BAD_REQUEST),
    SUMMONER_ABSENT_OF_BOARD(11, "RIOT Summoner isn't present on board", HttpStatus.BAD_REQUEST),;

    private final int errorCode;
    private final String message;
    @JsonIgnore private final HttpStatus httpResponseCode;
}
