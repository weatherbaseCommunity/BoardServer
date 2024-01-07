package com.example.makeboard0629.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Builder
@Getter
public class Board extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String hashTag;
    private String weatherUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;      // 작성자

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();       // 좋아요

    @ColumnDefault("0")
    private Integer likeCnt;        // 좋아요 수

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>(); // 댓글
    @ColumnDefault("0")
    private Integer commentCnt;     // 댓글 수

    public void addLike(Like like){
        likes.add(like);
        like.setBoard(this);
    }
    public void changeLikeCnt(Integer likeCnt){
        this.likeCnt = likeCnt;
    }
    public void updateBoard(String title, String content){
        this.title = title;
        this.content = content;
    }

}
