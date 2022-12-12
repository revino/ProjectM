package com.woong.projectmanager.exception;

public class RedirectUrlFailedException extends RuntimeException{
    public RedirectUrlFailedException() {
        super();
    }

    public RedirectUrlFailedException(String message) {
        super(message);
    }

    public RedirectUrlFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedirectUrlFailedException(Throwable cause) {
        super(cause);
    }
}
