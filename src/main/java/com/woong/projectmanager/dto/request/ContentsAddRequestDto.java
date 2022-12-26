package com.woong.projectmanager.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data @NoArgsConstructor
public class ContentsAddRequestDto {

    private Long itemId;

    @NotEmpty
    private String contents;
}
