package com.example.makeboard0629.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
