package com.penta.security.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원 생성 요청 DTO
 */
@Getter
@Setter
public class SystemUserCreateRequestDto {

    @NotEmpty(message = "아이디는 필수항목입니다.")
    @Size(max=30)
    public String userId;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    @Size(max=100)
    public String userPw;

    @NotEmpty(message = "이름은 필수항목입니다.")
    @Size(max=100)
    public String userNm;
}
