package com.penta.security.auth.controller;

import com.penta.security.auth.dto.request.LoginRequestDto;
import com.penta.security.auth.dto.request.RegenerateTokenRequestDto;
import com.penta.security.auth.dto.response.LoginResponseDto;
import com.penta.security.auth.dto.response.TokenResponseDto;
import com.penta.security.auth.service.AuthService;
import com.penta.security.user.dto.response.SystemUserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 로그인
     *
     * @param requestDto 로그인 요청 정보
     * @return 회원 정보 및 토큰 정보 응답
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
        LoginResponseDto responseDto = authService.login(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 로그인 유정 정보 조회
     *
     * @param authentication 인증 정보
     * @return 회원 정보 응답
     */
    @Secured({"SYSTEM_USER", "SYSTEM_ADMIN"})
    @GetMapping("/me")
    public ResponseEntity<SystemUserResponseDto> me(Authentication authentication) {
        SystemUserResponseDto responseDto = authService.getMe(authentication);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 토큰 갱신
     *
     * @param requestDto 토큰 갱신 요청 정보
     * @return 토큰 응답
     */
    @GetMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(
        @Valid @RequestBody RegenerateTokenRequestDto requestDto) {
        TokenResponseDto responseDto = authService.regenerateToken(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
