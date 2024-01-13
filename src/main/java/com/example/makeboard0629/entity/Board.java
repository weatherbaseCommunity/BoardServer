package com.example.makeboard0629.entity;

import com.example.makeboard0629.dto.board.BoardDto;
import com.example.makeboard0629.dto.board.BoardUpdateDto;
import com.example.makeboard0629.dto.board.BoardsDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String gradation;
    private String season;
    private String weather;
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;      // 작성자

    @JsonIgnore
    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();       // 좋아요

    public void addLike(Like like){
        likes.add(like);
        like.setBoard(this);
    }

    @ColumnDefault("0")
    private Integer likeCnt;        // 좋아요 수

    @JsonIgnore
    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>(); // 댓글

    @ColumnDefault("0")
    private Integer commentCnt;     // 댓글 수


    public void changeLikeCnt(Integer likeCnt){
        this.likeCnt = likeCnt;
    }

    public void update(BoardUpdateDto boardUpdateDto){
        this.title = boardUpdateDto.getTitle();
        this.content = boardUpdateDto.getContent();
        String[] hashTag = boardUpdateDto.getHashTag();
        StringBuilder sb = new StringBuilder();
        for (String tag : hashTag){
            sb.append(tag).append(" ");
        }
        sb.deleteCharAt(sb.length()-1);
        this.hashTag = sb.toString();
    }

}
