package com.woong.projectmanager.dto.response;

import com.woong.projectmanager.domain.Contents;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @NoArgsConstructor
public class ContentsResponseDto {

    private Long id;

    @NotEmpty
    private String writerEmail;

    @NotEmpty
    private Long itemId;

    @NotEmpty
    private String contents;

    @NotEmpty
    private LocalDateTime createdAt;

    public ContentsResponseDto(Contents contents){
        this.id = contents.getId();
        this.writerEmail = contents.getWriter().getEmail();
        this.itemId = contents.getItem().getId();
        this.contents = contents.getContents();
        this.createdAt = contents.getCreatedAt();
    }
}
