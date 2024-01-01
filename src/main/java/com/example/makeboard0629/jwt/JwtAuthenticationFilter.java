package com.example.makeboard0629.jwt;

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
        String accessToken = parseBearerToken(request, HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(accessToken) && this.tokenProvider.validateToken(accessToken)){
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        //todo : 토큰 만료 체크후 리프레시토큰 체크후 코드 발급 로직 필요
        // https://colabear754.tistory.com/179#%EB%A6%AC%ED%94%84%EB%A0%88%EC%8B%9C_%ED%86%A0%ED%81%B0%EC%9D%84_%ED%86%B5%ED%95%B4_%EC%95%A1%EC%84%B8%EC%8A%A4_%ED%86%A0%ED%81%B0%EC%9D%84_%EA%B0%B1%EC%8B%A0%ED%95%B4%EB%B3%B4%EC%9E%90!
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
            String refreshToken = parseBearerToken(request, "Refresh-Token");
            if (refreshToken == null) {
                throw exception;
            }
            String oldAccessToken = parseBearerToken(request, HttpHeaders.AUTHORIZATION);
            tokenProvider.validateRefreshToken(refreshToken, oldAccessToken);
            oldAccessToken.decode
            String newAccessToken = tokenProvider.recreationAccessToken(oldAccessToken);
            User user = parseUserSpecification(newAccessToken);
            AbstractAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(user, newAccessToken, user.getAuthorities());
            authenticated.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticated);

            response.setHeader("New-Access-Token", newAccessToken);
        } catch (Exception e) {
            request.setAttribute("exception", e);
        }
    }
}
