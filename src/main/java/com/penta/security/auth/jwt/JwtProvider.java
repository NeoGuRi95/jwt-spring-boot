package com.penta.security.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * JWT Provider
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    // Access Token 만료 시간 1시간
    private static final long ACCESS_TOKEN_VALID_TIME = (long) 1000 * 60 * 60;
    // Refresh Token 만료 시간 24시간
    private static final long REFRESH_TOKEN_VALID_TIME = (long) 1000 * 60 * 60 * 24;

    @Value("${spring.jwt.secret}")
    private String secret;

    private SecretKey key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * token Username 조회
     *
     * @param token JWT
     * @return token Username
     */
    public String getUserIdFromToken(final String token) {
        return getClaimFromToken(token, Claims::getId);
    }

    /**
     * 토큰 만료 일자 조회
     *
     * @param token JWT
     * @return 만료 일자
     */
    public Date getExpirationDateFromToken(final String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * token 사용자 속성 정보 조회
     *
     * @param token          JWT
     * @param claimsResolver Get Function With Target Claim
     * @param <T>            Target Claim
     * @return 사용자 속성 정보
     */
    public <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver) {
        // token 유효성 검증
        if (Boolean.FALSE.equals(validateToken(token))) {
            return null;
        }

        final Claims claims = getAllClaimsFromToken(token);

        return claimsResolver.apply(claims);
    }

    /**
     * token 사용자 모든 속성 정보 조회
     *
     * @param token JWT
     * @return All Claims
     */
    private Claims getAllClaimsFromToken(final String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    /**
     * JWT access token 생성
     *
     * @param userId token 생성 userId
     * @return access token
     */
    public String generateAccessToken(final String userId) {
        return Jwts.builder()
            .id(userId)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALID_TIME)) // 1시간
            .signWith(key)
            .compact();
    }

    /**
     * JWT refresh token 생성
     *
     * @param userId refresh token 생성 userId
     * @return refresh token
     */
    public String generateRefreshToken(final String userId) {
        return Jwts.builder()
            .id(userId)
            .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_TIME)) // 24시간
            .issuedAt(new Date(System.currentTimeMillis()))
            .signWith(key)
            .compact();
    }

    /**
     * token 검증
     *
     * @param token JWT
     * @return token 검증 결과
     */
    public Boolean validateToken(final String token) {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (SecurityException e) {
            log.warn("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

}