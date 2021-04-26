package com.zq.api.feign;

import com.zq.api.config.FeignConfig;
import com.zq.common.vo.ResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient(name = "CMS-SERVER", configuration = FeignConfig.class)  //指定调用哪个微服务
@RequestMapping("/cms")
public interface CmsFeign {

    @GetMapping("/adviceFeedback/getAdviceFeedbackById")
    ResultVo test(Map<String, Object> paramsMap);

}
