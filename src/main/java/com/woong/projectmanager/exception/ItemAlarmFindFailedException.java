package com.woong.projectmanager.exception;

public class ItemAlarmFindFailedException extends RuntimeException{
    public ItemAlarmFindFailedException() {
    }

    public ItemAlarmFindFailedException(String message) {
        super(message);
    }

    public ItemAlarmFindFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemAlarmFindFailedException(Throwable cause) {
        super(cause);
    }
}
