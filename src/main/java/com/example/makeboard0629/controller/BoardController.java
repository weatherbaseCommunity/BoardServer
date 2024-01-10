package com.example.makeboard0629.controller;

import com.example.makeboard0629.dto.board.BoardDto;
import com.example.makeboard0629.dto.board.BoardUpdateDto;
import com.example.makeboard0629.dto.board.BoardsDto;
import com.example.makeboard0629.entity.Board;
import com.example.makeboard0629.entity.User;
import com.example.makeboard0629.service.board.BoardService;
import com.example.makeboard0629.service.board.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final LikeService likeService;

    @PostMapping("/save")
    public ResponseEntity<?> saveBoard(@AuthenticationPrincipal User user, @RequestBody BoardDto boardDto) {
        boardService.saveBoard(boardDto, user.getEmail());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateBoard(@AuthenticationPrincipal User user, @RequestBody BoardUpdateDto BoardUpdateDto) {

        boardService.updateBoard(BoardUpdateDto, user.getEmail());
        return ResponseEntity.ok(HttpStatus.OK);
    }



    @GetMapping("/myBoard")
    public ResponseEntity<List<BoardsDto>> getBoardList(@AuthenticationPrincipal User user) {
        List<BoardsDto> boardsDtoList = boardService.getMyBoards(user.getEmail());
        return ResponseEntity.ok(boardsDtoList);
    }
    @GetMapping("/AllBoards")
    public ResponseEntity<List<BoardsDto>> getAllBoards(){
        List<BoardsDto> boardsDtoList = boardService.getAllBoards();
        return ResponseEntity.ok(boardsDtoList);
    }

    @PostMapping("/increaseLike")
    public ResponseEntity<?> increaseLike(@AuthenticationPrincipal User user, @RequestBody Long boardId){

        Board board = boardService.findBoardById(boardId);
       likeService.addLike(board, user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/decreaseLike")
    public ResponseEntity<?> decreaseLike(@AuthenticationPrincipal User user, @RequestBody Long boardId){

        Board board = boardService.findBoardById(boardId);
        likeService.decreaseLike(board, user);
        return ResponseEntity.ok(user);
    }




}