package com.sample.rest.server.domain;

public enum BookStatus {
    SUCCESS("Your booking is confirmed"),
    FAILURE("Insufficient space");

    private String message;

    BookStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
