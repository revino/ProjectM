package com.woong.projectmanager.exception;

public class ChannelSubscribeFailedException extends RuntimeException{
    public ChannelSubscribeFailedException() {
    }

    public ChannelSubscribeFailedException(String message) {
        super(message);
    }

    public ChannelSubscribeFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChannelSubscribeFailedException(Throwable cause) {
        super(cause);
    }
}
