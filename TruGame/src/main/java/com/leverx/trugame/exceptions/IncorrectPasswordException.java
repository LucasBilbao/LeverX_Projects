package com.leverx.trugame.exceptions;

public class IncorrectPasswordException extends RuntimeException {

    public IncorrectPasswordException() {
        super("Password is incorrect.");
    }
}
