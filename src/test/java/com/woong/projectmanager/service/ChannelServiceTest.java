package com.woong.projectmanager.service;

import com.woong.projectmanager.DatabaseTest;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ChannelServiceTest extends DatabaseTest{

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
        UserSignUpRequestDto userDto = new UserSignUpRequestDto();
        userDto.setEmail("test@test.com");
        userDto.setPassword("1234");
        userDto.setNickName("testName");

        ChannelCreateRequestDto channelDto = new ChannelCreateRequestDto();
        channelDto.setName("테스트채널");

        //when
        UserResponseDto userResponseDto = userService.signUp(userDto);
        ChannelResponseDto channelResponse = channelService.createChannel(channelDto, userDto.getEmail());

        //
        Assertions.assertEquals(channelResponse.getManagerEmail(), userResponseDto.getEmail());
        Assertions.assertEquals(channelResponse.getName(), channelDto.getName());
    }

}