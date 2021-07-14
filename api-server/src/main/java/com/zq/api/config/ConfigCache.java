package com.zq.api.config;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zq.api.cache.Cache;
import com.zq.api.cache.CacheManager;
import com.zq.api.dao.SysConfigDao;
import com.zq.api.utils.NumberUtils;
import com.zq.common.config.base.SpringContextHolder;
import com.zq.common.entity.SysConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置管理缓存
 *
 * @author wilmiam
 * @since 2021-07-14 10:42
 */
@Slf4j
public class ConfigCache {

    private final static String CACHE_NAME = "ConfigCache";
    private static Cache cache;

    private ConfigCache() {
    }

    public static void init() {
        if (cache == null) {
            cache = CacheManager.get(CACHE_NAME);
        }
        Map<String, SysConfig> cacheMap = new HashMap<>();
        SysConfigDao configDao = SpringContextHolder.getBean(SysConfigDao.class);
        List<SysConfig> sysConfigList = configDao.selectList(Wrappers.lambdaQuery(null));
        for (SysConfig config : sysConfigList) {
            cacheMap.put(config.getCode(), config);
        }
        cache.add("cacheMap", cacheMap);
    }

    public static void update() {
        init();
    }

    public static SysConfig getSysConfig(String code) {
        return getSysConfigMap().get(code);
    }

    public static String getValue(String code) {
        return getSysConfig(code) == null ? null : getSysConfig(code).getValue();
    }

    public static int getValueToInt(String code) {
        return NumberUtils.parseInt(getValue(code));
    }

    public static Boolean getValueToBoolean(String code) {
        String val = getValue(code);
        try {
            return Boolean.valueOf(val);
        } catch (Exception e) {
            return false;
        }
    }

    private static Map<String, SysConfig> getSysConfigMap() {
        return cache.get("cacheMap");
    }

}
