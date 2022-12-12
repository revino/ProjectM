package com.woong.projectmanager.dto;

import com.woong.projectmanager.domain.RoleType;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.provider.LoginProviderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpRequestDto {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String nickName;

    public Users toEntity(){

        Users user = new Users();

        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setNickName(this.nickName);
        user.setLoginProviderType(LoginProviderType.LOCAL);
        user.setRole(RoleType.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return user;
    }

}
