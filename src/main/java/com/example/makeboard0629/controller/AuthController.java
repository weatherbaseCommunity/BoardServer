package com.example.makeboard0629.controller;

import com.example.makeboard0629.dto.SignInDto;
import com.example.makeboard0629.dto.SignUpDto;
import com.example.makeboard0629.jwt.TokenProvider;
import com.example.makeboard0629.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthController {
    private final MemberService memberService;
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
        var token = tokenProvider.generateToken(member.getEmail(), member.getRoles());

        return ResponseEntity.ok(token);
    }

    @GetMapping("test/user")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<?> testuser(){
        return ResponseEntity.ok("testuser");
    }


    @GetMapping("test/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> testadmin(){
        return ResponseEntity.ok("testadmin");
    }
}
