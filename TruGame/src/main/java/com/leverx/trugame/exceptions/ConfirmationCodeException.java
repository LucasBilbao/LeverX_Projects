package com.leverx.trugame.exceptions;

public class ConfirmationCodeException extends RuntimeException {

    public ConfirmationCodeException() {
        super("Confirmation code is either expired or incorrect.");
    }
}
