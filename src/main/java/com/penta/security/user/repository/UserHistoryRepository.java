package com.penta.security.user.repository;

import com.penta.security.user.entity.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 회원 기록 레포지토리
 */
public interface UserHistoryRepository extends JpaRepository<UserHistory, Integer> {
}

