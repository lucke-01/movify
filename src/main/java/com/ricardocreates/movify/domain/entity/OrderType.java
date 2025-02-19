package com.ricardocreates.movify.domain.entity;


public enum OrderType {

    ASC("asc"),

    DESC("desc");

    private String value;

    OrderType(String value) {
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