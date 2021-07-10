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
 */
package com.zq.common.annotation;


import com.zq.common.config.limit.LimitType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jacky
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {

    // 限制类型
    LimitType limitType() default LimitType.IP;

    // 资源名称，用于描述接口功能
    String name() default "";

    // 资源 key
    String key() default "";

    // key prefix
    String prefix() default "";

    // 时间的，单位秒
    int period() default 1;

    // 限制访问次数
    int count() default 3;

    /**
     * 对象里的属性名,仅当仅当{@link #limitType}为{@code RateLimitTypeEnum.POJO_FIELD}时有用
     *
     * @return
     */
    String field() default "";

    /**
     * 要用来作为key组成的参数索引(从0开始), 该索引对应的参数必须为string/Long/Integer/Short/Byte, 仅当{@link #limitType}为{@code RateLimitTypeEnum.PARAM}时有用
     *
     * @return
     */
    int keyParamIndex() default 0;

    /**
     * 达到限流上限时的错误提示
     *
     * @return
     */
    String errMsg() default "操作过于频繁";

}
