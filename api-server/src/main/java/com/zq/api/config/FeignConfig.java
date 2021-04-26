package com.zq.api.config;

import com.zq.common.http.HttpRequestUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Configuration
public class FeignConfig {

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
                HttpServletRequest request = HttpRequestUtils.getRequest();
                template.header("X-App-Token", request.getParameter("token"));
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        // 跳过content-length
                        if (name.equals("content-length")) {
                            continue;
                        }
                        String values = request.getHeader(name);
                        template.header(name, values);
                    }
                }
            }
        };
    }

}
