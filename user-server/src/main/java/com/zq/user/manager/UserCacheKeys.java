package com.zq.user.manager;

import com.zq.common.config.redis.BaseCacheKeys;

/**
 * @author wilmiam
 * @since 2021-07-10 16:38
 */
public class UserCacheKeys extends BaseCacheKeys {

    public static final long APP_TOKEN_EXPIRE_MINUTES = 60 * 24 * 2;

    private static final String AUTH_CODE = PREFIX + "auth-code:";

    private static final String LIVE_APP_TOKEN = PREFIX + "live-app-token:";

    private static final String LIVE_ADMIN_TOKEN = PREFIX + "live-admin-token:";

    /**
     * 构建手机验证码的缓存key
     *
     * @param phone 手机号码
     * @return
     */
    public static String authCodeKey(String phone) {
        return AUTH_CODE + phone;
    }

    /**
     * 用户当前apptoken的缓存key
     *
     * @param userId
     * @return
     */
    public static String liveAppTokenKey(String userId) {
        return LIVE_APP_TOKEN + userId;
    }

}
