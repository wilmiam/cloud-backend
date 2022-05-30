package com.zq.api.config;

import com.zq.common.constant.FeignHeader;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

/**
 * @author wilmiam
 * @since 2021-07-09 10:34
 */
@Configuration
public class FeignConfig {

    /**
     * 转发请求头
     */
    private static final List<String> FORWARD_HEADERS = Arrays.asList(
            "AUTHORIZATION",
            "X-FORWARDED-FOR",
            "X-FORWARDED-PROTO",
            "X-FORWARDED-PORT",
            "X-FORWARDED-HOST",
            "FORWARDED",
            "PROXY-CLIENT-IP",
            "WL-PROXY-CLIENT-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "X-REAL-IP",
            "HOST"
    );

    /**
     * 解决fein远程调用丢失请求头
     *
     * @return
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
                template.header(FeignHeader.API_TOKEN, request.getParameter("token"));
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        // 不要设置content-length
                        if ("content-length".equals(name)) {
                            continue;
                        }

                        if (FORWARD_HEADERS.contains(name.toUpperCase())) {
                            String values = request.getHeader(name);
                            template.header(name, values);
                        }
                    }
                }
            }
        };
    }

}
