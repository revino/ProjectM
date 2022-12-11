package com.woong.projectmanager.dto;

import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String writerEmail;

    @NotEmpty
    private Long channelId;

    @NotEmpty
    private String status;

    @NotEmpty
    private LocalDate startDate;

    @NotEmpty
    private LocalDate endDate;

    public Item toEntity() {
        Item item = new Item();
        item.setName(this.name);
        item.setEndDate(this.endDate);
        item.setStartDate(this.startDate);
        item.setStatus(this.status);
        return  item;
    }
}
