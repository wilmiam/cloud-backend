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

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @author wilmiam
 * @since 2021-07-14 10:06
 */
@Slf4j
public class JavaSerializer implements Serializer {

    @Override
    public String name() {
        return "java";
    }

    @Override
    public byte[] serialize(Object obj) throws IOException {
        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            return baos.toByteArray();
        } finally {
            IoUtil.close(oos);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] bits) throws IOException {
        if (bits == null || bits.length == 0) {
            return null;
        }
        ObjectInputStream ois = null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bits);
            ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (ClassNotFoundException e) {
            log.error("内存反序列化没有找到类型", e);
        } finally {
            IoUtil.close(ois);
        }
        return null;
    }

}
