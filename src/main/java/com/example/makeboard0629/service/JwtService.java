package com.example.makeboard0629.service;

import com.example.makeboard0629.dto.SignInDto;
import com.example.makeboard0629.dto.TokenDto;
import com.example.makeboard0629.entity.RefreshToken;
import com.example.makeboard0629.jwt.TokenProvider;
import com.example.makeboard0629.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final TokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    @Transactional
    public void login(TokenDto token, SignInDto signInDto){
        String loginUserEmail = signInDto.getEmail();
//        Optional<RefreshToken> optionalRefreshToken = Optional.ofNullable(refreshTokenRepository
//                .findByKeyEmail(loginUserEmail).orElseThrow(() -> new NullPointerException("해당 이메일에 맞는 리스페시 토큰을 찾을수 없습니다.")));
//        RefreshToken refreshToken = optionalRefreshToken.get();
        if(refreshTokenRepository.existsByKeyEmail(loginUserEmail)){
            log.info("기존의 존재하는 refresh 토큰 삭제");
            refreshTokenRepository.deleteByKeyEmail(loginUserEmail);
        }
        RefreshToken refreshToken = RefreshToken.builder()
                .keyEmail(loginUserEmail)
                .refreshToken(token.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

    }

    public Optional<RefreshToken> getRefreshToken(String refreshToken){

        return refreshTokenRepository.findByRefreshToken(refreshToken);
    }

    public Map<String, String> validateRefreshToken(String refreshToken){
        RefreshToken refreshToken1 = getRefreshToken(refreshToken).get();
        String createdAccessToken = jwtTokenProvider.validateRefreshToken(refreshToken1);

        return createRefreshJson(createdAccessToken);
    }

    public Map<String, String> createRefreshJson(String createdAccessToken){

        Map<String, String> map = new HashMap<>();
        if(createdAccessToken == null){

            map.put("errortype", "Forbidden");
            map.put("status", "402");
            map.put("message", "Refresh 토큰이 만료되었습니다. 로그인이 필요합니다.");


            return map;
        }
        //기존에 존재하는 accessToken 제거


        map.put("status", "200");
        map.put("message", "Refresh 토큰을 통한 Access Token 생성이 완료되었습니다.");
        map.put("accessToken", createdAccessToken);

        return map;


    }


}