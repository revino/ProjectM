package com.woong.projectmanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woong.projectmanager.common.Message;
import com.woong.projectmanager.common.StatusEnum;
import com.woong.projectmanager.dto.request.ItemAddRequestDto;
import com.woong.projectmanager.dto.response.ItemResponseDto;
import com.woong.projectmanager.exception.EmailSignInFailedException;
import com.woong.projectmanager.exception.FormValidException;
import com.woong.projectmanager.service.ItemService;
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
public class ItemController {

    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final ItemService itemService;

    @GetMapping("/itemList/{id}")
    public ResponseEntity<Message> getItemList(@PathVariable Long id,
                                               HttpServletRequest request){
        Message message = new Message();

        String requestEmail = userService.getUserEmail(request);

        //아이템 조회
        List<ItemResponseDto> itemResponseDto = itemService.getItemList(id);

        message.setStatus(StatusEnum.OK);
        message.setMessage("아이템 리스트 조회 성공");
        message.setData(itemResponseDto);

        return ResponseEntity.ok().body(message);
    }

    @PostMapping("/item")
    public ResponseEntity<Message> addItem(@RequestBody @Valid ItemAddRequestDto itemAddRequestDto,
                                                 HttpServletRequest request,
                                                 Errors errors) throws JsonProcessingException {
        Message message = new Message();

        if (errors.hasErrors()) {
            throw new FormValidException(objectMapper.writeValueAsString(errors));
        }

        String managerEmail = userService.getUserEmail(request);

        //아이템 생성
        ItemResponseDto itemResponseDto = itemService.createItem(itemAddRequestDto, managerEmail);

        message.setStatus(StatusEnum.OK);
        message.setMessage("아이템 생성 성공");
        message.setData(itemResponseDto);

        return ResponseEntity.ok().body(message);
    }

    @PostMapping("/item/alarm/{id}")
    public ResponseEntity<Message> addAlarmItem(@PathVariable Long id,
                                                HttpServletRequest request) {
        Message message = new Message();

        String managerEmail = userService.getUserEmail(request);

        //아이템 알람 설정
        ItemResponseDto itemResponseDto = itemService.addAlarmUser(managerEmail, id);

        message.setStatus(StatusEnum.OK);
        message.setMessage("아이템 알람 설정 성공");
        message.setData(itemResponseDto);

        return ResponseEntity.ok().body(message);
    }

    @DeleteMapping("/item/alarm/{id}")
    public ResponseEntity<Message> removeAlarmItem(@PathVariable Long id,
                                                HttpServletRequest request) {
        Message message = new Message();

        String managerEmail = userService.getUserEmail(request);

        //아이템 알람 해제
        ItemResponseDto itemResponseDto = itemService.removeAlarmUser(managerEmail, id);

        message.setStatus(StatusEnum.OK);
        message.setMessage("아이템 알람 해제 성공");
        message.setData(itemResponseDto);

        return ResponseEntity.ok().body(message);
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<Message> getItem(@PathVariable Long id, HttpServletRequest request){
        Message message = new Message();

        //아이템 조회
        ItemResponseDto itemResponseDto = itemService.getItem(id);

        message.setStatus(StatusEnum.OK);
        message.setMessage("아이템 조회 성공");
        message.setData(itemResponseDto);

        return ResponseEntity.ok().body(message);
    }

    @PutMapping("/item/{id}")
    public ResponseEntity<Message> updateItem(@RequestBody @Valid ItemAddRequestDto itemAddRequestDto,
                                              @PathVariable Long id,
                                              HttpServletRequest request,
                                              Errors errors) throws JsonProcessingException {
        Message message = new Message();

        if (errors.hasErrors()) {
            throw new FormValidException(objectMapper.writeValueAsString(errors));
        }

        String managerEmail = userService.getUserEmail(request);

        //아이템 업데이트
        ItemResponseDto itemResponseDto = itemService.updateItem(id, itemAddRequestDto, managerEmail);

        message.setStatus(StatusEnum.OK);
        message.setMessage("아이템 수정 성공");
        message.setData(itemResponseDto);

        return ResponseEntity.ok().body(message);
    }

    @DeleteMapping("/item/{id}")
    public ResponseEntity<Message> removeItem(@PathVariable Long id,
                                              HttpServletRequest request) {
        Message message = new Message();

        String managerEmail = userService.getUserEmail(request);

        //아이템 삭제
        ItemResponseDto itemResponseDto = itemService.removeItem(id, managerEmail);

        message.setStatus(StatusEnum.OK);
        message.setMessage("아이템 삭제 성공");
        message.setData(itemResponseDto);

        return ResponseEntity.ok().body(message);
    }

}
