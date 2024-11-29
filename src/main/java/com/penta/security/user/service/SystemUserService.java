package com.penta.security.user.service;

import com.penta.security.global.exception.UserIdNotFoundException;
import com.penta.security.user.dto.request.SystemUserCreateRequestDto;
import com.penta.security.user.dto.request.SystemUserUpdateRequestDto;
import com.penta.security.user.dto.response.SystemUserResponseDto;
import com.penta.security.user.entity.SystemUser;
import com.penta.security.user.repository.SystemUserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 회원 서비스
 */
@Service
@RequiredArgsConstructor
public class SystemUserService {

    private final SystemUserRepository systemUserRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 회원 엔티티 조회
     *
     * @param userId 회원 아이디
     * @return 회원 엔티티
     */
    public SystemUser getSystemUserEntity(String userId) {
        Optional<SystemUser> opSystemUser = systemUserRepository.findByUserId(userId);
        return opSystemUser.orElseThrow(() -> new UserIdNotFoundException("해당하는 회원이 없습니다."));
    }

    /**
     * 회원 조회
     *
     * @param userId 회원 아이디
     * @return 회원 응답 DTO
     */
    public SystemUserResponseDto get(String userId) {
        SystemUser systemUser = getSystemUserEntity(userId);
        return new SystemUserResponseDto(systemUser);
    }

    /**
     * 회원 목록 조회
     *
     * @param searchWord 검색어
     * @return 회원 목록
     */
    public Page<SystemUserResponseDto> getSystemUserList(int page, String searchWord) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("userIdx"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        Page<SystemUser> systemUserPage = systemUserRepository.findAllBySearchWord(searchWord, pageable);

        return systemUserPage.map(SystemUserResponseDto::new);
    }

    /**
     * 회원 생성
     *
     * @param requestDto 회원 생성 요청 DTO
     * @return 회원 응답 DTO
     */
    public SystemUserResponseDto create(SystemUserCreateRequestDto requestDto) {
        SystemUser systemUser = SystemUser.builder()
            .userId(requestDto.getUserId())
            .userPw(bCryptPasswordEncoder.encode(requestDto.getUserPw()))
            .userNm(requestDto.getUserNm())
            .userAuth("SYSTEM_USER")
            .build();

        SystemUser createdSystemUser = systemUserRepository.save(systemUser);

        return new SystemUserResponseDto(createdSystemUser);
    }

    /**
     * 회원 조회
     *
     * @param userId     회원 아이디
     * @param requestDto 회원 수정 요청 DTO
     * @return 회원 응답 DTO
     */
    public SystemUserResponseDto update(String userId, SystemUserUpdateRequestDto requestDto)
        throws IllegalArgumentException {
        SystemUser systemUser = getSystemUserEntity(userId);

        systemUser.setUserNm(requestDto.getUserNm());
        SystemUser updatedSystemUser = systemUserRepository.save(systemUser);

        return new SystemUserResponseDto(updatedSystemUser);
    }

    /**
     * 회원 삭제
     *
     * @param userId 회원 아이디
     * @return 회원 응답 DTO
     */
    public SystemUserResponseDto delete(String userId) {
        SystemUser deletedSystemUser = getSystemUserEntity(userId);

        systemUserRepository.delete(deletedSystemUser);

        return new SystemUserResponseDto(deletedSystemUser);
    }
}
