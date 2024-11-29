package com.penta.security.user.repository;

import com.penta.security.user.entity.SystemUser;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 회원 레포지토리
 */
public interface SystemUserRepository extends JpaRepository<SystemUser, Integer> {

    Optional<SystemUser> findByUserId(String userId);

    @Query("select "
        + "distinct u "
        + "from SystemUser u "
        + "where "
        + "   u.userId like %:searchWord% "
        + "   or u.userNm like %:searchWord% ")
    Page<SystemUser> findAllBySearchWord(@Param("searchWord") String searchWord, Pageable pageable);
}

