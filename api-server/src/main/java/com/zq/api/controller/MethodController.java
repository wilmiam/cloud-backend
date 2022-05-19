package com.zq.api.controller;

import cn.hutool.core.util.StrUtil;
import com.zq.api.constant.ApiMethod;
import com.zq.api.form.ApiForm;
import com.zq.api.service.IApiLogic;
import com.zq.api.vo.MethodVo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wilmiam
 * @since 2022/1/21 16:44
 */
@Api(tags = "方法接口")
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class MethodController {

    @RequestMapping("/method")
    public String getAllMethod(Model model, @RequestParam(required = false) String service, @RequestParam(required = false) String name) {
        List<MethodVo> methodList = new ArrayList<>();
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

                methodList.add(MethodVo.builder()
                        .name(apiMethod == null ? "" : apiMethod.value())
                        .service(apiMethod == null ? "" : apiMethod.service())
                        .value(method.getName())
                        .build());
            }
        }

        methodList = methodList.stream().sorted(Comparator.comparing(MethodVo::getService).reversed()).collect(Collectors.toList());
        model.addAttribute("methodList", methodList);
        return "mothod";
    }

}
