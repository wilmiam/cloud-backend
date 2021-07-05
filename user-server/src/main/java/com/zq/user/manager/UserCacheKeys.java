package com.zq.user.manager;

public abstract class UserCacheKeys {

    public static final long APP_TOKEN_EXPIRE_MINUTES = 60 * 24 * 2;

    public static final String PREFIX = "wine.";

    private static final String AUTH_CODE = PREFIX + "auth-code.";

    private static final String APP_TOKEN = PREFIX + "app-token.";

    private static final String ADMIN_TOKEN = PREFIX + "admin-token.";

    private static final String LIVE_ADMIN_TOKEN = PREFIX + "live-admin-token.";

    private static final String LIVE_APP_TOKEN = PREFIX + "live-app-token.";

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
     * 构建app端用户token的缓存key
     *
     * @param token app登陆后的token
     * @return
     */
    public static String appTokenKey(String token) {
        return APP_TOKEN + token;
    }

    /**
     * 用户当前apptoken的缓存key
     *
     * @param memberId
     * @return
     */
    public static String liveAppTokenKey(Long memberId) {
        return LIVE_APP_TOKEN + memberId;
    }


}
