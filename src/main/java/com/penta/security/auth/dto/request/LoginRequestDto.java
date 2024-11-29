package com.penta.security.auth.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 요청 DTO
 */
@Getter
@Setter
public class LoginRequestDto {

    @NotEmpty(message = "아이디는 필수항목입니다.")
    @Size(max=30)
    private String userId;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    @Size(max=100)
    private String userPw;
}
