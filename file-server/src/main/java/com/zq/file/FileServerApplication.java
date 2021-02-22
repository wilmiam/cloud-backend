package com.zq.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {
        "com.zq.file",
        "com.zq.common.config.base"}, exclude = {DataSourceAutoConfiguration.class})
public class FileServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileServerApplication.class, args);
    }

    /**
     * 访问图片返回BufferedImage类型需要添加这个
     *
     * @return
     */
    @Bean
    public BufferedImageHttpMessageConverter bufferedImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

}
