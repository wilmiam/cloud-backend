package com.zq.api.service.impl;

import com.zq.api.feign.CmsFeign;
import com.zq.api.service.IApiLogic;
import org.springframework.stereotype.Component;

@Component
public class ApiV101Logic extends ApiV100Logic implements IApiLogic {

    public ApiV101Logic(CmsFeign cmsFeign) {
        super(cmsFeign);
    }
}
