package com.example.makeboard0629.jwt;

import com.example.makeboard0629.service.MemberService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Slf4j
@Component

public class TokenProvider {

    private static final String KEY_ROLES = "roles";
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; //1hour

    private final MemberService memberService;
    private Key key;



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

    public String generateToken(String email, List<String> roles) {
        //byte[] keyBytes = Decoders.BASE64.decode(secretKey); -> 500오류
        //key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.claims().setSubject(email);
        claims.put(KEY_ROLES, roles);



        var now = new Date();
        var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Authentication getAuthentication(String jwt) {
        UserDetails userDetails = memberService.loadUserByUsername(this.getMemberEmail(jwt));
        log.info("userDetails.getPassword() : " + userDetails.getPassword());;
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getMemberEmail(String token) {
        return this.parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info(String.format("exception : %s, message : 잘못된 JWT 서명입니다.", e.getClass().getName()));
        } catch (ExpiredJwtException e) {
            log.info(String.format("exception : %s, message : 만료된 JWT 토큰입니다.", e.getClass().getName()));
        } catch (UnsupportedJwtException e) {
            log.info(String.format("exception : %s, message : 지원되지 않는 JWT 토큰입니다.", e.getClass().getName()));
        } catch (IllegalArgumentException e) {
            log.info(String.format("exception : %s, message : JWT 토큰이 잘못되었습니다.", e.getClass().getName()));
        }
        return false;


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