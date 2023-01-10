package com.woong.projectmanager.domain;

import com.sun.istack.NotNull;
import com.woong.projectmanager.dto.request.ChannelCreateRequestDto;
import com.woong.projectmanager.dto.request.ItemAddRequestDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @NoArgsConstructor @AllArgsConstructor
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id")
    private Long id;

    @NotNull
    @Column(length = 30)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users manager;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL)
    private List<UserChannel> memberList = new ArrayList<>();

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> itemList = new ArrayList<>();

    @NotNull
    private LocalDateTime createdAt;

    public void addItem(Item item){
        item.setChannel(this);
        itemList.add(item);
    }

    public void setManager(Users manager) {
        this.manager = manager;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void update(ChannelCreateRequestDto channelCreateRequestDto){
        this.name = channelCreateRequestDto.getName();
    }
}
