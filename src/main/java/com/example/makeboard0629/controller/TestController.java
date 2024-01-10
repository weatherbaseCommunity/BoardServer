package com.example.makeboard0629.controller;

import com.example.makeboard0629.entity.Board;
import com.example.makeboard0629.entity.Comment;
import com.example.makeboard0629.entity.Like;
import com.example.makeboard0629.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
//@RequestMapping("/test")
public class TestController {
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<?> testuser() {
        return ResponseEntity.ok("testuser");
    }


    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> testadmin() {
        return ResponseEntity.ok("testadmin");
    }


    @GetMapping("/")
    public ResponseEntity<?> index() {
        System.out.println("index");
        return ResponseEntity.ok("testuser");
    }




}
