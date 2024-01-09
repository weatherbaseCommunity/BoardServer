package com.example.makeboard0629.controller;

import com.example.makeboard0629.dto.board.BoardDto;
import com.example.makeboard0629.entity.User;
import com.example.makeboard0629.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final MemberService memberService;
    @PostMapping("/mypage/update")
    public ResponseEntity<?> saveBoard(@AuthenticationPrincipal User user, @RequestBody String nickname) {
        System.out.println(user.getId());
        var result = memberService.updateNickname(user.getId(),nickname);
        return ResponseEntity.ok(result);
    }
}
