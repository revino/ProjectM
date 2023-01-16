package com.woong.projectmanager.domain;

import com.sun.istack.NotNull;
import com.woong.projectmanager.dto.request.ItemAddRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @NoArgsConstructor @AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Users writer;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemList")
    private Channel channel;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contents> contentsList = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<AlarmUserItem> alarmUserList = new ArrayList<>();

    @NotNull
    private String name;

    @NotNull
    private String status;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    public void addContents(Contents contents){
        contentsList.add(contents);
        contents.setItem(this);
    }

    public void update(ItemAddRequestDto itemAddRequestDto){
        this.name = itemAddRequestDto.getName();
        this.status = itemAddRequestDto.getStatus();
        this.startDate = itemAddRequestDto.getStartDate();
        this.endDate = itemAddRequestDto.getEndDate();
    }


    public void setWriter(Users writer) {
        this.writer = writer;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
