package com.zq.api.service;

import com.zq.api.form.ApiForm;
import com.zq.api.form.ApiResp;

/**
 * API通用接口
 * <p>
 * 2016年11月15日 下午9:43:49
 */
public interface IApiCommon {

    /**
     * 登陆接口
     * <p>
     * 2016年10月1日 下午9:20:12
     *
     * @param form
     * @return
     */
    ApiResp login(ApiForm form);

    /**
     * 签名验证
     * <p>
     * 2016年10月1日 下午9:20:12
     *
     * @param form
     * @return
     */
    ApiResp signValid(ApiForm form);

    /**
     * 登出接口
     * <p>
     * 2016年10月1日 下午9:20:12
     *
     * @param form
     * @return
     */
    ApiResp logout(ApiForm form);

    /**
     * 获取配置信息
     * <p>
     * 2016年10月1日 下午9:20:12
     *
     * @param form
     * @return
     */
    ApiResp config(ApiForm form);

}
