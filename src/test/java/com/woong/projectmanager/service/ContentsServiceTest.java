package com.woong.projectmanager.service;

import com.woong.projectmanager.domain.Contents;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.dto.*;
import com.woong.projectmanager.dto.request.ChannelCreateRequestDto;
import com.woong.projectmanager.dto.request.ContentsAddRequestDto;
import com.woong.projectmanager.dto.request.ItemAddRequestDto;
import com.woong.projectmanager.dto.request.UserSignUpRequestDto;
import com.woong.projectmanager.dto.response.ChannelResponseDto;
import com.woong.projectmanager.dto.response.ContentsResponseDto;
import com.woong.projectmanager.dto.response.ItemResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ContentsServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    @Autowired
    ChannelService channelService;

    @Autowired
    ContentsService contentsService;

    @Test
    public void 컨텐츠생성(){
        UserSignUpRequestDto userDto = new UserSignUpRequestDto();
        userDto.setEmail("test@test.com");
        userDto.setPassword("1234");
        userDto.setNickName("testName");
        Users user = userService.signUp(userDto);

        ChannelCreateRequestDto channelDto = new ChannelCreateRequestDto();
        channelDto.setName("테스트채널");
        ChannelResponseDto channelRep = channelService.createChannel(channelDto, userDto.getEmail());

        ItemAddRequestDto itemAddRequestDto = new ItemAddRequestDto();
        itemAddRequestDto.setName("테스트 아이템");
        itemAddRequestDto.setStartDate(LocalDate.now());
        itemAddRequestDto.setEndDate(LocalDate.now().plusDays(1));
        itemAddRequestDto.setChannelId(channelService.findChannel(channelRep.getName()));
        itemAddRequestDto.setStatus("대기중");
        ItemResponseDto itemResponseDto = itemService.createItem(itemAddRequestDto, userDto.getEmail());


        ContentsAddRequestDto contentsDto = new ContentsAddRequestDto();
        contentsDto.setContents("테스트 내용");
        contentsDto.setItemId(itemResponseDto.getId());
        ContentsResponseDto contentsResponseDto = contentsService.createContents(contentsDto, userDto.getEmail());

        //
        var list = contentsService.getContentsList(itemResponseDto.getId());
        Assertions.assertEquals(list.size(), 1);
        Assertions.assertEquals(list.get(0).getWriterEmail(), userDto.getEmail());
        Assertions.assertEquals(list.get(0).getContents(), contentsDto.getContents());
    }
}