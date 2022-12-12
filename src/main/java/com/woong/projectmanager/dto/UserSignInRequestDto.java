package com.woong.projectmanager.dto;

import com.woong.projectmanager.provider.LoginProviderType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class UserSignInRequestDto {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
