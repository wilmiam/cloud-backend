package com.zq.api.feign;

import com.zq.common.vo.ResultVo;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class CmsFeign {

    private RestTemplate restTemplate = new RestTemplate();

    private static String cmsServer = "http://127.0.0.1:9889/cms";

    public ResultVo test(Map<String, Object> paramsMap) {
        return restTemplate.getForObject(cmsServer + "/adviceFeedback/getAdviceFeedbackById", ResultVo.class);
    }
}
