package com.woong.projectmanager.service;

import com.woong.projectmanager.DatabaseTest;
import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.dto.ChannelDto;
import com.woong.projectmanager.dto.UserDto;
import com.woong.projectmanager.repository.ChannelRepository;
import com.woong.projectmanager.repository.UsersRepository;
import org.apache.catalina.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ChannelServiceTest {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ChannelRepository channelRepository;

    @Autowired
    UserService userService;

    @Autowired
    ChannelService channelService;

    @Test
    public void 채널생성(){
        UserDto userDto = new UserDto();
        userDto.setEmail("test@test.com");
        userDto.setPassword("1234");
        userDto.setNickName("testName");

        ChannelDto channelDto = new ChannelDto();
        channelDto.setName("테스트채널");
        channelDto.setManagerEmail(userDto.getEmail());

        //when
        Users user = userService.signUp(userDto);
        Channel channel = channelService.createChannel(channelDto);

        //
        Assertions.assertEquals(channel.getManager().getEmail(), user.getEmail());
        Assertions.assertEquals(channel.getName(), channelDto.getName());
    }

}