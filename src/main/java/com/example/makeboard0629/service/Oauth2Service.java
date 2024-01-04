package com.example.makeboard0629.service;

import com.example.makeboard0629.dto.SignUpDto;
import com.example.makeboard0629.model.KakaoUser;
import com.example.makeboard0629.model.OAuth2Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class Oauth2Service {
    private final String client_secret;
    private final String clientId;
    private final String redirectUrl;
    private final String grant_type;

    public Oauth2Service(@Value("${spring.security.oauth2.client.registration.kakao.client-secret}") String secret,
                         @Value("${spring.security.oauth2.client.registration.kakao.client-id}") String clientId,
                         @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}") String redirectUrl,
                         @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}") String grant_type
    ) {
        this.client_secret = secret;
        this.clientId = clientId;
        this.redirectUrl = redirectUrl;
        this.grant_type = grant_type;
    }

    public SignUpDto getKakaoToken(String code) {
        String REQUEST_URL = "https://kauth.kakao.com/oauth/token";
// 카카오 API 서버에게 POST 방식으로 key=value 데이터를 요청
        // 요청 방법 -> 여러가지 라이브러리  : HttpsURLConnection, Retrofit2(주로 안드로이드), OkHttp, RestTemplate
        RestTemplate rt = new RestTemplate();

        // HttpHeader 객체 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); // key=value 형태의 데이터라는 것을 알려주는 부분

        // HttpBody 객체 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grant_type);
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUrl);
        params.add("code", code);
        params.add("client_secret", client_secret);

        // HttpHeader와 HttpBody를 하나의 객체에 담기 -> 만든 이유 : 아래의 exchange 함수에 HttpEntity를 넣어야 해서..
        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(params, headers); // body 데이터와 headers 값을 가지고 있는 Entity

        // 카카오에게 Http 요청하기 (POST 방식) -> response라는 변수에 응답을 받음
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                request,
                String.class
        );
        System.out.println(response);

        // 토큰값 Json 형식으로 가져오기위해 생성
//        JSONObject jsonObject = new  JSONObject(response.getBody());

        ObjectMapper objectMapper = new ObjectMapper();
        OAuth2Token oAuth2Token = new OAuth2Token();
        try {
            oAuth2Token = objectMapper.readValue(response.getBody(), OAuth2Token.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // 토큰결과값
        log.info("카카오 엑세스 토큰 : " + oAuth2Token.getAccess_token());

        RestTemplate rt2 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();

        headers2.add("Authorization", "Bearer "+ oAuth2Token.getAccess_token());
        headers2.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 =
                new HttpEntity<>(headers2);

        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest2,
                String.class
        );

        // 토큰을 사용하여 사용자 정보 추출
        System.out.println(response2);

        // 이후 유저 여부를 판단하고 회원가입 / 로그인 처리를 진행하면된다
        ObjectMapper objectMapper2 = new ObjectMapper();

        KakaoUser kakaoUser = null;
        try {
            kakaoUser = objectMapper2.readValue(response2.getBody(), KakaoUser.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        assert kakaoUser != null;
        System.out.println("카카오 아이디(번호) : " + kakaoUser.getId());
        System.out.println("카카오 이메일 : " + kakaoUser.getKakao_account().getEmail());

        return SignUpDto.builder()
                .email(kakaoUser.getKakao_account().getEmail()+kakaoUser.getId())
                .password(kakaoUser.getId()+kakaoUser.getKakao_account().getProfile().getNickname())
                .build();
    }


}
