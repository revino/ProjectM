package com.woong.projectmanager.service;

import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.Item;
import com.woong.projectmanager.domain.UserChannel;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.dto.request.ChannelCreateRequestDto;
import com.woong.projectmanager.dto.response.ChannelResponseDto;
import com.woong.projectmanager.dto.response.ItemResponseDto;
import com.woong.projectmanager.exception.*;
import com.woong.projectmanager.repository.ChannelRepository;
import com.woong.projectmanager.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
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

        //validation
        if(channelCreateRequestDto.getName().length() >= 30) {
            throw new ChannelUpdateValidException("채널 이름이 30자 이상입니다." );
        }

        //유저 찾기
        Users manager = usersRepository.findByEmail(managerEmail).orElseThrow(()->new UserFindFailedException("존재하지 않는 유저입니다."));

        //채널 생성
        Channel channel = channelCreateRequestDto.toEntity();
        channel.setManager(manager);

        //생성된 채널 저장
        channelRepository.save(channel);

        //생성한 채널에 유저 추가
        userService.subscribeChannelWithoutCheck(manager, channel);

        ChannelResponseDto channelResponseDto = new ChannelResponseDto(channel);

        return channelResponseDto;
    }

    @Transactional
    public ChannelResponseDto updateChannel(Long channelId, ChannelCreateRequestDto channelCreateRequestDto, String requestEmail){

        //validation
        if(channelCreateRequestDto.getName().length() >= 30) {
            throw new ChannelUpdateValidException("채널 이름이 30자 이상입니다." );
        }

        Channel channel = channelRepository.findByIdWithManager(channelId).orElseThrow(()-> new ItemFindFailedException("존재하지 않는 채널입니다."));
        String managerEmail = channel.getManager().getEmail();

        if(!managerEmail.equals(requestEmail)){
            throw new AuthenticationFailedException("수정 권한이 없습니다. " + managerEmail);
        }

        channel.update(channelCreateRequestDto);

        channelRepository.save(channel);

        ChannelResponseDto channelResponseDto = new ChannelResponseDto(channel);

        return channelResponseDto;
    }

    @Transactional
    public ChannelResponseDto removeChannel(Long id, String requestEmail){

        //채널 찾기
        Channel channel = channelRepository.findByIdWithAllMember(id).orElseThrow(()->new ChannelFindFailedException("존재하지 않는 채널 입니다."));
        String managerEmail = channel.getManager().getEmail();

        //관리자 아이디 확인
        if(!managerEmail.equals(requestEmail)){
            throw new RemoveChannelFailedException("수정 권한이 없습니다. " + managerEmail);
        }

        //구독자 해제
        for (UserChannel userChannel: channel.getMemberList()) {
            userChannel.setChannel(null);
            userChannel.setUser(null);
        }

        //삭제
        channelRepository.delete(channel);

        ChannelResponseDto channelResponseDto = new ChannelResponseDto(channel);

        return channelResponseDto;
    }

    public Long findChannel(String name){
        Channel channel = channelRepository.findByName(name).orElseThrow(()->new ChannelFindFailedException("존재하지 않는 채널 입니다."));

        return channel.getId();
    }

    public ChannelResponseDto findChannel(Long channelId){

        Channel channel = channelRepository.findByIdWithManager(channelId).orElseThrow(()->new ChannelFindFailedException("존재하지 않는 채널 입니다."));

        ChannelResponseDto channelResponseDto = new ChannelResponseDto(channel);

        return channelResponseDto;
    }

}
