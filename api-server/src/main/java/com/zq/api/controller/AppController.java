package com.zq.api.controller;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.zq.api.constant.ApiCodeEnum;
import com.zq.api.form.ApiForm;
import com.zq.api.form.ApiResp;
import com.zq.api.service.AppService;
import com.zq.api.utils.ApiUtils;
import com.zq.common.config.security.ApiTokenUtils;
import com.zq.common.utils.ThrowableUtil;
import com.zq.common.vo.ApiTokenVo;
import com.zq.common.vo.ResultVo;
import feign.FeignException;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author wilmiam
 * @since 2021-08-16 15:37
 */
@Api(tags = "APP接口")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/app")
public class AppController {

    private final AppService appService;

    /**
     * 允许用户未登录状态下执行的方法名
     */
    private final String[] allowMethod = {"wxLogin", "getWxPhone"};

    /**
     * 获取信息入口
     * <p>
     * 2016年10月3日 下午1:38:27
     */
    @RequestMapping("/action")
    public ApiResp action(HttpServletRequest request, ApiForm form) {
        long start = System.currentTimeMillis();

        // 不处理Request Method:OPTIONS的请求
        if ("OPTIONS".equals(request.getMethod())) {
            return ApiUtils.getSuccessResp(form);
        }

        form.setType(1);
        //解析业务参数
        if (!form.parseBizContent()) {
            return ApiUtils.getParamError(form);
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

        String stackTrace = "";
        // 调用接口方法
        ApiResp resp;
        try {
            resp = appService.action(form);
        } catch (Exception e) {
            log.error("调用方法异常：{}", e.getMessage());
            stackTrace = ThrowableUtil.getStackTrace(e);
            // 判断指定异常是否来自或者包含指定异常
            if (ExceptionUtil.isFromOrSuppressedThrowable(e, FeignException.Unauthorized.class)) {
                resp = ApiUtils.toApiResp(form, ResultVo.fail(401, "Unauthorized"));
            } else if (ExceptionUtil.isFromOrSuppressedThrowable(e, FeignException.NotFound.class)) {
                resp = ApiUtils.toApiResp(form, ResultVo.fail(404, "NotFound"));
            } else if (stackTrace.contains("Load balancer does not have available server for client")) {
                resp = ApiUtils.getServiceNotAvailableError(form);
            } else if (stackTrace.contains("Connection refused: connect executing")) {
                resp = ApiUtils.getServiceNotAvailableError(form);
            } else {
                resp = ApiUtils.getMethodHandlerError(form);
            }
        }
        // 没有数据输出空
        resp = resp == null ? new ApiResp(form) : resp;

        String logType = resp.isSuccess() ? "INFO" : "400".equals(resp.getCode()) ? "WARN" : "ERROR";

        // 如果是500错误, 服务会返回错误的堆栈信息
        if (resp.getCode().equals(ApiCodeEnum.SERVER_ERROR.code())) {
            stackTrace = resp.getMsg();
            resp.setMsg(ApiCodeEnum.SERVER_ERROR.msg());
        }

        // 调试日志
        if (ApiUtils.DEBUG) {
            System.out.println("API DEBUG ACTION \n[from=" + form + "]"
                    + "\n[resp=" + JSON.toJSONString(resp) + "]"
                    + "\n[time=" + (System.currentTimeMillis() - start) + "ms]");
        }

        String clientIp = ServletUtil.getClientIP(request);
        appService.addLog(form, clientIp, logType, resp.getMsg(), stackTrace, System.currentTimeMillis() - start);
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
        return new ApiResp(from).setData(ApiUtils.DEBUG);
    }

}
