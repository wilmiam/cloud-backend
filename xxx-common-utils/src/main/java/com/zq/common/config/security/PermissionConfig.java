package com.zq.common.config.security;

import com.zq.common.context.ContextUtils;
import com.zq.common.vo.OnlineUserDto;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

/**
 * @author wilmiam
 * @since 2024-09-19 16:21
 */
@Service(value = "u")
public class PermissionConfig {

    public Boolean check(String... permissions) {
        // 获取当前用户的所有权限
        OnlineUserDto adminContext = ContextUtils.getAdminContext();
        Set<String> contextAuthority = adminContext.getAuthority();
        // 判断当前用户的所有权限是否包含接口上定义的权限
        return contextAuthority.contains("admin") || Arrays.stream(permissions).anyMatch(contextAuthority::contains);
    }

}
