package com.zq.api.feign;

import com.zq.api.config.FeignConfig;
import com.zq.common.vo.ResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "USER-SERVER", configuration = FeignConfig.class)  //指定调用哪个微服务
@RequestMapping("/user/app")
public interface UserFeign {

    @GetMapping(value = "/sendCode")
    ResultVo sendCode(@RequestParam String phone);

}
