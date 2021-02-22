package com.zq.common.config.interceptor;

import com.zq.common.config.redis.CacheKeys;
import com.zq.common.config.redis.RedisUtils;
import com.zq.common.utils.TokenUtils;
import com.zq.common.vo.AppTokenVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInfoInterceptor extends HandlerInterceptorAdapter {

    private final RedisUtils redisUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getRequestURI().startsWith("/app")) {
            String token = TokenUtils.getToken(request);
            log.info(">> [UserInfo token] {}", token);
            AppTokenVo tokenVo = redisUtils.getObj(CacheKeys.appTokenKey(token), AppTokenVo.class);
            TokenUtils.setUserContext(tokenVo);
        }
        return true;
    }

}
