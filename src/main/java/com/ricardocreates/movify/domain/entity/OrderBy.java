package com.ricardocreates.movify.domain.entity;


public enum OrderBy {

    TITLE("title"),

    RELEASE_DATE("releaseDate"),

    LANGUAGE("language"),

    RATING("rating");

    private String value;

    OrderBy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}

