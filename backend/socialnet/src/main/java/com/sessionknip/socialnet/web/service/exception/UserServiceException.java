package com.sessionknip.socialnet.web.service.exception;

public class UserServiceException extends Exception {

    public UserServiceException(String message) {
        super(message);
    }

    public UserServiceException(String message, Exception cause) {
        super(message, cause);
    }
}
