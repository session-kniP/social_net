package com.sessionknip.socialnet.web.security;

public class TokenProviderException extends Exception {

    public TokenProviderException(String message) {
        super(message);
    }

    public TokenProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
