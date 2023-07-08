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
import com.zq.common.context.ContextUtils;
import com.zq.common.vo.OnlineUserDto;
import com.zq.common.vo.ResultVo;
import com.zq.user.feign.AdminFeignClient;
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

/**
 * @author /
 */
public class TokenFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(TokenFilter.class);

    private final TokenProvider tokenProvider;
    private final AdminFeignClient adminFeignClient;

    /**
     * @param tokenProvider    Token
     * @param adminFeignClient adminFeign
     */
    public TokenFilter(TokenProvider tokenProvider, AdminFeignClient adminFeignClient) {
        this.tokenProvider = tokenProvider;
        this.adminFeignClient = adminFeignClient;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = tokenProvider.getToken(httpServletRequest);
        // 对于 Token 为空的不需要去查 Redis
        if (StrUtil.isNotBlank(token)) {
            OnlineUserDto onlineUserDto = null;
            try {
                ResultVo<OnlineUserDto> resultVo = adminFeignClient.getCurrentUser();
                onlineUserDto = resultVo.getData();
            } catch (Exception e) {
                log.error(">> 获取当前用户失败：" + e.getMessage());
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
