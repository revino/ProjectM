package com.woong.projectmanager.domain;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name ="ACCOUNT_REFRESH_TOKEN")
public class UserRefreshToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 64, unique = true)
    private String userId;

    @NotNull
    @Column(length = 256)
    private String refreshToken;

}
