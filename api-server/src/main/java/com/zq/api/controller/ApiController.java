package com.zq.api.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.zq.api.config.ApiTokenUtils;
import com.zq.api.constant.ApiCodeEnum;
import com.zq.api.form.ApiForm;
import com.zq.api.form.ApiResp;
import com.zq.api.service.ApiService;
import com.zq.api.utils.ApiUtils;
import com.zq.common.vo.ApiTokenVo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Api(tags = "API接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {

    private final ApiService apiService;

    // 允许用户未登录状态下执行的方法名
    private String[] allowMethod = {"test"};

    /**
     * 获取信息入口
     * <p>
     * 2016年10月3日 下午1:38:27
     */
    @RequestMapping("/action")
    public ApiResp action(HttpServletRequest request) {
        long start = System.currentTimeMillis();

        ApiForm form = ServletUtil.toBean(request, ApiForm.class, true);
        ApiResp resp = new ApiResp(form);

        // 不处理Request Method:OPTIONS的请求
        if (request.getMethod().equals("OPTIONS")) {
            return resp;
        }

        String method = form.getMethod();
        if (StrUtil.isBlank(method)) {
            method = request.getParameter("method");
            form.setMethod(method);
        }

        if (StrUtil.isBlank(form.getToken())) {
            boolean contains = Arrays.asList(allowMethod).contains(method);
            if (!contains) {
                return ApiUtils.getLoginValidError(form);
            }
        }

        ApiTokenVo tokenVo = ApiTokenUtils.getAppTokenVo(form.getToken());
        if (tokenVo == null) {
            boolean contains = Arrays.asList(allowMethod).contains(method);
            if (!contains) {
                return ApiUtils.getLoginValidError(form);
            }
        } else {
            form.setUserId(String.valueOf(tokenVo.getUserId()));
            form.setApiTokenVo(tokenVo);
        }

        // 调用接口方法
        resp = apiService.action(form);
        // 没有数据输出空
        resp = resp == null ? new ApiResp(form) : resp;

        String logType = "INFO";
        if (!resp.getCode().equals(ApiCodeEnum.SUCCESS.code())) {
            logType = "ERROR";
        }
        String respMsg = resp.getMsg();

        // 调试日志
        if (ApiUtils.DEBUG) {
            System.out.println("API DEBUG ACTION \n[from=" + form + "]" //
                    + "\n[resp=" + JSON.toJSONString(resp) + "]" //
                    + "\n[time=" + (System.currentTimeMillis() - start) + "ms]");
        }

        apiService.addLog(form, logType, respMsg, System.currentTimeMillis() - start);
        return resp;
    }

    /**
     * 开关调试日志
     * <p>
     * 2016年10月3日 下午5:47:46
     */
    @RequestMapping("/debug")
    public ApiResp debug(HttpServletRequest request) {
        ApiForm from = ServletUtil.toBean(request, ApiForm.class, true);

        ApiUtils.DEBUG = !ApiUtils.DEBUG;
        return new ApiResp(from).addData("debug", ApiUtils.DEBUG);
    }
}
