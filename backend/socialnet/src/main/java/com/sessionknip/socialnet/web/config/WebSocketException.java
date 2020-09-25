package com.sessionknip.socialnet.web.config;

public class WebSocketException extends Exception {

    public WebSocketException(String message) {
        super(message);
    }

    public WebSocketException(String message, Throwable cause) {
        super(message, cause);
    }
}
