package com.krazytop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HTTPResponse {

    private String message;
    private Integer statusCode;
}
