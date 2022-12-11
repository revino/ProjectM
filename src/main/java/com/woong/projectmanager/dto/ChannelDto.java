package com.woong.projectmanager.dto;

import com.sun.istack.NotNull;
import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.UserChannel;
import com.woong.projectmanager.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChannelDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String managerEmail;

    public Channel toEntity() {
        Channel channel = new Channel();
        channel.setCreatedAt(LocalDateTime.now());
        channel.setName(this.name);
        return  channel;
    }
}
