/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * //@EnableEurekaClient
//@EnableDiscoveryClient
 */
package com.zq.cas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Zheng Jie
 * @date 2018/11/15 9:20:19
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.zq.cas", "com.zq.common.config"},
        exclude = DataSourceAutoConfiguration.class)
public class CasServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CasServerApplication.class, args);
    }

    /**
     * 访问首页提示
     *
     * @return /
     */
    @GetMapping("/")
    public String index() {
        return "Backend service started successfully";
    }

}
