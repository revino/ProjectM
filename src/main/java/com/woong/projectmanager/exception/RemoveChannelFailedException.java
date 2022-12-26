package com.woong.projectmanager.exception;

public class RemoveChannelFailedException extends RuntimeException{

    public RemoveChannelFailedException() {
    }

    public RemoveChannelFailedException(String message) {
        super(message);
    }

    public RemoveChannelFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoveChannelFailedException(Throwable cause) {
        super(cause);
    }
}
