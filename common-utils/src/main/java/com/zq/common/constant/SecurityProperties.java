package com.zq.common.constant;

import lombok.Data;

@Data
public class SecurityProperties {
    /**
     * Request Headers ： Authorization
     */
    public static String HEADER = "Authorization";

    /**
     * 令牌前缀，最后留个空格 Bearer
     */
    public static String TOKENSTARTWITH = "Bearer";

    /**
     * 必须使用最少88位的Base64对该令牌进行编码
     */
    public static String BASE64SECRET = "ZmQ0ZGI5NjQ0MDQwY2I4MjMxY2Y3ZmI3MjdhN2ZmMjNhODViOTg1ZGE0NTBjMGM4NDA5NzYxMjdjOWMwYWRmZTBlZjlhNGY3ZTg4Y2U3YTE1ODVkZDU5Y2Y3OGYwZWE1NzUzNWQ2YjFjZDc0NGMxZWU2MmQ3MjY1NzJmNTE0MzI=";

    /**
     * 令牌过期时间 此处单位/毫秒
     */
    public static Long TOKENVALIDITYINSECONDS = 14400000L;

    /**
     * 在线用户 key，根据 key 查询 redis 中在线用户的数据
     */
    public static String ONLINEKEY = "online-token-";

    /**
     * 验证码 key
     */
    public static String CODEKEY = "code-key-";

    /**
     * token 续期检查
     */
    public static Long DETECT = 1800000L;

    /**
     * 续期时间
     */
    public static Long RENEW = 3600000L;

    public static String getTokenStartWith() {
        return TOKENSTARTWITH + " ";
    }
}

