package com.zq.common.config.redis;

/**
 * 公共缓存key
 *
 * @author wilmiam
 * @since 2021-07-09 17:52
 */
public abstract class BaseCacheKeys {

    public static final String PREFIX = "cloud.";

    private static final String APP_TOKEN = PREFIX + "app-token.";

    /**
     * 构建app端用户token的缓存key
     *
     * @param token app登陆后的token
     * @return
     */
    public static String appTokenKey(String token) {
        return APP_TOKEN + token;
    }

}
