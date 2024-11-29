package com.penta.security.auth.vo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * Refresh Token Store
 * TODO: Redis 도입
 */
@Component
public class RefreshTokenStore {

    /**
     * key: userId value: refresh token
     */
    private final Map<String, String> refreshTokenInfo = new ConcurrentHashMap<>();

    /**
     * refresh token get
     *
     * @param userId user id
     * @return refresh token
     */
    public String get(final String userId) {
        return refreshTokenInfo.get(userId);
    }

    /**
     * refresh token put
     *
     * @param userId       user id
     * @param refreshToken refresh token
     */
    public void put(final String userId, final String refreshToken) {
        refreshTokenInfo.put(userId, refreshToken);
    }

    /**
     * refresh token remove
     *
     * @param userId user id
     */
    public void remove(final String userId) {
        refreshTokenInfo.remove(userId);
    }
}
