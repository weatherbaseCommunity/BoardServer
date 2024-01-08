package com.example.makeboard0629.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {

    private String title;
    private String content;
    private String hashTag;
    private String weatherUrl;


}
