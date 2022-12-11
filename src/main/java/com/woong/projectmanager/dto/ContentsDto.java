package com.woong.projectmanager.dto;

import com.woong.projectmanager.domain.Contents;
import com.woong.projectmanager.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContentsDto {

    @NotEmpty
    private String writerEmail;

    @NotEmpty
    private Long itemId;

    @NotEmpty
    private String contents;

}
