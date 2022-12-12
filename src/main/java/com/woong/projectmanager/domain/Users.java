package com.woong.projectmanager.domain;

import com.sun.istack.NotNull;
import com.woong.projectmanager.provider.LoginProviderType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter @NoArgsConstructor @AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Column(unique = true, length = 250)
    private String email;

    @NotNull
    @Column(length = 100)
    private String password;

    @NotNull
    @Column(length = 10)
    private String nickName;

    @Column()
    private String picture;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserChannel> channelList = new ArrayList<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column
    private RoleType role;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private LoginProviderType loginProviderType;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;

    public void addChannel(UserChannel userChannel){
        channelList.add(userChannel);
        userChannel.setUser(this);
    }

    public void removeChannel(UserChannel userChannel){
        channelList.remove(userChannel);
        userChannel.setChannel(null);
        userChannel.setUser(null);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public void setLoginProviderType(LoginProviderType loginProviderType) {
        this.loginProviderType = loginProviderType;
    }
}