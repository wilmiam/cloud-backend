package com.zq.api.config;

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

@Configuration
public class FeignConfig {

    private static final List<String> HEADERS_TO_TRY = Arrays.asList(
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
            "X-REAL-IP"
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
                template.header("X-App-Token", request.getParameter("token"));
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        // 不要设置content-length
                        if (name.equals("content-length")) {
                            continue;
                        }

                        if (HEADERS_TO_TRY.contains(name.toUpperCase())) {
                            String values = request.getHeader(name);
                            template.header(name, values);
                        }
                    }
                }
            }
        };
    }

}
