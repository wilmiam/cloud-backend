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

import com.zq.common.annotation.Limit;
import com.zq.common.http.HttpRequestUtils;
import com.zq.common.utils.AssertUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author /
 */
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

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method signatureMethod = signature.getMethod();
        Limit limit = signatureMethod.getAnnotation(Limit.class);
        LimitType limitType = limit.limitType();
        String key = limit.key();
        if (StringUtils.isBlank(key)) {
            if (limitType == LimitType.IP) {
                key = HttpRequestUtils.getClientIp(request);
            } else {
                key = signatureMethod.getName();
                if ("sendCode".equals(key)) {
                    key = (String) joinPoint.getArgs()[0];
                }
            }
        }

        key = StringUtils.join(limit.prefix(), "_", key, "_", signatureMethod.getName());

        /*String obj = stringRedisTemplate.opsForValue().get(key);
        int currentLimit = 0;
        if (obj != null) {
            currentLimit = Integer.parseInt(obj);
        }
        if (currentLimit + 1 > limit.count()) {
            throw new BusinessException("访问次数受限制");
        }

        stringRedisTemplate.opsForValue().set(key, (currentLimit + 1) + "");
        stringRedisTemplate.expire(key, limit.period(), TimeUnit.SECONDS);

        logger.info("第{}次访问key为 {}，描述为 [{}] 的接口", currentLimit + 1, key, limit.name());
        return joinPoint.proceed();*/

        List<String> keys = Collections.singletonList(key);
        String luaScript = buildLuaScript();
        RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
        Long count = stringRedisTemplate.execute(redisScript, keys, String.valueOf(limit.count()), String.valueOf(limit.period()));

        AssertUtils.isTrue(count != null && count != 0, "访问次数受限制");

        logger.info("第{}次访问key为 {}，描述为 [{}] 的接口", count, keys, limit.name());
        return joinPoint.proceed();
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

}
