package com.woong.projectmanager.service;

import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.UserChannel;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.dto.ChannelDto;
import com.woong.projectmanager.dto.UserDto;
import com.woong.projectmanager.repository.ChannelRepository;
import com.woong.projectmanager.repository.UserChannelRepository;
import com.woong.projectmanager.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UsersRepository usersRepository;

    private final ChannelRepository channelRepository;

    private final UserChannelRepository userChannelRepository;

    @Transactional
    public Users signUp(UserDto userDto){

        return usersRepository.save(userDto.toEntity());
    }

    @Transactional
    public void addChannel(String email, Long channelId){
        Users member = usersRepository.findByEmail(email).orElseThrow();
        Channel channel = channelRepository.findById(channelId).orElseThrow();

        //유저채널 생성
        UserChannel userChannel = UserChannel.createUserChannel(member, channel);

        //채널 추가
        member.addChannel(userChannel);
    }

    @Transactional
    public void removeChannel(String email, Long channelId){
        Users member = usersRepository.findByEmail(email).orElseThrow();
        Channel channel = channelRepository.findById(channelId).orElseThrow();

        //유저채널 찾기
        UserChannel userChannel = userChannelRepository.findByUserAndChannel(member, channel).orElseThrow();

        //채널 제거
        member.removeChannel(userChannel);
        usersRepository.save(member);
    }

    public Users findUserEmail(String email){
        Users user = usersRepository.findByEmail(email).orElseThrow();

        return user;
    }

    public List<ChannelDto> getChannelList(String email){
        Users user = usersRepository.findByEmail(email).orElseThrow();

        //Dto 생성하여 반환
        List<ChannelDto> channelDtoList = new ArrayList<>();

        for(var el : user.getChannelList()){
            ChannelDto channelDto = new ChannelDto();
            Channel channel = el.getChannel();
            channelDto.setManagerEmail(channel.getManager().getEmail());
            channelDto.setName(channel.getName());
            channelDtoList.add(channelDto);
        }

        return channelDtoList;
    }
}
