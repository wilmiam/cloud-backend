package com.zq.common.config.redis;

public abstract class CacheKeys {

    public static final String PREFIX = "drug.";

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
