package com.zq.api.service;

import com.zq.api.config.ConfigCache;
import com.zq.api.constant.ApiCodeEnum;
import com.zq.api.dao.ApiLogDao;
import com.zq.api.form.ApiForm;
import com.zq.api.form.ApiResp;
import com.zq.api.utils.ApiUtils;
import com.zq.api.utils.ReflectionUtils;
import com.zq.common.entity.ApiLog;
import com.zq.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        // 登陆验证标识
        boolean validFlag = ConfigCache.getValueToBoolean("API.LOGIN.VALID");
        IApiLogic apiLogic = getApiLogic(form);
        if (validFlag) {
            // 先进行登陆验证。如果验证失败，直接返回错误
            ApiResp validResp = apiLogic.valid(form);
            if (!validResp.getCode().equals(ApiCodeEnum.SUCCESS.code())) {
                return validResp;
            }
        }

        // 调用接口方法，利用反射更简洁
        return (ApiResp) ReflectionUtils.invokeMethod(apiLogic, form.getMethod(), new Class<?>[]{ApiForm.class}, new Object[]{form});
    }

    public void addLog(ApiForm form, String logType, String respMsg, String errorInfo, Long timeCost) {
        apiLogDao.insert(ApiLog.builder()
                .appId(form.getAppId())
                .userId(form.getUserId())
                .method(form.getMethod())
                .version(form.getVersion())
                .bizContent(form.getBizContent())
                .logType(logType)
                .respMsg(respMsg)
                .errorInfo(errorInfo)
                .timeCost(String.valueOf(timeCost))
                .createTime(DateUtils.now())
                .build());
    }
}
