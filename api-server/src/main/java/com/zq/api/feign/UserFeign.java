package com.zq.api.feign;

import com.zq.api.config.FeignConfig;
import com.zq.common.vo.ResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wilmiam
 * @since 2021-07-13 09:56
 */
@FeignClient(name = "USER-SERVER", configuration = FeignConfig.class)
@RequestMapping("/user/app")
public interface UserFeign {

    /**
     * 发送手机验证码
     *
     * @param phone
     * @return
     */
    @GetMapping(value = "/sendCode")
    ResultVo sendCode(@RequestParam String phone);

}
