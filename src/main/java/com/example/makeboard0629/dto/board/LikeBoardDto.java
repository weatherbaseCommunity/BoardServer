package com.example.makeboard0629.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeBoardDto {

    private String uniqueId;
    private Long boardId;

}
