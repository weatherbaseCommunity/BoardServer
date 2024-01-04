package com.example.makeboard0629.controller;

import com.example.makeboard0629.dto.CodeDto;
import com.example.makeboard0629.dto.SignInDto;
import com.example.makeboard0629.dto.SignUpDto;
import com.example.makeboard0629.entity.Member;
import com.example.makeboard0629.jwt.TokenProvider;
import com.example.makeboard0629.service.JwtService;
import com.example.makeboard0629.service.MemberService;
import com.example.makeboard0629.service.Oauth2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
//@RequestMapping("/test")
public class TestController {
    private final Oauth2Service oauth2Service;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final JwtService jwtService;

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

    @PostMapping("/login/oauth2/code/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody CodeDto codeDto) {
        String code = codeDto.getCode();
        SignUpDto signUpDto = oauth2Service.getKakaoToken(code);
        Member member = memberService.oauth2Register(signUpDto);
        var token = tokenProvider.generateToken(member.getEmail(), member.getUserRole());
        SignInDto signInDto = SignInDto.builder()
                .email(signUpDto.getEmail())
                .password(signUpDto.getPassword())
                .build();
        jwtService.login(token, signInDto);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/login/naver")
    public ResponseEntity<?> naverLogin(@RequestBody String code) {

        SignUpDto signUpDto = oauth2Service.getNaverToken(code);

        return ResponseEntity.ok().build();
    }


}
