package com.woong.projectmanager.service;

import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.dto.request.ChannelCreateRequestDto;
import com.woong.projectmanager.dto.response.ChannelResponseDto;
import com.woong.projectmanager.dto.request.UserSignUpRequestDto;
import com.woong.projectmanager.dto.response.UserResponseDto;
import com.woong.projectmanager.repository.ChannelRepository;
import com.woong.projectmanager.repository.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
        UserSignUpRequestDto userDto = new UserSignUpRequestDto();
        userDto.setEmail("test1@test.com");
        userDto.setPassword("1234");
        userDto.setNickName("testName");

        //when
        userService.signUp(userDto);

        //then
        UserResponseDto user = userService.findUserEmail(userDto.getEmail());

        Assertions.assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    public void 채널구독(){
        //given
        UserSignUpRequestDto userDto = new UserSignUpRequestDto();
        userDto.setEmail("test2@test.com");
        userDto.setPassword("1234");
        userDto.setNickName("testName");

        ChannelCreateRequestDto channelDto = new ChannelCreateRequestDto();
        channelDto.setName("테스트채널");

        Users user = userService.signUp(userDto);
        ChannelResponseDto channel = channelService.createChannel(channelDto, userDto.getEmail());

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
        UserSignUpRequestDto userDto = new UserSignUpRequestDto();
        userDto.setEmail("test3@test.com");
        userDto.setPassword("1234");
        userDto.setNickName("testName");

        ChannelCreateRequestDto channelDto = new ChannelCreateRequestDto();
        channelDto.setName("테스트채널");

        Users user = userService.signUp(userDto);
        ChannelResponseDto channel = channelService.createChannel(channelDto, userDto.getEmail());

        //when
        userService.addChannel(user.getEmail(), channel.getId());
        userService.removeChannel(user.getEmail(), channel.getId());

        //then
        var list= userService.getChannelList(user.getEmail());

        Assertions.assertEquals(list.size(), 0);
    }
}