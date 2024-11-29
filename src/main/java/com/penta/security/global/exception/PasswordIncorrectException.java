package com.penta.security.global.exception;

import java.io.Serial;

public class PasswordIncorrectException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public PasswordIncorrectException(String message) {
        super(message);
    }
}
