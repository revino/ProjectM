package com.woong.projectmanager.exception;

public class FormValidException extends RuntimeException{
    public FormValidException() {
    }

    public FormValidException(String message) {
        super(message);
    }

    public FormValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public FormValidException(Throwable cause) {
        super(cause);
    }
}
