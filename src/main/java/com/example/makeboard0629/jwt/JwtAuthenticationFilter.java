package com.example.makeboard0629.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String accessToken = parseBearerToken(request, HttpHeaders.AUTHORIZATION);
            if (StringUtils.hasText(accessToken) && this.tokenProvider.validateToken(accessToken)){
                Authentication authentication = tokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }


        }catch (ExpiredJwtException e) {	// 변경
            log.info(String.format("exception : %s, message : 만료된 JWT 토큰입니다.", e.getClass().getName()));
            reissueAccessToken(request, response, e);
        } catch (Exception e) {
            request.setAttribute("exception", e);
        }
        filterChain.doFilter(request, response);
    }

    // ACCESS 토큰을 추출하는 함수
    private String resolveTokenRequest(HttpServletRequest request){
        String token = request.getHeader(TOKEN_HEADER);
        if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)){
            return token.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    private String parseBearerToken(HttpServletRequest request, String headerName) {
        return Optional.ofNullable(request.getHeader(headerName))
                .filter(token -> token.substring(0, 7).equalsIgnoreCase("Bearer "))
                .map(token -> token.substring(7))
                .orElse(null);
    }

    private void reissueAccessToken(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        try {
//
            String refreshToken =  parseBearerToken(request, "Refresh-Token");
            if (refreshToken == null) {
                throw exception;
            }
//            String oldAccessToken = parseBearerToken(request, HttpHeaders.AUTHORIZATION);
            String createdAccessToken = tokenProvider.validateRefreshToken(refreshToken);
            Authentication authentication = tokenProvider.getAuthentication(createdAccessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
//            oldAccessToken.decode
//            String newAccessToken = tokenProvider.recreationAccessToken(oldAccessToken);
//            User user = parseUserSpecification(newAccessToken);
//            AbstractAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(user, newAccessToken, user.getAuthorities());
//            authenticated.setDetails(new WebAuthenticationDetails(request));
//            SecurityContextHolder.getContext().setAuthentication(authenticated);

            response.setHeader("New-Access-Token", createdAccessToken);
        } catch (Exception e) {
            request.setAttribute("exception", e);
        }
    }
}
