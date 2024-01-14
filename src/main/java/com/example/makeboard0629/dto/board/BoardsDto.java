package com.example.makeboard0629.dto.board;

import com.example.makeboard0629.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class BoardsDto {
    private Long id;
    private String title;
    private String content;
    private int commentCnt;
    private int likeCnt;
    private String[] hashTag;
    private String gradation;
    private String season;
    private String weather;
    private String country;
    private String timeZone;
    private String nickname;
    private LocalDateTime createdTime;

    public BoardsDto(Board board) {
        this.id =board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.commentCnt = board.getCommentCnt();
        this.likeCnt = board.getLikeCnt();
        this.gradation = board.getGradation();
        this.weather = board.getWeather();
        this.season = board.getSeason();
        this.country = board.getCountry();
        this.timeZone = board.getTimeZone();
        this.nickname = board.getUser().getNickName();
        this.createdTime = board.getCreatedDate();
        String hash = board.getHashTag();
        this.hashTag = hash.split(" ");
    }
}