package com.penta.security;

import com.penta.security.user.dto.request.SystemUserCreateRequestDto;
import com.penta.security.user.dto.response.SystemUserResponseDto;
import com.penta.security.user.service.SystemUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class SecurityApplicationTests {

	@Autowired
	private SystemUserService systemUserService;

	@Test
	@DisplayName("회원 삽입 기능 테스트")
	void systemUserCreateTest() {
		// given
		SystemUserCreateRequestDto requestDto = new SystemUserCreateRequestDto();
		requestDto.setUserId("test_user_id");
		requestDto.setUserPw("test_user_pw");
		requestDto.setUserNm("test_user_nm");

		// when
		SystemUserResponseDto responseDto = systemUserService.create(requestDto);

		// then
		String actualUserAuth = responseDto.getUserAuth();
		assertThat(actualUserAuth).isEqualTo("SYSTEM_USER");
	}

}
