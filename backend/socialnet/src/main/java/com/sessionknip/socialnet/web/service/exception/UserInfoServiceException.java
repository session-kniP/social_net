package com.sessionknip.socialnet.web.service.exception;

public class UserInfoServiceException extends Exception {

    public UserInfoServiceException(String message) {
        super(message);
    }

    public UserInfoServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
