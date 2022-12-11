package com.woong.projectmanager.service;

import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.UserChannel;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.dto.ChannelDto;
import com.woong.projectmanager.dto.UserDto;
import com.woong.projectmanager.repository.ChannelRepository;
import com.woong.projectmanager.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChannelService {

    private final ChannelRepository channelRepository;

    private final UsersRepository usersRepository;

    @Transactional
    public Channel createChannel(ChannelDto channelDto){
        Channel channel = channelDto.toEntity();

        Users manager = usersRepository.findByEmail(channelDto.getManagerEmail()).orElseThrow();

        channel.setManager(manager);

        return channelRepository.save(channel);

    }

    public Channel findChannel(Long channelId){

        Channel channel = channelRepository.findById(channelId).orElseThrow();

        return channel;
    }

}
