package com.woong.projectmanager.dto.response;

import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.UserChannel;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.provider.LoginProviderType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collections;
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

    private List<ChannelResponseDto> subscribeList = new ArrayList<>();

    public UserResponseDto(Users users){
        Channel currentChannel = users.getCurrentChannel();
        this.email = users.getEmail();
        this.nickName = users.getNickName();
        this.picture = users.getPicture();
        this.loginProviderType = users.getLoginProviderType();

        for(UserChannel userChannel : users.getChannelList()){
            subscribeList.add(new ChannelResponseDto(userChannel));
        }

        if(currentChannel != null) {
            this.currentChannel = new ChannelResponseDto(currentChannel);
        }
    }

}
