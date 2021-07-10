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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zq.common.annotation.Limit;
import com.zq.common.config.redis.BaseCacheKeys;
import com.zq.common.context.ContextUtils;
import com.zq.common.http.HttpRequestUtils;
import com.zq.common.utils.AssertUtils;
import com.zq.common.vo.ApiTokenVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * @author wilmiam
 * @since 2021-07-09 17:51
 */
@Slf4j
@Aspect
@Component
public class LimitAspect {

    private final StringRedisTemplate stringRedisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(LimitAspect.class);

    public LimitAspect(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Pointcut("@annotation(com.zq.common.annotation.Limit)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void limitBeforeExecute(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method signatureMethod = signature.getMethod();
        Limit limit = signatureMethod.getAnnotation(Limit.class);

        String key = buildLimitKey(limit, joinPoint, signatureMethod);
        log.debug("限流缓存KEY: {}", key);

        List<String> keys = Collections.singletonList(key);
        String luaScript = buildLuaScript();
        RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
        Long count = stringRedisTemplate.execute(redisScript, keys, String.valueOf(limit.count()), String.valueOf(limit.period()));

        AssertUtils.isTrue(count != null && count != 0, limit.errMsg());

        String name = limit.name();
        name = StringUtils.isNotBlank(name) ? name : signatureMethod.getName();
        logger.info("第{}次访问key为 {}，描述为 [{}] 的接口", count, keys, name);
    }

    /**
     * 限流脚本
     */
    private String buildLuaScript() {
        return "-- lua 下标从 1 开始" +
                "\n-- 限流 key" +
                "\nlocal key = KEYS[1]" +
                "\n-- 限流大小" +
                "\nlocal limit = tonumber(ARGV[1])" +
                "\nlocal perSeconds = tonumber(ARGV[2])" +
                "\n" +
                "\n-- 获取当前流量大小" +
                "\nlocal currentLimit = tonumber(redis.call('get', key) or 0)" +
                "\n" +
                "\nif currentLimit + 1 > limit then" +
                "\n    -- 达到限流大小 返回" +
                "\n    return 0;" +
                "\nelse" +
                "\n    -- 没有达到阈值 value + 1" +
                "\n    redis.call('INCRBY', key, 1)" +
                "\n    -- EXPIRE的单位是秒" +
                "\n    redis.call('EXPIRE', key, perSeconds)" +
                "\n    return currentLimit + 1" +
                "\nend";
    }

    /**
     * 构建key
     *
     * @param limit
     * @param joinPoint
     * @return
     */
    private String buildLimitKey(Limit limit, JoinPoint joinPoint, Method signatureMethod) {
        String limitKey = null;
        String key = limit.key();
        key = StringUtils.isBlank(key) ? signatureMethod.getName() : key;
        LimitType limitType = limit.limitType();

        Object[] args = joinPoint.getArgs();
        switch (limitType) {
            case IP:
                limitKey = BaseCacheKeys.rateLimitKey(LimitType.IP, key, HttpRequestUtils.getClientIp());
                break;
            case USER:
                // 按用户登录id限流
                ApiTokenVo apiTokenVo = ContextUtils.getUserContext();
                if (apiTokenVo != null) {
                    limitKey = BaseCacheKeys.rateLimitKey(LimitType.USER, key, String.valueOf(apiTokenVo.getUserId()));
                } else {
                    log.warn(">> 未找到登录用户信息,限流失败: {}", joinPoint);
                }
                break;
            case POJO_FIELD:
                String field = limit.field();
                if (StringUtils.isBlank(field)) {
                    log.warn(">> 未设置field,限流失败: {}", joinPoint);
                    break;
                }
                if (args == null || args.length == 0 || args[0] == null) {
                    log.warn(">> 未找到对象,限流失败: {}", joinPoint);
                    break;
                }
                String fieldValue = getPojoField(field, args[0]);
                limitKey = BaseCacheKeys.rateLimitKey(LimitType.POJO_FIELD, key, fieldValue);
                break;
            case PARAM:
                int keyIndex = limit.keyParamIndex();

                if (keyIndex < 0 || args == null || args.length < (keyIndex + 1) || args[keyIndex] == null) {
                    log.warn(">> 未找到参数或参数值为空,限流失败: {}, keyParamIndex={}", joinPoint, keyIndex);
                } else if (isValidKeyParamType(args[keyIndex])) {
                    limitKey = BaseCacheKeys.rateLimitKey(LimitType.PARAM, key, String.valueOf(args[keyIndex]));
                } else {
                    log.warn(">> 设置的参数不是string/long/int/short/byte类型,限流失败: {}", joinPoint);
                }
                break;
            case KEY:
                limitKey = BaseCacheKeys.rateLimitKey(LimitType.KEY, key);
                break;
            default:
                // nothing to do
        }
        return limitKey;
    }

    private boolean isValidKeyParamType(Object param) {
        return (param instanceof String) || (param instanceof Long) || (param instanceof Integer) || (param instanceof Short)
                || (param instanceof Byte);
    }

    private String getPojoField(String field, Object pojo) {
        try {
            JSONObject object = JSON.parseObject(JSON.toJSONString(pojo));
            return object.getString(field);
        } catch (Exception e) {
            return null;
        }
    }

}
