package com.zq.api.controller;

import cn.hutool.core.util.StrUtil;
import com.zq.api.constant.ApiMethod;
import com.zq.api.form.ApiForm;
import com.zq.api.form.ApiResp;
import com.zq.api.service.IApiLogic;
import com.zq.api.utils.ApiUtils;
import com.zq.common.vo.ResultVo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wilmiam
 * @since 2022/1/21 17:00
 */
@Api(tags = "方法接口")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MethodController {

    @RequestMapping("/method")
    public ApiResp getAllMethod(@RequestParam(required = false) String service, @RequestParam(required = false) String name) {
        List<Map<String, Object>> methodList = new ArrayList<>();
        Method[] methods = IApiLogic.class.getMethods();
        for (Method method : methods) {
            Class<?>[] params = method.getParameterTypes();
            if (params.length == 1 && (params[0] == ApiForm.class)) {
                ApiMethod apiMethod = AnnotationUtils.getAnnotation(method, ApiMethod.class);
                if (StrUtil.isNotBlank(service)) {
                    if (apiMethod == null || !apiMethod.service().equals(service)) {
                        continue;
                    }
                }
                if (StrUtil.isNotBlank(name)) {
                    if ((apiMethod == null || !apiMethod.value().contains(name)) && !method.getName().toLowerCase().contains(name.toLowerCase())) {
                        continue;
                    }
                }

                Map<String, Object> data = new HashMap<>();
                data.put("value", method.getName());
                data.put("name", apiMethod == null ? "" : apiMethod.value());
                data.put("service", apiMethod == null ? "" : apiMethod.service());
                methodList.add(data);
            }
        }
        return ApiUtils.toApiResp(new ApiForm(), ResultVo.success(methodList));
    }

}
