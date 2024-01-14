package com.example.makeboard0629.controller;

import com.example.makeboard0629.dto.SignInDto;
import com.example.makeboard0629.dto.SignUpDto;
import com.example.makeboard0629.dto.board.BoardDto;
import com.example.makeboard0629.entity.User;
import com.example.makeboard0629.jwt.TokenProvider;
import com.example.makeboard0629.service.JwtService;
import com.example.makeboard0629.service.MemberService;
import com.example.makeboard0629.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/dummy")
public class DummyController {

    private final MemberService memberService;
    private final BoardService boardService;
    private final TokenProvider tokenProvider;
    private final JwtService jwtService;

    @PostMapping("/")
    public ResponseEntity<?> testuser() {
        SignUpDto signUpDto = SignUpDto.builder()
                .email("dummyTest@email.com")
                .password("dummyPassword")
                .build();
        var result= memberService.register(signUpDto);
        String[] hashTag = {"#dummy1", "#dummy2","#dummy3", "#dummy4"};
        BoardDto boardDto = BoardDto.builder()
                .title("dummyTitle")
                .content("dummyContentdummyContentdummyContentdummyContentdummyContentdummyContentdummyContentdummyContent")
                .hashTag(hashTag)
                .gradation("dummyGradation")
                .season("dummySeason")
                .weather("dummyWeather")
                .country("dummyCountry")
                .timeZone("dummyTimezone")
                .build();

        boardService.saveBoard(boardDto, "dummyTest@email.com");


        var token = tokenProvider.generateToken(result.getEmail(), result.getUserRole());
        SignInDto signInDto = SignInDto.builder()
                .email(result.getEmail())
                .password(result.getPassword())
                .build();
        jwtService.login(token, signInDto);


        return ResponseEntity.ok(token);
    }


}
