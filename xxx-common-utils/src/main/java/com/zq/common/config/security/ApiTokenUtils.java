package com.zq.common.config.security;/*
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

import com.alibaba.fastjson.JSON;
import com.zq.common.vo.ApiTokenVo;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * @author wilmiam
 * @since 2021-07-09 17:55
 */
@Slf4j
@Component
public class ApiTokenUtils implements InitializingBean {

    private static SecurityProperties properties;

    private static final String APP_TOKEN_KEY = "appToken";
    private static Key key;
    private static SignatureAlgorithm signatureAlgorithm;

    public ApiTokenUtils(SecurityProperties securityProperties) {
        ApiTokenUtils.properties = securityProperties;
    }

    @Override
    public void afterPropertiesSet() {
        signatureAlgorithm = SignatureAlgorithm.HS512;
        byte[] keyBytes = DatatypeConverter.parseBase64Binary(properties.getBase64Secret());
        key = new SecretKeySpec(keyBytes, signatureAlgorithm.getJcaName());
    }

    public static String createToken(ApiTokenVo tokenVo, long minutes) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(tokenVo.getPhone())
                .setIssuedAt(now)
                .claim(APP_TOKEN_KEY, tokenVo)
                .signWith(signatureAlgorithm, key)
                // 加入ID确保生成的 Token 都不一致
                .setId(tokenVo.getUserId());

        if (minutes >= 0) {
            long expMillis = nowMillis + (minutes * 60 * 1000);
            Date exp = new Date(expMillis);
            jwtBuilder.setExpiration(exp);
        }

        return jwtBuilder.compact();
    }

    public static ApiTokenVo getAppTokenVo(String token) {
        Claims claims = getClaims(token);
        if (claims == null) {
            return null;
        }

        // fix bug: 当前用户如果没有任何权限时，在输入用户名后，刷新验证码会抛IllegalArgumentException
        return JSON.parseObject(JSON.toJSONString(claims.get(APP_TOKEN_KEY)), ApiTokenVo.class);
    }

    public static Claims getClaims(String token) {
        try {
            //解析JWT字符串中的数据，并进行最基础的验证
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(properties.getBase64Secret()))
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        }
        //在解析JWT字符串时，如果密钥不正确，将会解析失败，抛出SignatureException异常，说明该JWT字符串是伪造的
        //在解析JWT字符串时，如果‘过期时间字段’已经早于当前时间，将会抛出ExpiredJwtException异常，说明本次请求已经失效
        catch (SignatureException | ExpiredJwtException e) {
            log.error("解析JWT TOKEN错误", e);
            return null;
        }
    }

    public static boolean isTokenValid(String token) {
        return getClaims(token) != null;
    }
}
