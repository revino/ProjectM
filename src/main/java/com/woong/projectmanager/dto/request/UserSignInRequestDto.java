package com.woong.projectmanager.dto.request;

import com.woong.projectmanager.domain.RoleType;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.provider.LoginProviderType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserSignInRequestDto {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

}
