package com.woong.projectmanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woong.projectmanager.common.Message;
import com.woong.projectmanager.common.StatusEnum;
import com.woong.projectmanager.dto.request.UserSettingRequestDto;
import com.woong.projectmanager.dto.response.ChannelResponseDto;
import com.woong.projectmanager.dto.response.UserResponseDto;
import com.woong.projectmanager.dto.request.UserSignUpRequestDto;
import com.woong.projectmanager.dto.request.UserSignInRequestDto;
import com.woong.projectmanager.exception.FormValidException;
import com.woong.projectmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    private final ObjectMapper objectMapper;

    @PostMapping("/user/login")
    public ResponseEntity<Message> signIn(@RequestBody @Valid UserSignInRequestDto userSignInRequestDto,
                                          HttpServletRequest request,
                                          HttpServletResponse response,
                                          Errors errors) throws JsonProcessingException {

        Message message = new Message();

        if (errors.hasErrors()) {
            throw new FormValidException(objectMapper.writeValueAsString(errors));
        }

        String token = userService.signIn(userSignInRequestDto);

        //리프레시 토글 발급
        userService.makeRefreshToken(userSignInRequestDto.getEmail(), request, response);

        message.setStatus(StatusEnum.OK);
        message.setMessage("로그인 성공");
        message.setData(token);

        return ResponseEntity.ok().body(message);
    }

    @GetMapping("/user")
    public ResponseEntity<Message> signIn(HttpServletRequest request) throws JsonProcessingException {

        Message message = new Message();

        String requestEmail = userService.getUserEmail(request);

        UserResponseDto userResponseDto = userService.findUserEmail(requestEmail);

        message.setStatus(StatusEnum.OK);
        message.setMessage("회원 정보 반환 성공");
        message.setData(userResponseDto);

        return ResponseEntity.ok().body(message);
    }

    @PostMapping("/user")
    public ResponseEntity<Message> signUp(@RequestBody @Valid UserSignUpRequestDto userSignUpRequestDto,
                                          Errors errors) throws JsonProcessingException {

        Message message = new Message();

        if (errors.hasErrors()) {
            throw new FormValidException(objectMapper.writeValueAsString(errors));
        }

        UserResponseDto userResponseDto = userService.signUp(userSignUpRequestDto);

        //응답 메세지 설정

        message.setStatus(StatusEnum.OK);
        message.setMessage("가입 성공");
        message.setData(userResponseDto);

        return ResponseEntity.ok().body(message);
    }

    @PutMapping("/user/channel/{id}")
    public ResponseEntity<Message> changeCurrentChannel(@PathVariable Long id,
                                                    HttpServletRequest request){
        Message message = new Message();

        String requestEmail = userService.getUserEmail(request);

        ChannelResponseDto channelResponseDto = userService.setCurrentChannel(requestEmail, id);

        message.setStatus(StatusEnum.OK);
        message.setMessage("현재 채널 변경 성공");
        message.setData(channelResponseDto);

        return ResponseEntity.ok().body(message);
    }

    @PutMapping("/user/setting")
    public ResponseEntity<Message> changeUserSetting(@RequestBody @Valid UserSettingRequestDto userSettingRequestDto,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response,
                                                     Errors errors) throws JsonProcessingException {
        Message message = new Message();

        String requestEmail = userService.getUserEmail(request);

        UserResponseDto userResponseDto = userService.setUserSettings(requestEmail, userSettingRequestDto);

        if (errors.hasErrors()) {
            throw new FormValidException(objectMapper.writeValueAsString(errors));
        }

        message.setStatus(StatusEnum.OK);
        message.setMessage("유저 설정 변경 성공");
        message.setData(userResponseDto);

        return ResponseEntity.ok().body(message);
    }
}
