package com.example.makeboard0629.dto.board;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeBoardDto {

    private String uniqueId;
    private Long boardId;

}
