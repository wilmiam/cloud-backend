package com.zq.user.feign;

import com.zq.common.vo.OnlineUserDto;
import com.zq.common.vo.ResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author wilmiam
 * @since 2022/10/11 17:19
 */
@FeignClient(name = "ADMIN-SERVER", path = "/admin")
public interface AdminFeignClient {

    /**
     * 获取当前用户
     *
     * @return
     */
    @GetMapping(value = "/auth/getCurrentUser")
    ResultVo<OnlineUserDto> getCurrentUser();

}
