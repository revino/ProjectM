package com.woong.projectmanager.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Contents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contents_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Users writer;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private String contents;

    public static Contents createContents(Users writer, Item item, String contents){
        Contents content = new Contents();

        content.setWriter(writer);
        content.setContents(contents);
        content.setCreatedAt(LocalDateTime.now());

        //아이템에 컨텐츠 추가
        item.addContents(content);

        return content;
    }

    public void setWriter(Users writer) {
        this.writer = writer;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
