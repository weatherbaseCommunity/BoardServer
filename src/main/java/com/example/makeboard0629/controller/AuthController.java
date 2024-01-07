package com.example.makeboard0629.controller;

import com.example.makeboard0629.dto.oauth2.CodeDto;
import com.example.makeboard0629.dto.SignInDto;
import com.example.makeboard0629.dto.SignUpDto;
import com.example.makeboard0629.entity.Users;
import com.example.makeboard0629.jwt.TokenProvider;
import com.example.makeboard0629.service.JwtService;
import com.example.makeboard0629.service.MemberService;
import com.example.makeboard0629.service.Oauth2Service;
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
    private final Oauth2Service oauth2Service;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final JwtService jwtService;

    @PostMapping("/signup")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> signup(@RequestBody SignUpDto signUpDto){
       var result = memberService.register(signUpDto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login/oauth2/code/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody CodeDto codeDto) {
        String code = codeDto.getCode();
        SignUpDto signUpDto = oauth2Service.getKakaoToken(code);
        Users user = memberService.oauth2Register(signUpDto);
        var token = tokenProvider.generateToken(user.getEmail(), user.getUserRole());
        SignInDto signInDto = SignInDto.builder()
                .email(signUpDto.getEmail())
                .password(signUpDto.getPassword())
                .build();
        jwtService.login(token, signInDto);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/login/oauth2/code/naver")
    public ResponseEntity<?> naverLogin(@RequestBody CodeDto codeDto) {
        String code = codeDto.getCode();
        SignUpDto signUpDto = oauth2Service.getNaverToken(code);
        Users user = memberService.oauth2Register(signUpDto);
        var token = tokenProvider.generateToken(user.getEmail(), user.getUserRole());
        SignInDto signInDto = SignInDto.builder()
                .email(signUpDto.getEmail())
                .password(signUpDto.getPassword())
                .build();
        jwtService.login(token, signInDto);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/login/oauth2/code/google")
    public ResponseEntity<?> googleLogin(@RequestBody CodeDto codeDto) {
        String code = codeDto.getCode();
        SignUpDto signUpDto = oauth2Service.getGoogleToken(code);
        Users user = memberService.oauth2Register(signUpDto);
        var token = tokenProvider.generateToken(user.getEmail(), user.getUserRole());
        SignInDto signInDto = SignInDto.builder()
                .email(signUpDto.getEmail())
                .password(signUpDto.getPassword())
                .build();
        jwtService.login(token, signInDto);

        return ResponseEntity.ok().build();
    }


    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SignInDto signInDto){
        var member = memberService.authenticate(signInDto);
        var token = tokenProvider.generateToken(member.getEmail(), member.getUserRole());

        jwtService.login(token, signInDto);


        return ResponseEntity.ok(token);
    }



}
