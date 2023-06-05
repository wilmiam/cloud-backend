package com.zq.cas.config.feign;

import com.zq.common.utils.HttpRequestUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.Encoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * @author wilmiam
 * @since 2021-07-09 10:34
 */
@Configuration
public class FeignConfig {

    private final ObjectFactory<HttpMessageConverters> messageConverters;

    public FeignConfig(ObjectFactory<HttpMessageConverters> messageConverters) {
        this.messageConverters = messageConverters;
    }

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
                HttpServletRequest request = HttpRequestUtils.getRequest();
                if (request == null) {
                    return;
                }
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

    @Bean
    public Encoder feignEncoder() {
        return new FeignSpringFormEncoder(new SpringEncoder(messageConverters));
    }

}
