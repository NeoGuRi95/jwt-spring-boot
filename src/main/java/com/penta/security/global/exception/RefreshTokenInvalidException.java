package com.penta.security.global.exception;

import java.io.Serial;

public class RefreshTokenInvalidException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public RefreshTokenInvalidException(String message) {
        super(message);
    }
}
