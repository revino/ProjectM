package com.woong.projectmanager.service;

import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.Item;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.dto.ChannelCreateRequestDto;
import com.woong.projectmanager.dto.ChannelResponseDto;
import com.woong.projectmanager.dto.ItemDto;
import com.woong.projectmanager.dto.UserSignUpRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ItemServiceTest {

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
        Users user = userService.signUp(userDto);

        ChannelCreateRequestDto channelDto = new ChannelCreateRequestDto();
        channelDto.setName("테스트채널");
        ChannelResponseDto channel = channelService.createChannel(channelDto, userDto.getEmail());

        ItemDto itemDto = new ItemDto();
        itemDto.setName("테스트 아이템");
        itemDto.setWriterEmail(userDto.getEmail());
        itemDto.setStartDate(LocalDate.now());
        itemDto.setEndDate(LocalDate.now().plusDays(1));
        itemDto.setChannelId(channel.getId());
        itemDto.setStatus("대기중");
        Item item = itemService.createItem(itemDto);

        //
        var list = itemService.getItemList(channel.getId());
        Assertions.assertEquals(list.size(), 1);
        Assertions.assertEquals(list.get(0).getWriterEmail(), userDto.getEmail());
    }

    @Test
    public void 아이템삭제(){
        UserSignUpRequestDto userDto = new UserSignUpRequestDto();
        userDto.setEmail("test@test.com");
        userDto.setPassword("1234");
        userDto.setNickName("testName");
        Users user = userService.signUp(userDto);

        ChannelCreateRequestDto channelDto = new ChannelCreateRequestDto();
        channelDto.setName("테스트채널");
        ChannelResponseDto channel = channelService.createChannel(channelDto, userDto.getEmail());

        ItemDto itemDto = new ItemDto();
        itemDto.setName("테스트 아이템");
        itemDto.setWriterEmail(userDto.getEmail());
        itemDto.setStartDate(LocalDate.now());
        itemDto.setEndDate(LocalDate.now().plusDays(1));
        itemDto.setChannelId(channel.getId());
        itemDto.setStatus("대기중");
        Item item = itemService.createItem(itemDto);

        //
        itemService.removeItem(item.getId());

        //
        var list = itemService.getItemList(channel.getId());
        Assertions.assertEquals(list.size(), 0);
    }

}