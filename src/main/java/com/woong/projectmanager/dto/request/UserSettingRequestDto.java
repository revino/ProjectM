package com.woong.projectmanager.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UserSettingRequestDto {

    private Boolean isSlackWebHook;

    private String slackWebHookUrl;

}
