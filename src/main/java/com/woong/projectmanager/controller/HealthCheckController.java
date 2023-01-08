package com.woong.projectmanager.controller;

import com.woong.projectmanager.common.Message;
import com.woong.projectmanager.common.StatusEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/")
    public ResponseEntity<Message> HealthCheck(){
        Message message = new Message();
        message.setStatus(StatusEnum.OK.OK);
        return ResponseEntity.ok().body(message);
    }

}
