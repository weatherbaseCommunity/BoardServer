package com.example.makeboard0629.jwt;

import com.example.makeboard0629.dto.TokenDto;
import com.example.makeboard0629.service.MemberService;
import com.example.makeboard0629.type.Authority;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component

public class TokenProvider {

    private static final String KEY_ROLES = "roles";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60; //1hour
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14; //14days

    private final MemberService memberService;
    private final Key key;

    public TokenProvider(@Value("${jwt.secret}") String secretKey,
                         final MemberService userService) {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        key = Keys.hmacShaKeyFor(keyBytes);

        this.memberService = userService;
    }


    /**
     * 토큰 생성(발급)
     *
     * @param email
     * @param roles
     * @return
     */

    public TokenDto generateToken(String email, Authority roles) {
        //byte[] keyBytes = Decoders.BASE64.decode(secretKey); -> 500오류
        //key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.claims().setSubject(email);
        claims.put(KEY_ROLES, roles);


        var now = new Date();
        var expiredDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        String refreshToken = Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME)) // set Expire Time
                .signWith(key, SignatureAlgorithm.HS512)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();

        return TokenDto.builder().accessToken(accessToken).refreshToken(refreshToken).key("key").build();
    }

    //Refresh Token을 받아 유효성 검증을 하는 메소드이다.
    public String recreationAccessToken(String userEmail, Object roles){

        Claims claims = Jwts.claims().setSubject(userEmail); // JWT payload 에 저장되는 정보단위
        claims.put("roles", roles); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();

        //Access Token
        String accessToken = Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, key)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();

        return accessToken;
    }


    public Authentication getAuthentication(String jwt) {
        UserDetails userDetails = memberService.loadUserByUsername(this.getMemberEmail(jwt));
        log.info("userDetails.getPassword() : " + userDetails.getPassword());
        ;
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getMemberEmail(String token) {
        return this.parseClaims(token).getSubject();
    }
    public String getUserPk(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info(String.format("exception : %s, message : 잘못된 JWT 서명입니다.", e.getClass().getName()));
        }  catch (UnsupportedJwtException e) {
            log.info(String.format("exception : %s, message : 지원되지 않는 JWT 토큰입니다.", e.getClass().getName()));
        } catch (IllegalArgumentException e) {
            log.info(String.format("exception : %s, message : JWT 토큰이 잘못되었습니다.", e.getClass().getName()));
        }
        return false;


    }

    public String validateRefreshToken(String refreshToken){


        // refresh 객체에서 refreshToken 추출
//        String refreshToken = refreshTokenObj.getRefreshToken();


        try {
            // 검증
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken);

            //refresh 토큰의 만료시간이 지나지 않았을 경우, 새로운 access 토큰을 생성합니다.
            if (!claims.getBody().getExpiration().before(new Date())) {
                return recreationAccessToken(claims.getBody().get("sub").toString(), claims.getBody().get("roles"));
            }
        }catch (Exception e) {

            //refresh 토큰이 만료되었을 경우, 로그인이 필요합니다.
            return null;

        }

        return null;
    }

    private Claims parseClaims(String token) {
        //key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        try {
            return Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }


    }
}