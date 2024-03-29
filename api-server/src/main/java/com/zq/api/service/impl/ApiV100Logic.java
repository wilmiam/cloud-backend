package com.zq.api.service.impl;

import com.zq.api.feign.UserFeign;
import com.zq.api.form.ApiForm;
import com.zq.api.form.ApiResp;
import com.zq.api.service.IApiLogic;
import com.zq.api.utils.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiV100Logic extends BaseApiLogic implements IApiLogic {

    @Autowired
    private UserFeign userFeign;

    /**
     * 发送验证码
     *
     * @param form
     * @return
     */
    @Override
    public ApiResp sendCode(ApiForm form) {
        return ApiUtils.toApiResp(form, userFeign.sendCode(form.getString("phone")));
    }

}
