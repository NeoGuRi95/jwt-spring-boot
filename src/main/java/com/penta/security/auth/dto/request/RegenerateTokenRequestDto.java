package com.penta.security.auth.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 토큰 갱신 요청 DTO
 */
@Getter
@Setter
public class RegenerateTokenRequestDto {

    @NotEmpty(message = "아이디는 필수항목입니다.")
    @Size(max=30)
    private String userId;

    @NotEmpty(message = "리프레쉬 토큰은 필수항목입니다.")
    private String refreshToken;
}
