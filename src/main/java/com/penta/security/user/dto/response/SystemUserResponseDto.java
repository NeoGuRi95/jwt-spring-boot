package com.penta.security.user.dto.response;

import com.penta.security.user.entity.SystemUser;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원 응답 DTO
 */
@Getter
@Setter
public class SystemUserResponseDto {

    private Integer userIdx;

    private String userId;

    private String userNm;

    private String userAuth;

    public SystemUserResponseDto(SystemUser systemUser) {
        this.userIdx = systemUser.getUserIdx();
        this.userId = systemUser.getUserId();
        this.userNm = systemUser.getUserNm();
        this.userAuth = systemUser.getUserAuth();
    }
}
