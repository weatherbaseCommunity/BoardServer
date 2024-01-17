package com.example.makeboard0629.controller;

import com.example.makeboard0629.dto.board.CommentDto;
import com.example.makeboard0629.entity.Comment;
import com.example.makeboard0629.entity.User;
import com.example.makeboard0629.service.board.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/save")
    public Comment addComments(@AuthenticationPrincipal User user, @RequestBody CommentDto commentDto) {
        return commentService.writeComment(user, commentDto);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editComment(@AuthenticationPrincipal User user, @RequestBody CommentDto commentDto) {
        var flag  = commentService.editComment(user, commentDto);

        if (flag!= null) return ResponseEntity.ok("수정되었습니다.");
        else return ResponseEntity.status(400).body("오류 수정되지 않았습니다..");
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal User user, @RequestBody Long commentId) {
        boolean flag = commentService.deleteComment(user, commentId);
        if (flag) return ResponseEntity.ok("삭제되었습니다.");
        else return ResponseEntity.status(403).body("댓글 주인만 삭제할수 있습니다.");

    }
}