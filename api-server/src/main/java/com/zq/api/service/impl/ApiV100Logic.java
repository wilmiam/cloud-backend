package com.zq.api.service.impl;

import com.zq.api.form.ApiForm;
import com.zq.api.form.ApiResp;
import com.zq.api.service.IApiLogic;

public class ApiV100Logic extends BaseApiLogic implements IApiLogic {

    /**
     * 测试连接
     *
     * @param form
     * @return
     */
    @Override
    public ApiResp test(ApiForm form) {
        return new ApiResp(form);
    }
}
