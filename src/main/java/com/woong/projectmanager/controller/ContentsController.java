package com.woong.projectmanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woong.projectmanager.common.Message;
import com.woong.projectmanager.common.StatusEnum;
import com.woong.projectmanager.dto.request.ContentsAddRequestDto;
import com.woong.projectmanager.dto.response.ContentsResponseDto;
import com.woong.projectmanager.dto.response.ItemResponseDto;
import com.woong.projectmanager.exception.EmailSignInFailedException;
import com.woong.projectmanager.service.ContentsService;
import com.woong.projectmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ContentsController {

    private final ContentsService contentsService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @PostMapping("/contents")
    public ResponseEntity<Message> addContents(@RequestBody @Valid ContentsAddRequestDto contentsAddRequestDto,
                                                 HttpServletRequest request,
                                                 Errors errors) throws JsonProcessingException {
        Message message = new Message();

        if (errors.hasErrors()) {
            throw new EmailSignInFailedException(objectMapper.writeValueAsString(errors));
        }

        String requestEmail = userService.getUserEmail(request);

        //컨텐츠 생성
        ContentsResponseDto contentsResponseDto = contentsService.createContents(contentsAddRequestDto, requestEmail);

        message.setStatus(StatusEnum.OK);
        message.setMessage("컨텐츠 생성 성공");
        message.setData(contentsResponseDto);

        return ResponseEntity.ok().body(message);
    }

    @GetMapping("/contentsList/{id}")
    public ResponseEntity<Message> getContentsList(@PathVariable Long id,
                                               HttpServletRequest request){
        Message message = new Message();

        String managerEmail = userService.getUserEmail(request);

        //아이템 생성
        List<ContentsResponseDto> contentsResponseDtoList = contentsService.getContentsList(id);

        message.setStatus(StatusEnum.OK);
        message.setMessage("컨텐츠 리스트 조회 성공");
        message.setData(contentsResponseDtoList);

        return ResponseEntity.ok().body(message);
    }
}
