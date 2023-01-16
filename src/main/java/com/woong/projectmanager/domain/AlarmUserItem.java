package com.woong.projectmanager.domain;

import lombok.*;

import javax.persistence.*;
@Entity @Builder @Getter
@AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmUserItem {

    @Id
    @GeneratedValue
    @Column(name = "alarm_user_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    //생성 메소드
    public static AlarmUserItem createAlarmUserItem(Users member, Item item){
        AlarmUserItem alarmUserItem = new AlarmUserItem();

        //userChannel.setUser(member);
        alarmUserItem.setItem(item);

        return alarmUserItem;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
