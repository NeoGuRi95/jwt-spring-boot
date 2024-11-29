package com.penta.security.auth.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {

    private String userId;

    private String userNm;

    private String userAuth;

    private String accessToken;

    private String refreshToken;
}
