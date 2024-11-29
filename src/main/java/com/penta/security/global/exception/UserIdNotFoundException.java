package com.penta.security.global.exception;

import java.io.Serial;

public class UserIdNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UserIdNotFoundException(String message) {
        super(message);
    }
}
