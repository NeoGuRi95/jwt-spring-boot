package com.penta.security.auth.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 토큰 응답 DTO
 */
@Getter
@Setter
public class TokenResponseDto {

    private String accessToken;

    private String refreshToken;
}
