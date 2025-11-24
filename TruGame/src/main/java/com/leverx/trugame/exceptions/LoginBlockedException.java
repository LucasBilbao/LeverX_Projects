package com.leverx.trugame.exceptions;

public class LoginBlockedException extends RuntimeException {

    public LoginBlockedException(Long minutes, Long seconds) {
        super("You have failed to authenticate 3 times, login is blocked for 5 minutes. Please try again after " +
                minutes + " minutes and " + seconds + " seconds.");
    }
}
