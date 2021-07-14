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

package com.zq.api.cache.impl;

import com.zq.api.cache.Cache;
import com.zq.api.utils.serializable.Serializer;
import com.zq.api.utils.serializable.SerializerManage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存序列化
 *
 * @author wilmiam
 * @since 2021-07-14 10:00
 */
public class MemorySerializeCache implements Cache {

    protected String name;
    protected Serializer serializer;
    protected Map<String, byte[]> map = new ConcurrentHashMap<>();

    public MemorySerializeCache() {
        this.serializer = SerializerManage.getDefault();
    }

    public MemorySerializeCache(Serializer serializer) {
        this.serializer = serializer;
    }

    public String name() {
        return name;
    }

    @Override
    public MemorySerializeCache name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public MemorySerializeCache add(String key, Object value) {
        try {
            map.put(key, serializer.serialize(value));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public <T> T get(String key) {
        try {
            return serializer.deserialize(map.get(key));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object remove(String key) {
        return map.remove(key);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Set<String> keys() {
        if (map.size() == 0) {
            return new HashSet<>();
        }
        return map.keySet();
    }

    @Override
    public <T> Collection<T> values() {
        Collection<T> list = new ArrayList<>();
        if (map.size() == 0) {
            return list;
        }

        for (byte[] obj : map.values()) {
            try {
                list.add(serializer.deserialize(obj));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

}
