package com.woong.projectmanager.exception;

public class ItemAlarmAddFailedException extends RuntimeException{
    public ItemAlarmAddFailedException() {
    }

    public ItemAlarmAddFailedException(String message) {
        super(message);
    }

    public ItemAlarmAddFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemAlarmAddFailedException(Throwable cause) {
        super(cause);
    }
}
