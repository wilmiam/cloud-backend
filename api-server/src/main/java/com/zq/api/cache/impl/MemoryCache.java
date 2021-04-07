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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存
 * <p>
 * 2015年4月26日 下午8:24:11
 */
public class MemoryCache implements Cache {

    protected String name;
    protected Map<String, Object> map = new ConcurrentHashMap<String, Object>();

    public MemoryCache() {
    }

    public String name() {
        return name;
    }

    public MemoryCache name(String name) {
        this.name = name;
        return this;
    }

    public MemoryCache add(String key, Object value) {
        map.put(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) map.get(key);
    }

    public Object remove(String key) {
        return map.remove(key);
    }

    public void clear() {
        map.clear();
    }

    public int size() {
        return map.size();
    }

    public Set<String> keys() {
        if (map.size() == 0) {
            return null;
        }
        return map.keySet();
    }

    @SuppressWarnings("unchecked")
    public <T> Collection<T> values() {
        if (map.size() == 0) {
            return null;
        }

        Collection<T> list = new ArrayList<T>();
        for (Object obj : map.values()) {
            list.add((T) obj);
        }

        return list;
    }

}
