package com.woong.projectmanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woong.projectmanager.common.Message;
import com.woong.projectmanager.common.StatusEnum;
import com.woong.projectmanager.dto.request.ChannelCreateRequestDto;
import com.woong.projectmanager.dto.response.ChannelResponseDto;
import com.woong.projectmanager.exception.EmailSignInFailedException;
import com.woong.projectmanager.service.ChannelService;
import com.woong.projectmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChannelController {

    private final ChannelService channelService;

    private final UserService userService;

    private final ObjectMapper objectMapper;

    @PostMapping("/channel")
    public ResponseEntity<Message> createChannel(@RequestBody @Valid ChannelCreateRequestDto channelCreateRequestDto,
                                                 HttpServletRequest request,
                                                 Errors errors) throws JsonProcessingException {
        Message message = new Message();

        if (errors.hasErrors()) {
            throw new EmailSignInFailedException(objectMapper.writeValueAsString(errors));
        }

        String managerEmail = userService.getUserEmail(request);

        //채널 생성
        ChannelResponseDto channelResponseDto = channelService.createChannel(channelCreateRequestDto, managerEmail);

        message.setStatus(StatusEnum.OK);
        message.setMessage("채널 생성 성공");
        message.setData(channelResponseDto);

        return ResponseEntity.ok().body(message);
    }

    @DeleteMapping("/channel/{id}")
    public ResponseEntity<Message> deleteChannel(@PathVariable Long id,
                                                 HttpServletRequest request){
        Message message = new Message();

        String requestEmail = userService.getUserEmail(request);

        //체널 제거
        ChannelResponseDto channelResponseDto = channelService.removeChannel(id, requestEmail);

        message.setStatus(StatusEnum.OK);
        message.setMessage("채널 삭제 성공");
        message.setData(channelResponseDto);

        return ResponseEntity.ok().body(message);
    }

    @PutMapping("/channel/sub/{id}")
    public ResponseEntity<Message> subscribeChannel(@PathVariable Long id,
                                                    HttpServletRequest request){
        Message message = new Message();

        String requestEmail = userService.getUserEmail(request);

        List<ChannelResponseDto> channelResponseDtoList = userService.addChannel(requestEmail, id);

        message.setStatus(StatusEnum.OK);
        message.setMessage("채널 구독 성공");
        message.setData(channelResponseDtoList);

        return ResponseEntity.ok().body(message);
    }

    @PutMapping("/channel/unsub/{id}")
    public ResponseEntity<Message> unsubscribeChannel(@PathVariable Long id,
                                                    HttpServletRequest request){
        Message message = new Message();

        String requestEmail = userService.getUserEmail(request);

        List<ChannelResponseDto> channelResponseDtoList = userService.removeChannel(requestEmail, id);

        message.setStatus(StatusEnum.OK);
        message.setMessage("채널 구독 해제");
        message.setData(channelResponseDtoList);

        return ResponseEntity.ok().body(message);
    }

}
