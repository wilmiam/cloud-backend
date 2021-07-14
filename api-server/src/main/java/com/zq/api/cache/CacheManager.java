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

package com.zq.api.cache;


import com.zq.api.cache.impl.MemorySerializeCache;
import com.zq.api.utils.serializable.SerializerManage;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 缓存管理接口
 *
 * @author wilmiam
 * @since 2021-07-14 10:00
 */
public class CacheManager {

    private static final ConcurrentHashMap<String, Cache> CACHE_MANAGER = new ConcurrentHashMap<>();
    static ICacheManager createCache;

    protected CacheManager() {
    }

    static {
        createCache = () -> new MemorySerializeCache(SerializerManage.getDefault());
    }

    public static void setCache(ICacheManager thisCache) {
        createCache = thisCache;
    }

    public static Cache get(String name) {
        Cache cache = CACHE_MANAGER.get(name);
        if (cache == null) {
            synchronized (CACHE_MANAGER) {
                cache = CACHE_MANAGER.get(name);
                if (cache == null) {
                    cache = createCache.getCache();
                    cache.name(name);
                    CACHE_MANAGER.put(name, cache);
                }
            }
        }
        return cache;
    }

    public static int size() {
        return CACHE_MANAGER.size();
    }

    public static Collection<Cache> values() {
        return CACHE_MANAGER.values();
    }

    public static Set<String> keys() {
        return CACHE_MANAGER.keySet();
    }

}