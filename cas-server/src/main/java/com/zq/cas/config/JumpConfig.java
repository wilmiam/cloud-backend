package com.zq.cas.config;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author wilmiam
 * @since 2022/10/13 18:09
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jump")
public class JumpConfig {

    private Map<String, String> domainMap;

    public String getDomain(String systemTag) {
        String url = domainMap.get(systemTag);
        if (StrUtil.isBlank(url)) {
            url = domainMap.get("default");
        }
        return url;
    }

}
