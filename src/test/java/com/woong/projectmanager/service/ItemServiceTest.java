package com.woong.projectmanager.service;

import com.woong.projectmanager.DatabaseTest;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.dto.request.ChannelCreateRequestDto;
import com.woong.projectmanager.dto.request.ItemAddRequestDto;
import com.woong.projectmanager.dto.request.UserSignUpRequestDto;
import com.woong.projectmanager.dto.response.ChannelResponseDto;
import com.woong.projectmanager.dto.response.ItemResponseDto;
import com.woong.projectmanager.dto.response.UserResponseDto;
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
class ItemServiceTest extends DatabaseTest {

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    @Autowired
    ChannelService channelService;

    @Test
    public void 아이템생성(){
        UserSignUpRequestDto userDto = new UserSignUpRequestDto();
        userDto.setEmail("test@test.com");
        userDto.setPassword("1234");
        userDto.setNickName("testName");
        UserResponseDto userResponseDto = userService.signUp(userDto);

        ChannelCreateRequestDto channelDto = new ChannelCreateRequestDto();
        channelDto.setName("테스트채널");
        ChannelResponseDto channel = channelService.createChannel(channelDto, userDto.getEmail());

        ItemAddRequestDto itemDto = new ItemAddRequestDto();
        itemDto.setName("테스트 아이템");
        itemDto.setStartDate(LocalDate.now());
        itemDto.setEndDate(LocalDate.now().plusDays(1));
        itemDto.setChannelId(channel.getId());
        itemDto.setStatus("대기중");
        ItemResponseDto itemResponseDto = itemService.createItem(itemDto, userDto.getEmail());

        //
        var list = itemService.getItemList(channel.getId());
        Assertions.assertEquals(list.size(), 1);
        Assertions.assertEquals(list.get(0).getWriterEmail(), userDto.getEmail());
    }

    @Test
    public void 아이템삭제(){
        UserSignUpRequestDto userDto = new UserSignUpRequestDto();
        userDto.setEmail("test2@test.com");
        userDto.setPassword("1234");
        userDto.setNickName("testName");
        UserResponseDto userResponseDto = userService.signUp(userDto);

        ChannelCreateRequestDto channelDto = new ChannelCreateRequestDto();
        channelDto.setName("테스트채널2");
        ChannelResponseDto channel = channelService.createChannel(channelDto, userDto.getEmail());

        ItemAddRequestDto itemDto = new ItemAddRequestDto();
        itemDto.setName("테스트 아이템");
        itemDto.setStartDate(LocalDate.now());
        itemDto.setEndDate(LocalDate.now().plusDays(1));
        itemDto.setChannelId(channel.getId());
        itemDto.setStatus("대기중");
        ItemResponseDto itemResponseDto = itemService.createItem(itemDto, userDto.getEmail());

        //
        itemService.removeItem(itemResponseDto.getId(), userDto.getEmail());

        //
        var list = itemService.getItemList(channel.getId());
        Assertions.assertEquals(list.size(), 0);
    }

}