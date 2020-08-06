package com.sessionknip.socialnet.web.service.exception;

public class UserInfoException extends Exception {

    public UserInfoException(String message) {
        super(message);
    }

    public UserInfoException(String message, Throwable cause) {
        super(message, cause);
    }
}
