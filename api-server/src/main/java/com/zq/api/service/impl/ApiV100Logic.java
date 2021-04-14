package com.zq.api.service.impl;

import com.zq.api.form.ApiForm;
import com.zq.api.form.ApiResp;
import com.zq.api.service.IApiLogic;
import com.zq.api.utils.ApiUtils;
import com.zq.common.vo.ResultVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiV100Logic extends BaseApiLogic implements IApiLogic {

    /**
     * 测试连接
     *
     * @param form
     * @return
     */
    @Override
    public ApiResp test(ApiForm form) {
        return ApiUtils.toApiResp(form, ResultVo.success());
    }
}
