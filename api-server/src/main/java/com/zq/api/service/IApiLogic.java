package com.zq.api.service;

import com.zq.api.form.ApiForm;
import com.zq.api.form.ApiResp;

/**
 * api实现接口
 * <p>
 * 2016年9月29日 上午11:45:08
 */
public interface IApiLogic extends IApiCommon {

    /**
     * 发送验证码
     *
     * @param form
     * @return
     */
    ApiResp sendCode(ApiForm form);

}
