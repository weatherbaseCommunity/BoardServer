package com.example.makeboard0629.dto.board;

import com.example.makeboard0629.entity.Board;
import com.example.makeboard0629.type.Authority;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyInfoDto {
    //private String uniqueId;
    private String nickName;

    @Enumerated(EnumType.STRING)
    private Authority userRole;

    private List<Board> boards;
//    private List<Like> likes;
//    private List<Comment> comments;
}