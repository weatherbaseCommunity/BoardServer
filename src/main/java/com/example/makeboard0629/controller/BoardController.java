package com.example.makeboard0629.controller;

import com.example.makeboard0629.dto.board.BoardDto;
import com.example.makeboard0629.dto.board.BoardsDto;
import com.example.makeboard0629.entity.User;
import com.example.makeboard0629.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/saveBoard")
    public ResponseEntity<?> saveBoard(@AuthenticationPrincipal User user, @RequestBody BoardDto boardDto) {

        boardService.saveBoard(boardDto, user.getEmail());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/getMyBoardList")
    public ResponseEntity<List<BoardsDto>> getBoardList(@AuthenticationPrincipal User user) {
        List<BoardsDto> boardsDtoList = boardService.getMyBoards(user.getEmail());
        return ResponseEntity.ok(boardsDtoList);
    }
    @GetMapping("/getAllBoards")
    public ResponseEntity<List<BoardsDto>> getAllBoards(){
        List<BoardsDto> boardsDtoList = boardService.getAllBoards();
        return ResponseEntity.ok(boardsDtoList);
    }






}