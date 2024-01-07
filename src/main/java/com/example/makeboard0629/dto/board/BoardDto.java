package com.example.makeboard0629.dto.board;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardDto {

    private String title;
    private String content;
    private String hashTag;
    private String weatherUrl;


}
