/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.zq.user.config;

import cn.hutool.core.util.StrUtil;
import com.zq.common.config.redis.BaseCacheKeys;
import com.zq.common.config.redis.RedisUtils;
import com.zq.common.config.security.SecurityProperties;
import com.zq.common.context.ContextUtils;
import com.zq.common.vo.OnlineUserDto;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

/**
 * @author /
 */
public class TokenFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(TokenFilter.class);

    private final TokenProvider tokenProvider;
    private final SecurityProperties properties;
    private final RedisUtils redisUtils;

    /**
     * @param tokenProvider Token
     * @param properties    JWT
     * @param redisUtils    redis
     */
    public TokenFilter(TokenProvider tokenProvider, SecurityProperties properties, RedisUtils redisUtils) {
        this.properties = properties;
        this.tokenProvider = tokenProvider;
        this.redisUtils = redisUtils;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = tokenProvider.getToken(httpServletRequest);
        // 对于 Token 为空的不需要去查 Redis
        if (StrUtil.isNotBlank(token)) {
            OnlineUserDto onlineUserDto = null;
            boolean cleanUserCache = false;
            try {
                onlineUserDto = redisUtils.getObj(properties.getOnlineKey() + token, OnlineUserDto.class);
            } catch (ExpiredJwtException e) {
                log.error(e.getMessage());
                cleanUserCache = true;
            } finally {
                if (cleanUserCache || Objects.isNull(onlineUserDto)) {
                    String username = String.valueOf(tokenProvider.getClaims(token).get(TokenProvider.AUTHORITIES_KEY));
                    redisUtils.hdel(BaseCacheKeys.USER_DATA_MAP_KEY, username);
                }
            }
            if (onlineUserDto != null && StringUtils.isNotBlank(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // Token 续期
                tokenProvider.checkRenewal(token);

                // 设置当前用户
                ContextUtils.setAdminContext(onlineUserDto);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
