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

package com.zq.common.cache.serializable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wilmiam
 * @since 2021-07-14 10:03
 */
public class SerializerManage {

    private static final Map<String, Serializer> MAP = new HashMap<>();
    private static String DEFAULT_KEY;

    static {
        JavaSerializer javaSerializer = new JavaSerializer();
        add(javaSerializer);
        DEFAULT_KEY = javaSerializer.name();
    }

    public static void setDefaultKey(String defaultKey) {
        DEFAULT_KEY = defaultKey;
    }

    /**
     * V3.1以前版本兼容
     * <p>
     * 2017年1月18日 下午2:38:00
     *
     * @param key
     * @param serializer
     */
    public static void add(String key, Serializer serializer) {
        MAP.put(key, serializer);
    }

    public static void add(Serializer serializer) {
        MAP.put(serializer.name(), serializer);
    }

    public static Serializer get(String key) {
        return MAP.get(key);
    }

    public static Serializer getDefault() {
        return MAP.get(DEFAULT_KEY);
    }

    public static byte[] serialize(Object obj) throws IOException {
        return getDefault().serialize(obj);
    }

    public static Object deserialize(byte[] bytes) throws IOException {
        return getDefault().deserialize(bytes);
    }

}
