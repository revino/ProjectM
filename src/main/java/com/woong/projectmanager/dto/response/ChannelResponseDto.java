package com.woong.projectmanager.dto.response;

import com.sun.istack.NotNull;
import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.Item;
import com.woong.projectmanager.domain.UserChannel;
import com.woong.projectmanager.domain.Users;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor
public class ChannelResponseDto {

    @NotEmpty
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String managerEmail;

    @NotEmpty
    private LocalDateTime createdAt;

    public ChannelResponseDto(Channel channel){
        this.id = channel.getId();
        this.name = channel.getName();
        this.managerEmail = channel.getManager().getEmail();
        this.createdAt = channel.getCreatedAt();
    }


}
