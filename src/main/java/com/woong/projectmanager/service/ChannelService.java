package com.woong.projectmanager.service;

import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.dto.ChannelCreateRequestDto;
import com.woong.projectmanager.dto.ChannelResponseDto;
import com.woong.projectmanager.repository.ChannelRepository;
import com.woong.projectmanager.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChannelService {

    private final ChannelRepository channelRepository;

    private final UsersRepository usersRepository;

    @Transactional
    public ChannelResponseDto createChannel(ChannelCreateRequestDto channelCreateRequestDto, String managerEmail){

        Channel channel = channelCreateRequestDto.toEntity();

        Users manager = usersRepository.findByEmail(managerEmail).orElseThrow();

        channel.setManager(manager);

        channelRepository.save(channel);

        ChannelResponseDto channelResponseDto = new ChannelResponseDto();
        channelResponseDto.setId(channel.getId());
        channelResponseDto.setCreatedAt(channel.getCreatedAt());
        channelResponseDto.setName(channel.getName());
        channelResponseDto.setManagerEmail(channel.getManager().getEmail());

        return channelResponseDto;
    }

    public Long findChannel(String name){
        Channel channel = channelRepository.findByName(name).orElseThrow();

        return channel.getId();
    }

    public Long findChannel(Long channelId){

        Channel channel = channelRepository.findById(channelId).orElseThrow();

        return channel.getId();
    }

}
