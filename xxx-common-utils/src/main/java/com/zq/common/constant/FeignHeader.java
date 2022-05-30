package com.zq.common.constant;

/**
 * feign要添加的请求头
 *
 * @author wilmiam
 * @since 2021/9/6 9:43
 */
public class FeignHeader {

    /**
     * feign添加服务名的请求头字段
     */
    public static final String SERVER_NAME = "X-Server-Name";

    /**
     * feign添加api-token的请求头字段
     */
    public static final String API_TOKEN = "X-Api-Token";

}
