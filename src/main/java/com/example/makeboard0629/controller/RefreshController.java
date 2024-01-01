package com.example.makeboard0629.controller;

import com.example.makeboard0629.jwt.TokenProvider;
import com.example.makeboard0629.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RefreshController {


    private final JwtService jwtService;

    @PostMapping("/refresh")
    public ResponseEntity<?> validateRefreshToken(@RequestBody HashMap<String, String> bodyJson){

        log.info("refresh controller 실행");
        Map<String, String> map = jwtService.validateRefreshToken(bodyJson.get("refreshToken"));

        if(map.get("status").equals("402")){
            log.info("RefreshController - Refresh Token이 만료.");

            return ResponseEntity.status(UNAUTHORIZED).body("RefreshController - Refresh Token이 만료.");
        }

        log.info("RefreshController - Refresh Token이 유효.");

        return ResponseEntity.status(HttpStatus.OK).body("RefreshController - Refresh Token이 유효.");

    }
}
