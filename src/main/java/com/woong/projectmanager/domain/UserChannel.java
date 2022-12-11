package com.woong.projectmanager.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserChannel {

    @Id
    @GeneratedValue
    @Column(name = "user_channel_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    //생성 메소드
    public static UserChannel createUserChannel(Users member, Channel channel){
        UserChannel userChannel = new UserChannel();

        //userChannel.setUser(member);
        userChannel.setChannel(channel);

        return userChannel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
