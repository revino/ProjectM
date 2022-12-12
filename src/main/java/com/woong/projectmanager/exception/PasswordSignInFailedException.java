package com.woong.projectmanager.exception;

public class PasswordSignInFailedException extends RuntimeException{
    public PasswordSignInFailedException() {
        super();
    }

    public PasswordSignInFailedException(String message) {
        super(message);
    }

    public PasswordSignInFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
