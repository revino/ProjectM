package com.woong.projectmanager.common;

import lombok.Data;

@Data
public class Message {

    private StatusEnum status;
    private String message;
    private Object data;

    public Message() {
        this.status = StatusEnum.BAD_REQUEST;
        this.data = null;
        this.message = null;
    }

    public boolean isOk(){
        return status.equals(StatusEnum.OK);
    }
}
