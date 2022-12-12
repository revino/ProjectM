package com.woong.projectmanager.exception;

public class EmailSignInFailedException extends RuntimeException{

    public EmailSignInFailedException(String msg, Throwable t){
        super(msg, t);
    }

    public EmailSignInFailedException(String msg){
        super(msg);
    }

    public EmailSignInFailedException() {
        super();
    }
}
