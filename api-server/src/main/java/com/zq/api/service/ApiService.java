package com.zq.api.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.zq.api.dao.ApiLogDao;
import com.zq.api.entity.ApiLog;
import com.zq.api.form.ApiForm;
import com.zq.api.form.ApiResp;
import com.zq.api.utils.ApiUtils;
import com.zq.api.utils.ReflectionUtils;
import com.zq.common.config.redis.BaseCacheKeys;
import com.zq.common.config.redis.RedisUtils;
import com.zq.common.vo.ApiTokenVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiService {

    private final ApiLogDao apiLogDao;
    private final RedisUtils redisUtils;

    // 允许用户未登录状态下执行的方法名
    private final String[] allowMethod = {"getApiToken"};

    private static final List<String> METHOD_LIST;

    static {
        METHOD_LIST = methodList();
    }

    public IApiLogic getApiLogic(ApiForm form) {
        IApiLogic apiLogic = ApiUtils.getApiLogic(form);
        return apiLogic;
    }

    public static List<String> methodList() {
        List<String> methodList = new ArrayList<>();
        Method[] methods = IApiLogic.class.getMethods();
        for (Method method : methods) {
            Class<?>[] params = method.getParameterTypes();
            if (params.length == 1 && (params[0] == ApiForm.class)) {
                methodList.add(method.getName());
            }
        }
        return methodList;
    }

    /**
     * 身份验证
     *
     * @param form
     * @return
     */
    public ApiResp auth(ApiForm form, String token) {
        boolean contains = Arrays.asList(allowMethod).contains(form.getMethod());
        if (contains) {
            return ApiUtils.getSuccessResp(form);
        } else if (StrUtil.isBlank(token)) {
            return ApiUtils.getLoginValidError(form);
        }

        // 验证认证信息
        ApiTokenVo tokenVo = redisUtils.getObj(BaseCacheKeys.PREFIX + "ApiToken." + token, ApiTokenVo.class);
        if (tokenVo == null) {
            return ApiUtils.getLoginValidError(form);
        }

        // 验证签名
        String sign = ApiUtils.getSign(form.getSignStr(tokenVo.getSessionKey() == null ? "" : tokenVo.getSessionKey()));
        if (!sign.equals(form.getSign())) {
            return ApiUtils.getCheckSignValidError(form);
        }

        form.setUserId(tokenVo.getUserId());
        form.setApiTokenVo(tokenVo);
        return ApiUtils.getSuccessResp(form);
    }

    public ApiResp action(ApiForm form) throws Exception {
        if (!METHOD_LIST.contains(form.getMethod())) {
            return ApiUtils.getMethodError(form);
        }
        IApiLogic apiLogic = getApiLogic(form);

        // 调用接口方法，利用反射更简洁
        return (ApiResp) ReflectionUtils.invokeMethod(apiLogic, form.getMethod(), new Class<?>[]{ApiForm.class}, new Object[]{form});
    }

    @Async
    public void addLog(ApiForm form, String ip, String logType, String respMsg, String errorInfo, Long timeCost) {
        apiLogDao.insert(ApiLog.builder()
                .appId(form.getAppId())
                .userId(form.getUserId())
                .method(form.getMethod())
                .version(form.getVersion())
                .bizContent(form.getBizContent())
                .ip(ip)
                .logType(logType)
                .respMsg(respMsg)
                .stackTrace(errorInfo)
                .timeCost(timeCost)
                .createTime(DateUtil.date().toJdkDate())
                .build());
    }

}
