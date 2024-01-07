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
    private int commentCnt;
    private int lickCnt;
    private String weatherUrl;
    private String hashTag;

    public BoardsDto(Board board) {
        this.id =board.getId();
        this.title = board.getTitle();
        this.commentCnt = board.getCommentCnt();
        this.lickCnt = board.getLikeCnt();
        this.weatherUrl = board.getWeatherUrl();
        this.hashTag = board.getHashTag();
    }
}
