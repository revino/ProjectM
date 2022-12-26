package com.woong.projectmanager.exception;

public class ItemFindFailedException extends RuntimeException{
    public ItemFindFailedException() {
    }

    public ItemFindFailedException(String message) {
        super(message);
    }

    public ItemFindFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemFindFailedException(Throwable cause) {
        super(cause);
    }
}
