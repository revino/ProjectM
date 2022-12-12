package com.woong.projectmanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woong.projectmanager.common.Message;
import com.woong.projectmanager.common.StatusEnum;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.dto.UserResponseDto;
import com.woong.projectmanager.dto.UserSignUpRequestDto;
import com.woong.projectmanager.dto.UserSignInRequestDto;
import com.woong.projectmanager.exception.EmailSignInFailedException;
import com.woong.projectmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    private final ObjectMapper objectMapper;

    @GetMapping("/signIn")
    public ResponseEntity<Message> signIn(@RequestBody @Valid UserSignInRequestDto userSignInRequestDto,
                                          HttpServletRequest request,
                                          HttpServletResponse response,
                                          Errors errors) throws JsonProcessingException {

        Message message = new Message();

        if (errors.hasErrors()) {
            throw new EmailSignInFailedException(objectMapper.writeValueAsString(errors));
        }

        String token = userService.signIn(userSignInRequestDto);

        //리프레시 토글 발급
        userService.makeRefreshToken(userSignInRequestDto.getEmail(), request, response);

        message.setStatus(StatusEnum.OK);
        message.setMessage("로그인 성공");
        message.setData(token);

        return ResponseEntity.ok().body(message);
    }

    @PostMapping("/signUp")
    public ResponseEntity<Message> signUp(@RequestBody @Valid UserSignUpRequestDto userSignUpRequestDto,
                                          HttpServletRequest request,
                                          HttpServletResponse response,
                                          Errors errors) throws JsonProcessingException {

        Message message = new Message();

        if (errors.hasErrors()) {
            throw new EmailSignInFailedException(objectMapper.writeValueAsString(errors));
        }

        Users user = userService.signUp(userSignUpRequestDto);

        //응답 메세지 설정
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setNickName(user.getNickName());
        userResponseDto.setPicture(user.getPicture());
        userResponseDto.setLoginProviderType(user.getLoginProviderType());

        message.setStatus(StatusEnum.OK);
        message.setMessage("가입 성공");
        message.setData(userResponseDto);

        return ResponseEntity.ok().body(message);
    }
}
