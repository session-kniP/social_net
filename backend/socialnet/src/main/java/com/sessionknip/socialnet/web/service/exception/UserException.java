package com.sessionknip.socialnet.web.service.exception;

public class UserException extends Exception {

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Exception cause) {
        super(message, cause);
    }
}
