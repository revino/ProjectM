package com.woong.projectmanager.exception;

public class ChannelUpdateValidException extends RuntimeException{
    public ChannelUpdateValidException() {
    }

    public ChannelUpdateValidException(String message) {
        super(message);
    }

    public ChannelUpdateValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChannelUpdateValidException(Throwable cause) {
        super(cause);
    }
}
