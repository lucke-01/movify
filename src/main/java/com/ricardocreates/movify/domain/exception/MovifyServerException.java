package com.ricardocreates.movify.domain.exception;


import com.ricardocreates.movify.domain.generic.ErrorResponse;

import java.util.Map;

public class MovifyServerException extends MovifyBaseException {
    private static final int DEFAULT_HTTP_CODE = 500;

    public MovifyServerException(String message) {
        super(message, DEFAULT_HTTP_CODE);
    }

    public MovifyServerException(String message, Map<String, ErrorResponse> errors) {
        super(message, DEFAULT_HTTP_CODE, errors);
    }
}
