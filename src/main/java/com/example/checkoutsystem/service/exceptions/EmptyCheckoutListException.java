package com.example.checkoutsystem.service.exceptions;

public class EmptyCheckoutListException extends RuntimeException {

    private final String errorMessage;

    public EmptyCheckoutListException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
