package com.example.makeboard0629.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Comment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;      // 작성자

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;    // 댓글이 달린 게시판

    public void update(String newContent) {
        this.content = newContent;
    }
}
