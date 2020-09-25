package com.sessionknip.socialnet.web.service.exception;

public class ChatServiceException extends Exception {

    public ChatServiceException(String message) {
        super(message);
    }

    public ChatServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
