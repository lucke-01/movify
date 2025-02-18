package com.ricardocreates.movify.customexception;

import org.springframework.http.HttpStatusCode;

public class UserNotFoundException extends RuntimeException {
    private String description;
    private HttpStatusCode code;

    public UserNotFoundException(String message, String description, HttpStatusCode code) {
        super(message);
        this.description = description;
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HttpStatusCode getCode() {
        return code;
    }

    public void setCode(HttpStatusCode code) {
        this.code = code;
    }
}
