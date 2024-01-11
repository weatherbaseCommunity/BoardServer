package com.example.makeboard0629.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Builder
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private String content;
    private Long boardId;

    @ColumnDefault("")
    private Long commentId;
}
