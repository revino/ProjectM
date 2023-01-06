package com.woong.projectmanager.exception;

public class ContentFindFailedException extends RuntimeException{
    public ContentFindFailedException() {
    }

    public ContentFindFailedException(String message) {
        super(message);
    }

    public ContentFindFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContentFindFailedException(Throwable cause) {
        super(cause);
    }
}
