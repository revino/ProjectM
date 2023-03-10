package com.woong.projectmanager.dto.response;

import com.woong.projectmanager.domain.AlarmUserItem;
import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.UserChannel;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.provider.LoginProviderType;
import com.woong.projectmanager.util.ResponseUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor
public class UserResponseDto {

    @NotEmpty
    private String email;

    @NotEmpty
    private String nickName;

    private String picture;

    private LoginProviderType loginProviderType;

    private ChannelResponseDto currentChannel;

    private boolean isSlackWebHook;

    private String slackWebHookUrl;

    private List<ChannelResponseDto> subscribeList = new ArrayList<>();

    private List<ItemResponseDto> alarmItemList = new ArrayList<>();

    public UserResponseDto(Users users){
        Channel currentChannel = users.getCurrentChannel();
        this.email = users.getEmail();
        this.nickName = users.getNickName();
        this.picture = users.getPicture();
        this.loginProviderType = users.getLoginProviderType();

        //setting
        this.isSlackWebHook = users.isSlackWebHook();
        this.slackWebHookUrl = ResponseUtil.checkNull(users.getSlackWebHookUrl());

        for(UserChannel userChannel : users.getChannelList()){
            subscribeList.add(new ChannelResponseDto(userChannel.getChannel()));
        }

        if(currentChannel != null) {
            this.currentChannel = new ChannelResponseDto(currentChannel);
        }

        for(AlarmUserItem alarmUserItem : users.getAlarmItemList()){
            alarmItemList.add(new ItemResponseDto(alarmUserItem));
        }
    }

}
