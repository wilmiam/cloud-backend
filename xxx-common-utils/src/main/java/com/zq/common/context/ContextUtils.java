package com.zq.common.context;


import com.zq.common.vo.ApiTokenVo;
import com.zq.common.vo.OnlineUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wilmiam
 * @since 2021-07-09 17:50
 */
@Slf4j
@Component
public class ContextUtils {

    private static final String APP_TOKEN_CONTEXT_KEY = "app-token";
    private static final String ADMIN_TOKEN_CONTEXT_KEY = "admin-token";

    public static void setUserContext(ApiTokenVo apiTokenVo) {
        if (apiTokenVo != null) {
            ThreadContext.set(APP_TOKEN_CONTEXT_KEY, apiTokenVo);
        }
    }

    public static ApiTokenVo getUserContext() {
        return ThreadContext.get(APP_TOKEN_CONTEXT_KEY);
    }

    public static Long getUserUserId() {
        ApiTokenVo apiTokenVo = ThreadContext.get(APP_TOKEN_CONTEXT_KEY);
        return apiTokenVo == null ? null : apiTokenVo.getUserId();
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
        return userDto == null ? null : userDto.getUserId();
    }

}

