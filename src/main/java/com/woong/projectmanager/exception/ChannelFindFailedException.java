package com.woong.projectmanager.exception;

public class ChannelFindFailedException extends RuntimeException{

    public ChannelFindFailedException() {
    }

    public ChannelFindFailedException(String message) {
        super(message);
    }

    public ChannelFindFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChannelFindFailedException(Throwable cause) {
        super(cause);
    }
}
