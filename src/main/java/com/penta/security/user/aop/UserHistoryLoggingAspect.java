package com.penta.security.user.aop;

import com.penta.security.user.dto.response.SystemUserResponseDto;
import com.penta.security.user.entity.UserHistory;
import com.penta.security.user.repository.UserHistoryRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * SYSTEM_USER 테이블의 변경 기록을 위한 Logging Aspect
 */
@Aspect
@Component
@RequiredArgsConstructor
public class UserHistoryLoggingAspect {

    private final UserHistoryRepository userHistoryRepository;
    private final HttpServletRequest request; // 클라이언트 요청 정보를 주입

    /**
     * logging user history
     *
     * @param joinPoint
     * @return 회원 저장 결과
     * @throws Throwable
     */
    @Around("execution(* com.penta.security.user.service.SystemUserService.*(..))")
    @Transactional
    public Object logUserHistory(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메서드 이름과 매개변수를 추출
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // 작업 유형 및 URL 결정
        String actionType = getActionType(methodName);
        String url = getApiUrl(methodName, args);

        Object result;
        try {
            // 메서드 실행
            result = joinPoint.proceed();

            // 로깅
            if (actionType != null && url != null) {
                Integer userIdx = ((SystemUserResponseDto) result).getUserIdx(); // 사용자 ID 추출
                String clientIp = getClientIp(); // 클라이언트 IP 가져오기
                saveUserHistory(userIdx, actionType, url, clientIp);
            }
        } catch (Exception e) {
            throw e;
        }

        return result;
    }

    /**
     * 회원 서비스 작업 유형 맵핑
     *
     * @param methodName 회원 서비스 메서드 이름
     * @return 작업 유형
     */
    private String getActionType(String methodName) {
        return switch (methodName) {
            case "create" -> "C";
            case "update" -> "U";
            case "delete" -> "D";
            default -> null;
        };
    }

    /**
     * api url 결정
     *
     * @param methodName 회원 서비스 메서드 이름
     * @param args       메서드 매개변수
     * @return api url
     */
    private String getApiUrl(String methodName, Object[] args) {
        return switch (methodName) {
            case "create" -> "/api/users";
            case "update", "delete" -> args.length > 0 ? "/api/users?userId=" + args[0] : null;
            default -> null;
        };
    }

    /**
     * 클라이언트 IP 가져오기
     *
     * @return 클라이언트 IP
     */
    private String getClientIp() {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-RealIP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * user history 저장
     *
     * @param userIdx    회원 ID
     * @param actionType 작업 유형
     * @param url        API URL
     * @param clientIp   클라이언트 IP
     */
    private void saveUserHistory(Integer userIdx, String actionType, String url, String clientIp) {
        if (userIdx == null) {
            return;
        }

        UserHistory userHistory = UserHistory.builder()
            .regUserIdx(userIdx)
            .actionType(UserHistory.ActionType.valueOf(actionType))
            .url(url)
            .regIp(clientIp)
            .regDt(LocalDateTime.now())
            .build();

        userHistoryRepository.save(userHistory);
    }
}
