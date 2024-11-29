package com.penta.security.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
 * 회원 기록 엔티티
 */
@Table(name = "USER_HISTORY")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("회원 기록 기본키")
    private Integer historyIdx;

    @Column(nullable = false)
    @Comment("URL")
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("회원 기록 유형")
    private ActionType actionType;

    @Column(nullable = false)
    @Comment("최초 등록 회원 기본키")
    private Integer regUserIdx;

    @Column(nullable = false, length = 16)
    @Comment("최초 등록 아이피")
    private String regIp;

    @Column(nullable = false)
    @Comment("최초 등록 일시")
    private LocalDateTime regDt;

    public enum ActionType {
        C, U, D
    }
}
