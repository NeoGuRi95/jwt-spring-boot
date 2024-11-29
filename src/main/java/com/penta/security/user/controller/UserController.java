package com.penta.security.user.controller;

import com.penta.security.user.dto.request.SystemUserCreateRequestDto;
import com.penta.security.user.dto.request.SystemUserUpdateRequestDto;
import com.penta.security.user.dto.response.SystemUserResponseDto;
import com.penta.security.user.service.SystemUserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 관리 API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final SystemUserService systemUserService;

    /**
     * 회원 조회
     *
     * @param userId 회원 ID
     * @return 회원 정보
     */
    @Secured("SYSTEM_ADMIN")
    @GetMapping("/{userId}")
    public ResponseEntity<SystemUserResponseDto> get(@PathVariable String userId) {
        SystemUserResponseDto responseDto = systemUserService.get(userId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 회원 목록 조회
     *
     * @param page       페이지 번호
     * @param searchWord 검색어
     * @return 회원 목록
     */
    @Secured("SYSTEM_ADMIN")
    @GetMapping
    public ResponseEntity<Page<SystemUserResponseDto>> getUsers(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "searchWord", defaultValue = "") String searchWord) {
        Page<SystemUserResponseDto> responseDtoPage =
            systemUserService.getSystemUserList(page, searchWord);
        return ResponseEntity.ok(responseDtoPage);
    }

    /**
     * 회원 생성
     *
     * @param requestDto 회원 생성 요청 정보
     * @return 생성된 회원 정보
     */
    @PostMapping
    public ResponseEntity<SystemUserResponseDto> create(
        @RequestBody @Valid SystemUserCreateRequestDto requestDto) {
        SystemUserResponseDto responseDto = systemUserService.create(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 회원 정보 수정
     *
     * @param userId     회원 ID
     * @param requestDto 회원 수정 요청 정보
     * @return 수정된 회원 정보
     */
    @Secured("SYSTEM_ADMIN")
    @PutMapping
    public ResponseEntity<SystemUserResponseDto> update(
        @RequestParam String userId,
        @RequestBody @Valid SystemUserUpdateRequestDto requestDto) {
        SystemUserResponseDto responseDto = systemUserService.update(userId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 회원 삭제
     *
     * @param userId 회원 ID
     * @return 삭제된 회원 정보
     */
    @Secured("SYSTEM_ADMIN")
    @DeleteMapping
    public ResponseEntity<SystemUserResponseDto> delete(@RequestParam String userId) {
        SystemUserResponseDto responseDto = systemUserService.delete(userId);
        return ResponseEntity.ok(responseDto);
    }
}
