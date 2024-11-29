package com.penta.security.auth.config;

import com.penta.security.auth.service.CustomUserDetailsService;
import com.penta.security.auth.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 헤더에 있는 Jwt 토큰을 검증, DB에 해당 회원이 있는지 확인 후 Security Context 인증 정보를 설정하는 필터
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private final CustomUserDetailsService customUserDetailsService;

    /**
     * 헤더에 있는 Jwt 토큰을 검증, DB에 해당 회원이 있는지 확인 후 Security Context에 인증 정보를 설정
     *
     * @param request     요청
     * @param response    응답
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 예외
     * @throws IOException      IO 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            // JWT 유효성 검증
            if (jwtProvider.validateToken(token)) {
                String userId = jwtProvider.getUserIdFromToken(token);
                // DB에 해당 아이디를 가진 유저가 있는지 확인
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);

                if (userDetails != null) {
                    // UserDetails, Password, Role -> 접근권한 인증 Token 생성
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());

                    // Security Context 접근 권한 설정
                    SecurityContextHolder.getContext()
                        .setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }

        filterChain.doFilter(request, response); // 다음 필터로 넘기기
    }
}