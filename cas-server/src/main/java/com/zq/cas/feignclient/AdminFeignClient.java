package com.zq.cas.feignclient;

import com.zq.cas.config.feign.FeignConfig;
import com.zq.common.vo.ResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author wilmiam
 * @since 2022/10/11 17:19
 */
@FeignClient(name = "ADMIN-SERVER", path = "/admin", configuration = FeignConfig.class)
public interface AdminFeignClient {

    @PostMapping(value = "/auth/casLogin")
    ResultVo casLogin(@RequestBody Map<String, Object> params);

    @DeleteMapping(value = "/auth/logout")
    ResultVo logout();

}
