package com.woong.projectmanager.dto;

import com.woong.projectmanager.domain.Users;
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
public class UserDto {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String nickName;

    public UserDto(Users users){
        this.email = users.getEmail();
        this.nickName = users.getNickName();
    }

    public Users toEntity(){

        Users user = new Users();

        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setNickName(this.nickName);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return user;
    }

}
