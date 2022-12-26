package com.woong.projectmanager.service;

import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.dto.request.ChannelCreateRequestDto;
import com.woong.projectmanager.dto.response.ChannelResponseDto;
import com.woong.projectmanager.exception.RemoveChannelFailedException;
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

    private final UserService userService;

    @Transactional
    public ChannelResponseDto createChannel(ChannelCreateRequestDto channelCreateRequestDto, String managerEmail){

        //유저 찾기
        Users manager = usersRepository.findByEmail(managerEmail).orElseThrow();

        //채널 생성
        Channel channel = channelCreateRequestDto.toEntity();
        channel.setManager(manager);

        //생성된 채널 저장
        channelRepository.save(channel);

        //생성한 채널에 유저 추가
        userService.addChannel(manager, channel);

        ChannelResponseDto channelResponseDto = new ChannelResponseDto(channel);

        return channelResponseDto;
    }

    @Transactional
    public ChannelResponseDto removeChannel(Long id, String requestEmail){

        //채널 찾기
        Channel channel = channelRepository.findById(id).orElseThrow();

        //관리자 아이디 확인
        if(!channel.getManager().getEmail().equals(requestEmail)){
            throw new RemoveChannelFailedException("관리자 이메일이 일치하지 않습니다.");
        }

        // 삭제
        channelRepository.delete(channel);

        ChannelResponseDto channelResponseDto = new ChannelResponseDto(channel);

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
