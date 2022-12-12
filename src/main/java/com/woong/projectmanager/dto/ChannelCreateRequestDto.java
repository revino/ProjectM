package com.woong.projectmanager.dto;

import com.woong.projectmanager.domain.Channel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChannelCreateRequestDto {

    @NotEmpty
    private String name;

    public Channel toEntity() {
        Channel channel = new Channel();
        channel.setCreatedAt(LocalDateTime.now());
        channel.setName(this.name);
        return  channel;
    }
}
