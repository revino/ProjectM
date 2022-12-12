package com.woong.projectmanager.exception;

public class AccessTokenFailedException extends RuntimeException{
    public AccessTokenFailedException() {
        super();
    }

    public AccessTokenFailedException(String message) {
        super(message);
    }

    public AccessTokenFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
