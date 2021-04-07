package com.zq.api.cache.impl;

import cn.hutool.core.util.StrUtil;
import com.zq.api.cache.Cache;
import com.zq.api.cache.CacheManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiCache {

    private final static String cacheName = "ApiCache";
    private static Cache cache;

    public static void init() {
        if (cache == null) {
            log.info("####API Cache初始化......");
            cache = CacheManager.get(cacheName);
        }
    }

    public static <T> T getCache(String key) {
        if (StrUtil.isEmpty(key)) {
            return null;
        }

        init();
        return cache.get(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T removeCache(String key) {
        if (StrUtil.isEmpty(key)) {
            return null;
        }

        init();
        return (T) cache.remove(key);
    }

    public static Cache addCache(String key, Object value) {
        if (StrUtil.isEmpty(key)) {
            return null;
        }

        init();
        cache.add(key, value);
        return cache;
    }
}
