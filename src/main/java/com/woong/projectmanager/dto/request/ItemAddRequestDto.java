package com.woong.projectmanager.dto.request;

import com.woong.projectmanager.domain.Item;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data @NoArgsConstructor
public class ItemAddRequestDto {

    @NotEmpty
    private String name;

    private Long channelId;

    @NotEmpty
    private String status;

    @NotNull
    private LocalDate startDate;

    @NotNull
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
