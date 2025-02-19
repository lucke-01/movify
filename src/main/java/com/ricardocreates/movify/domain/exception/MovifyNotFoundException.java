package com.ricardocreates.movify.domain.exception;


import com.ricardocreates.movify.domain.generic.ErrorResponse;

import java.util.Map;

public class MovifyNotFoundException extends MovifyBaseException {
    private static final int DEFAULT_HTTP_CODE = 404;

    public MovifyNotFoundException(String message) {
        super(message, DEFAULT_HTTP_CODE);
    }

    public MovifyNotFoundException(String message, Map<String, ErrorResponse> errors) {
        super(message, DEFAULT_HTTP_CODE, errors);
    }
}