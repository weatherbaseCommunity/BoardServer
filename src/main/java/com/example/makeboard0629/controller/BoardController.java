package com.example.makeboard0629.controller;

import com.example.makeboard0629.dto.board.BoardDto;
import com.example.makeboard0629.dto.board.BoardsDto;
import com.example.makeboard0629.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
//    private final LikeService likeService;

    @PostMapping("/save")
    public ResponseEntity<?> saveBoard(Authentication authentication, @RequestBody BoardDto boardDto) {

        boardService.saveBoard(boardDto, uniqueId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/getMyBoardList")
    public ResponseEntity<List<BoardsDto>> getBoardList(Authentication authentication) {
        List<BoardsDto> boardsDtoList = boardService.getMyBoards(uniqueId);
        return ResponseEntity.ok(boardsDtoList);
    }
    @GetMapping("/getAllBoards")
    public ResponseEntity<List<BoardsDto>> getAllBoards(){
        List<BoardsDto> boardsDtoList = boardService.getAllBoards();
        return ResponseEntity.ok(boardsDtoList);
    }
}
