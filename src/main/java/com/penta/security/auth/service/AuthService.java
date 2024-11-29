package com.penta.security.auth.service;

import com.penta.security.auth.dto.request.LoginRequestDto;
import com.penta.security.auth.dto.request.RegenerateTokenRequestDto;
import com.penta.security.auth.dto.response.LoginResponseDto;
import com.penta.security.auth.dto.response.TokenResponseDto;
import com.penta.security.auth.vo.CustomUserDetails;
import com.penta.security.auth.vo.RefreshTokenStore;
import com.penta.security.global.exception.PasswordIncorrectException;
import com.penta.security.global.exception.RefreshTokenInvalidException;
import com.penta.security.user.dto.response.SystemUserResponseDto;
import com.penta.security.user.entity.SystemUser;
import com.penta.security.auth.jwt.JwtProvider;
import com.penta.security.user.service.SystemUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 서비스
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final SystemUserService systemUserService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtProvider jwtProvider;

    private final RefreshTokenStore refreshTokenStore;

    /**
     * 로그인
     *
     * @param requestDto 로그인 요청 DTO
     * @return 토큰 응답 DTO
     * @throws RuntimeException 비밀번호가 일치하지 않는 경우 발생하는 예외
     */
    @Transactional
    public LoginResponseDto login(LoginRequestDto requestDto) throws RuntimeException {
        // 사용자 정보 조회
        SystemUser systemUser = systemUserService.getSystemUserEntity(requestDto.getUserId());

        // password 일치 여부 체크
        if (!bCryptPasswordEncoder.matches(requestDto.getUserPw(), systemUser.getUserPw())) {
            throw new PasswordIncorrectException("비밀번호가 일치하지 않습니다.");
        }

        // jwt 토큰 생성
        String userId = systemUser.getUserId();
        String accessToken = jwtProvider.generateAccessToken(userId);

        // 기존에 가지고 있는 사용자의 refresh token 제거
        refreshTokenStore.remove(userId);

        // refresh token 생성 후 저장
        String refreshToken = jwtProvider.generateRefreshToken(userId);
        refreshTokenStore.put(userId, refreshToken);

        LoginResponseDto responseDto = new LoginResponseDto();
        responseDto.setUserId(systemUser.getUserId());
        responseDto.setUserNm(systemUser.getUserNm());
        responseDto.setUserAuth(systemUser.getUserAuth());
        responseDto.setAccessToken(accessToken);
        responseDto.setRefreshToken(refreshToken);

        return responseDto;
    }

    /**
     * 로그인 유저 정보 조회
     *
     * @param authentication 인증 정보
     * @return 로그인 유저 정보 응답 DTO
     */
    @Transactional
    public SystemUserResponseDto getMe(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();
        SystemUser systemUser = systemUserService.getSystemUserEntity(userId);

        return new SystemUserResponseDto(systemUser);
    }

    /**
     * RTR 방식 토큰 갱신
     *
     * @param requestDto 토큰 갱신 요청 DTO
     * @return 토큰 응답 DTO
     * @throws RuntimeException 리프레쉬 토큰이 유효하지 않은 경우 발생하는 예외
     */
    @Transactional
    public TokenResponseDto regenerateToken(RegenerateTokenRequestDto requestDto) {
        String userId = requestDto.getUserId();

        // refresh token 유효성 검증
        String requestedRefreshToken = requestDto.getRefreshToken();
        if (!jwtProvider.validateToken(requestedRefreshToken)) {
            throw new RefreshTokenInvalidException("리프레쉬 토큰이 유효하지 않습니다.");
        }
        // 서버에 저장된 refresh token 동일한지 확인
        String savedRefreshToken = refreshTokenStore.get(userId);
        if (!savedRefreshToken.equals(requestedRefreshToken)) {
            throw new RefreshTokenInvalidException("리프레쉬 토큰이 유효하지 않습니다.");
        }

        // 새로운 access, refresh token 생성
        String newAccessToken = jwtProvider.generateAccessToken(userId);
        String newRefreshToken = jwtProvider.generateRefreshToken(userId);

        // 리프레쉬 토큰 갱신
        refreshTokenStore.put(userId, newRefreshToken);

        TokenResponseDto responseDto = new TokenResponseDto();
        responseDto.setAccessToken(newAccessToken);
        responseDto.setRefreshToken(newRefreshToken);

        return responseDto;
    }
}
