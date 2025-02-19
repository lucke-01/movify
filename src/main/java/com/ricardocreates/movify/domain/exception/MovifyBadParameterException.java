package com.ricardocreates.movify.domain.exception;


import com.ricardocreates.movify.domain.generic.ErrorResponse;

import java.util.Map;

public class MovifyBadParameterException extends MovifyBaseException {
    private static final int DEFAULT_HTTP_CODE = 400;

    public MovifyBadParameterException(String message) {
        super(message, DEFAULT_HTTP_CODE);
    }

    public MovifyBadParameterException(String message, Map<String, ErrorResponse> errors) {
        super(message, DEFAULT_HTTP_CODE, errors);
    }
}