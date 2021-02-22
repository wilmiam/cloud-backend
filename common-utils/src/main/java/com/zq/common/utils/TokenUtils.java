package com.zq.common.utils;


import com.zq.common.constant.SecurityProperties;
import com.zq.common.context.ThreadContext;
import com.zq.common.vo.AppTokenVo;
import com.zq.common.vo.OnlineUserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class TokenUtils {

    private static final String APP_TOKEN_CONTEXT_KEY = "app-token";
    private static final String ADMIN_TOKEN_CONTEXT_KEY = "admin-token";

    public static void setUserContext(AppTokenVo appTokenVo) {
        if (appTokenVo != null) {
            ThreadContext.set(APP_TOKEN_CONTEXT_KEY, appTokenVo);
        }
    }

    public static AppTokenVo getUserContext() {
        return ThreadContext.get(APP_TOKEN_CONTEXT_KEY);
    }

    public static Long getUserUserId() {
        AppTokenVo appTokenVo = ThreadContext.get(APP_TOKEN_CONTEXT_KEY);
        return appTokenVo.getUserId();
    }

    public static void setAdminContext(OnlineUserDto onlineUserDto) {
        if (onlineUserDto != null) {
            ThreadContext.set(ADMIN_TOKEN_CONTEXT_KEY, onlineUserDto);
        }
    }

    public static OnlineUserDto getAdminContext() {
        return ThreadContext.get(ADMIN_TOKEN_CONTEXT_KEY);
    }

    public static Long getAdminUserId() {
        OnlineUserDto userDto = ThreadContext.get(ADMIN_TOKEN_CONTEXT_KEY);
        return userDto.getUserId();
    }

    public static String getToken(HttpServletRequest request) {
        String header = request.getHeader(SecurityProperties.HEADER);
        if (StringUtils.isNotBlank(header)) {
            return header.replace(SecurityProperties.getTokenStartWith(), "");
        }
        return "";
    }

}

