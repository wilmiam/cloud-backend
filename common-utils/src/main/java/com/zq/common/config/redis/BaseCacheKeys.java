package com.zq.common.config.redis;

import com.zq.common.config.limit.LimitType;
import org.apache.commons.lang3.StringUtils;

/**
 * 公共缓存key
 *
 * @author wilmiam
 * @since 2021-07-09 17:52
 */
public abstract class BaseCacheKeys {

    public static final String PREFIX = "cloud.";

    private static final String APP_TOKEN = PREFIX + "app-token.";

    private static final String ADMIN_TOKEN = PREFIX + "admin-token.";

    private static final String RATE_LIMIT = PREFIX + "rate-limit.";

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
     * 构建admin端用户token的缓存key
     *
     * @param token admin登陆后的token
     * @return
     */
    public static String adminTokenKey(String token) {
        return ADMIN_TOKEN + token;
    }

    /**
     * 构建限流Key
     *
     * @param type
     * @param prefix
     * @param key
     * @return
     */
    public static String rateLimitKey(LimitType type, String prefix, String key) {
        return rateLimitKey(type, prefix, key, null);
    }

    /**
     * 构建限流key
     *
     * @param type
     * @param prefix
     * @param key
     * @param param
     * @return
     */
    public static String rateLimitKey(LimitType type, String prefix, String key, String param) {
        String result = RATE_LIMIT;
        if (StringUtils.isNotBlank(prefix)) {
            result = PREFIX + prefix + ".";
        }
        switch (type) {
            case IP:
                result += "ip.";
                break;
            case USER:
                result += "u.";
                break;
            case PARAM:
                result += "p.";
                break;
            case POJO_FIELD:
                result += "f.";
                break;
            case KEY:
                result += "k.";
                break;
            default:
                // nothing to do
        }
        result += key;
        if (StringUtils.isNotBlank(param)) {
            result += "." + param;
        }
        return result;
    }

}
