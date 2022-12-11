package com.woong.projectmanager.service;

import com.woong.projectmanager.DatabaseTest;
import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.dto.ChannelDto;
import com.woong.projectmanager.dto.UserDto;
import com.woong.projectmanager.repository.ChannelRepository;
import com.woong.projectmanager.repository.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceTest {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ChannelRepository channelRepository;

    @Autowired
    UserService userService;

    @Autowired
    ChannelService channelService;

    @Test
    public void 회원가입() throws Exception{
        //given
        UserDto userDto = new UserDto();
        userDto.setEmail("test1@test.com");
        userDto.setPassword("1234");
        userDto.setNickName("testName");

        //when
        userService.signUp(userDto);

        //then
        Users user = userService.findUserEmail(userDto.getEmail());

        Assertions.assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    public void 채널구독(){
        //given
        UserDto userDto = new UserDto();
        userDto.setEmail("test2@test.com");
        userDto.setPassword("1234");
        userDto.setNickName("testName");

        ChannelDto channelDto = new ChannelDto();
        channelDto.setName("테스트채널");
        channelDto.setManagerEmail(userDto.getEmail());

        Users user = userService.signUp(userDto);
        Channel channel = channelService.createChannel(channelDto);

        //when
        userService.addChannel(user.getEmail(), channel.getId());

        //then
        var list= userService.getChannelList(user.getEmail());

        Assertions.assertEquals(list.size(), 1);
        Assertions.assertEquals(list.get(0).getName(), channel.getName());
    }

    @Test
    public void 채널구독취소(){
        //given
        UserDto userDto = new UserDto();
        userDto.setEmail("test3@test.com");
        userDto.setPassword("1234");
        userDto.setNickName("testName");

        ChannelDto channelDto = new ChannelDto();
        channelDto.setName("테스트채널");
        channelDto.setManagerEmail(userDto.getEmail());

        Users user = userService.signUp(userDto);
        Channel channel = channelService.createChannel(channelDto);

        //when
        userService.addChannel(user.getEmail(), channel.getId());
        userService.removeChannel(user.getEmail(), channel.getId());

        //then
        var list= userService.getChannelList(user.getEmail());

        Assertions.assertEquals(list.size(), 0);
    }
}