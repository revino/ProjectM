package com.woong.projectmanager.exception;

public class UserFindFailedException extends RuntimeException{
    public UserFindFailedException() {
    }

    public UserFindFailedException(String message) {
        super(message);
    }

    public UserFindFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserFindFailedException(Throwable cause) {
        super(cause);
    }
}
