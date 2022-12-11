package com.woong.projectmanager.service;

import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.Contents;
import com.woong.projectmanager.domain.Item;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.dto.ChannelDto;
import com.woong.projectmanager.dto.ContentsDto;
import com.woong.projectmanager.dto.ItemDto;
import com.woong.projectmanager.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

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
        UserDto userDto = new UserDto();
        userDto.setEmail("test@test.com");
        userDto.setPassword("1234");
        userDto.setNickName("testName");
        Users user = userService.signUp(userDto);

        ChannelDto channelDto = new ChannelDto();
        channelDto.setName("테스트채널");
        channelDto.setManagerEmail(userDto.getEmail());
        Channel channel = channelService.createChannel(channelDto);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("테스트 아이템");
        itemDto.setWriterEmail(userDto.getEmail());
        itemDto.setStartDate(LocalDate.now());
        itemDto.setEndDate(LocalDate.now().plusDays(1));
        itemDto.setChannelId(channel.getId());
        itemDto.setStatus("대기중");
        Item item = itemService.createItem(itemDto);

        ContentsDto contentsDto = new ContentsDto();
        contentsDto.setContents("테스트 내용");
        contentsDto.setWriterEmail(userDto.getEmail());
        contentsDto.setItemId(item.getId());
        Contents contents = contentsService.createContents(contentsDto);

        //
        var list = contentsService.getContentsList(item.getId());
        Assertions.assertEquals(list.size(), 1);
        Assertions.assertEquals(list.get(0).getWriterEmail(), userDto.getEmail());
        Assertions.assertEquals(list.get(0).getContents(), contentsDto.getContents());
    }
}