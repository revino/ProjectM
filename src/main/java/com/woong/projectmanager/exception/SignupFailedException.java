package com.woong.projectmanager.exception;

public class SignupFailedException extends RuntimeException{
    public SignupFailedException() {
    }

    public SignupFailedException(String message) {
        super(message);
    }

    public SignupFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignupFailedException(Throwable cause) {
        super(cause);
    }
}
