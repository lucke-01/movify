package com.ricardocreates.movify.domain.exception;

import com.ricardocreates.movify.domain.generic.ErrorResponse;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class MovifyBaseException extends RuntimeException {
    protected int httpCode;
    protected Map<String, ErrorResponse> errors;

    public MovifyBaseException(String message) {
        this(message, 500);
    }

    public MovifyBaseException(String message, int httpCode) {
        this(message, httpCode, new HashMap<>());
    }

    public MovifyBaseException(String message, Map<String, ErrorResponse> errors) {
        this(message, 500, errors);
    }

    public MovifyBaseException(String message, int httpCode, Map<String, ErrorResponse> errors) {
        super(message);
        this.httpCode = httpCode;
        this.errors = errors;
    }


}
