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
package com.zq.common.config.limit;

/**
 * 限流枚举
 *
 * @author wilmiam
 * @since 2021-07-09 17:51
 */
public enum LimitType {
    /**
     * 针对每个IP进行限流
     */
    IP,
    /**
     * 针对每个用户进行限流
     */
    USER,
    /**
     * 针对对象的某个属性值进行限流
     */
    POJO_FIELD,
    /**
     * 针对某个参数进行限流
     */
    PARAM,
    /**
     * 直接对指定的key进行限流
     */
    KEY
}
