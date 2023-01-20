package com.woong.projectmanager.dto.response;

import com.woong.projectmanager.domain.AlarmUserItem;
import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.Item;
import com.woong.projectmanager.domain.UserChannel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data @NoArgsConstructor
public class ItemResponseDto {

    @NotEmpty
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String writerEmail;

    @NotEmpty
    private Long channelId;

    @NotEmpty
    private String channelName;

    @NotEmpty
    private String status;

    @NotEmpty
    private LocalDate startDate;

    @NotEmpty
    private LocalDate endDate;

    private boolean isSubscribe = false;

    public ItemResponseDto(Item item){
        this.id = item.getId();
        this.name = item.getName();
        this.writerEmail = item.getWriter().getEmail();
        this.channelId = item.getChannel().getId();
        this.channelName = item.getChannel().getName();
        this.status = item.getStatus();
        this.startDate = item.getStartDate();
        this.endDate = item.getEndDate();
    }

    public ItemResponseDto(Item item, boolean isSubscribe){
        this.id = item.getId();
        this.name = item.getName();
        this.writerEmail = item.getWriter().getEmail();
        this.channelId = item.getChannel().getId();
        this.channelName = item.getChannel().getName();
        this.status = item.getStatus();
        this.startDate = item.getStartDate();
        this.endDate = item.getEndDate();
        this.isSubscribe = isSubscribe;
    }

    public ItemResponseDto(AlarmUserItem alarmUserItem){
        Item item = alarmUserItem.getItem();

        this.id = item.getId();
        this.name = item.getName();
        this.id = item.getId();
        this.name = item.getName();
        this.writerEmail = item.getWriter().getEmail();
        this.channelId = item.getChannel().getId();
        this.channelName = item.getChannel().getName();
        this.status = item.getStatus();
        this.startDate = item.getStartDate();
        this.endDate = item.getEndDate();
    }


}
