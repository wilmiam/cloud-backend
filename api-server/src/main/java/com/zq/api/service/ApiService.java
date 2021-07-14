package com.zq.api.service;

import cn.hutool.core.date.DateUtil;
import com.zq.api.config.ConfigCache;
import com.zq.api.constant.ApiCodeEnum;
import com.zq.api.dao.ApiLogDao;
import com.zq.api.form.ApiForm;
import com.zq.api.form.ApiResp;
import com.zq.api.utils.ApiUtils;
import com.zq.api.utils.ReflectionUtils;
import com.zq.common.entity.ApiLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiService {

    private final ApiLogDao apiLogDao;

    private static List<String> methodList;

    static {
        methodList = methodList();
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

    public ApiResp action(ApiForm form) throws Exception {
        if (!methodList.contains(form.getMethod())) {
            return ApiUtils.getMethodError(form);
        }
        // 签名验证标识
        boolean validFlag = ConfigCache.getValueToBoolean("API.SIGN.VALID");
        IApiLogic apiLogic = getApiLogic(form);
        if (validFlag) {
            // 验证签名
            ApiResp validResp = apiLogic.signValid(form);
            if (!validResp.getCode().equals(ApiCodeEnum.SUCCESS.code())) {
                return validResp;
            }
        }

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
