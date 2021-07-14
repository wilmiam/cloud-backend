package com.zq.api.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zq.api.config.ConfigCache;
import com.zq.api.form.ApiForm;
import com.zq.api.utils.ApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author wilmiam
 * @since 2021-07-14 10:46
 */
@Slf4j
@Component
public class ApiInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        System.out.println("get URI = " + request.getRequestURL());
        System.out.println("get queryString = " + queryString);

        long start = System.currentTimeMillis();

        // 获取传递参数
        ApiForm form = ServletUtil.toBean(request, ApiForm.class, true);
        // 开关
        boolean flag = ConfigCache.getValueToBoolean("API.FLAG");
        if (!flag) {
            ServletUtil.write(response, JSON.toJSONString(ApiUtils.getServerMaintain(form), SerializerFeature.WriteMapNullValue), "application/json;charset=utf-8");
            return false;
        }

        // 黑名单
        String ip = ServletUtil.getClientIP(request);
        String blackIps = ConfigCache.getValue("API.IP.BLACK");
        if (StrUtil.isNotBlank(ip) && StrUtil.isNotBlank(blackIps)) {
            List<String> ipList = Arrays.asList(blackIps.split(","));
            // 如果黑名单包含该IP返回错误信息
            if (ipList.contains(ip)) {
                ServletUtil.write(response, JSON.toJSONString(ApiUtils.getIpBlackResp(form), SerializerFeature.WriteMapNullValue), "application/json;charset=utf-8");
                return false;
            }
        } else {
            log.debug("WARN: ApiInterceptor can't get ip ...");
        }

        // 版本验证
        String version = form.getVersion();
        String versions = ConfigCache.getValue("API.VERSIONS");
        if (StrUtil.isEmpty(version)) {
            ServletUtil.write(response, JSON.toJSONString(ApiUtils.getVersionErrorResp(form), SerializerFeature.WriteMapNullValue), "application/json;charset=utf-8");
            return false;
        } else if (!StrUtil.isEmpty(versions)) {
            List<String> versionList = Arrays.asList(versions.split(","));
            // 如果不支持该版本返回错误信息
            if (!versionList.contains(version)) {
                ServletUtil.write(response, JSON.toJSONString(ApiUtils.getVersionErrorResp(form), SerializerFeature.WriteMapNullValue), "application/json;charset=utf-8");
                return false;
            }
        }

        // 调试日志
        if (ApiUtils.DEBUG) {
            log.info("API DEBUG INTERCEPTOR \n[path=" + uri + "/" + queryString + "]"
                    + "[from:" + form + "]"
                    + "\n[time=" + (System.currentTimeMillis() - start) + "ms]");
        }

        return true;
    }

}
