package com.zq.api.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

/**
 * @author wilmiam
 * @since 2021-07-13 09:50
 */
@Slf4j
@Configuration
public class ApiCommonConfig implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        log.info(">> 参数配置Cache初始化");
        ConfigCache.init();
        log.info("<< 完成初始化系统配置表");
    }

}
