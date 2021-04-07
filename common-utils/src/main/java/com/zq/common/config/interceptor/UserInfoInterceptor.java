package com.zq.common.config.interceptor;

import com.zq.common.config.base.SecurityProperties;
import com.zq.common.config.redis.CacheKeys;
import com.zq.common.config.redis.RedisUtils;
import com.zq.common.context.ContextUtils;
import com.zq.common.vo.ApiTokenVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInfoInterceptor extends HandlerInterceptorAdapter {

    private final RedisUtils redisUtils;
    private final SecurityProperties properties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getRequestURI().startsWith("/app")) {
            String token = getToken(request);
            log.info(">> [UserInfo token] {}", token);
            ApiTokenVo tokenVo = redisUtils.getObj(CacheKeys.appTokenKey(token), ApiTokenVo.class);
            ContextUtils.setUserContext(tokenVo);
        }
        return true;
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(properties.getHeader());
        if (StringUtils.isNotBlank(header)) {
            return header.replace(properties.getTokenStartWith(), "");
        }
        return "";
    }

}
