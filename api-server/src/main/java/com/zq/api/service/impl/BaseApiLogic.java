package com.zq.api.service.impl;

import com.zq.api.form.ApiForm;
import com.zq.api.form.ApiResp;
import com.zq.api.service.IApiLogic;
import com.zq.api.utils.ApiUtils;

/**
 * API基础类
 * <p>
 * 2016年11月15日 下午9:48:27
 *
 * @author wilmiam
 */
public abstract class BaseApiLogic implements IApiLogic {

    @Override
    public ApiResp login(ApiForm form) {
        return null;
    }

    @Override
    public ApiResp signValid(ApiForm form) {
        String timestamp = form.getTimestamp();
        // 一分钟内的数据有效
        if (Long.parseLong(timestamp) + (60 * 1000) > System.currentTimeMillis()) {
            return ApiUtils.getCheckSignValidError(form);
        }

        String serverSign = ApiUtils.getSign(form.getSignStr(""));
        if (!serverSign.equals(form.getSign())) {
            return ApiUtils.getCheckSignValidError(form);
        }
        return ApiUtils.getSuccessResp(form);
    }

    /**
     * 不需要验证的方法
     * <p>
     * 2016年11月15日 下午11:03:01
     *
     * @param form
     * @return
     */
    protected boolean notValid(ApiForm form) {
        return "login".equals(form.getMethod());
    }

    @Override
    public ApiResp logout(ApiForm form) {
        return new ApiResp(form).setData("ok");
    }

    @Override
    public ApiResp config(ApiForm form) {
        return null;
    }


}
