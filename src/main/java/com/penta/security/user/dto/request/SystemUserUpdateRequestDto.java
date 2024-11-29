package com.penta.security.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원 수정 요청 DTO
 */
@Getter
@Setter
public class SystemUserUpdateRequestDto {

    @NotEmpty(message = "이름은 필수항목입니다.")
    @Size(max=100)
    public String userNm;
}
