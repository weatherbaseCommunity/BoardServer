package com.example.makeboard0629.dto.board;

import com.example.makeboard0629.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BoardsDto {
    private Long id;
    private String title;
    private String content;
    private int commentCnt;
    private int lickCnt;
    private String[] hashTag;
    private String gradation;
    private String season;
    private String weather;
    private String country;

    public BoardsDto(Board board) {
        this.id =board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.commentCnt = board.getCommentCnt();
        this.lickCnt = board.getLikeCnt();
        this.gradation = board.getGradation();
        this.weather = board.getWeather();
        this.season = board.getSeason();
        this.country = board.getCountry();

        String hash = board.getHashTag();
        this.hashTag = hash.split(" ");
    }
}