package com.zq.api.service.impl;

import com.zq.api.constant.ApiCodeEnum;
import com.zq.api.form.ApiForm;
import com.zq.api.form.ApiResp;
import com.zq.api.service.IApiLogic;
import com.zq.api.utils.ApiUtils;

import java.util.TreeMap;

/**
 * API基础类
 * <p>
 * 2016年11月15日 下午9:48:27
 */
public abstract class BaseApiLogic implements IApiLogic {

    @Override
    public ApiResp login(ApiForm form) {
        return null;
    }

    @Override
    public ApiResp valid(ApiForm form) {
        // 不需要验证的方法
        if (notValid(form)) {
            return new ApiResp(form);
        }

        TreeMap<String, String> tree = form.getSignTreeMap();
        String serverSign = ApiUtils.getSign(tree);
        if (!serverSign.equals(form.getSign())) {
            return new ApiResp(form, ApiCodeEnum.CHECK_SIGN_VALID_ERROR);
        }
        return new ApiResp(form);
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
        return form.getMethod().equals("login");
    }

    @Override
    public ApiResp logout(ApiForm form) {
        return new ApiResp(form).addData("r", "ok");
    }

    @Override
    public ApiResp config(ApiForm form) {
        return null;
    }


}
