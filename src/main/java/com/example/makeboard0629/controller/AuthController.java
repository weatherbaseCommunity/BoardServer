package com.example.makeboard0629.controller;

import com.example.makeboard0629.dto.SignInDto;
import com.example.makeboard0629.dto.SignUpDto;
import com.example.makeboard0629.jwt.TokenProvider;
import com.example.makeboard0629.service.JwtService;
import com.example.makeboard0629.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;
    private final JwtService jwtService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> signup(@RequestBody SignUpDto signUpDto){
       var result = memberService.register(signUpDto);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SignInDto signInDto){
        var member = memberService.authenticate(signInDto);
        var token = tokenProvider.generateToken(member.getEmail(), member.getUserRole());

        jwtService.login(token, signInDto);


        return ResponseEntity.ok(token);
    }



}
