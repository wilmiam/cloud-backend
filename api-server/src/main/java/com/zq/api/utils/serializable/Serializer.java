/**
 * Copyright 2015-2025 .
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zq.api.utils.serializable;

import java.io.IOException;

/**
 * @author wilmiam
 * @since 2021-07-14 10:04
 */
public interface Serializer {

    /**
     * 获取序列化名称
     *
     * @return
     */
    String name();

    /**
     * 序列化
     *
     * @param obj
     * @return
     * @throws IOException
     */
    byte[] serialize(Object obj) throws IOException;

    /**
     * 反序列化
     *
     * @param bytes
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T deserialize(byte[] bytes) throws IOException;

}
